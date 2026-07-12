import { Component, OnInit, computed, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { DisenoUsuario } from '../../components/templates/diseno-usuario/diseno-usuario';
import { Estado } from '../../components/atoms/estado/estado';
import { Boton } from '../../components/atoms/boton/boton';
import { CampoFormulario } from '../../components/molecules/campo-formulario/campo-formulario';
import { ClienteService } from '../../services/cliente.service';
import { ParejaService } from '../../services/pareja.service';
import { CompraService } from '../../services/compra.service';
import { SobrecupoService } from '../../services/sobrecupo.service';
import { RestriccionService } from '../../services/restriccion.service';
import { SesionService } from '../../services/sesion.service';
import {
  ClienteResponseDTO,
  ParejaResponseDTO,
  ParejaRequestDTO,
  CompraResponseDTO,
  SolicitudSobrecupoResponseDTO,
  RestriccionHorarioDTO,
  EstadoSolicitudSobrecupo
} from '../../models/model';

const ETIQUETAS_ESTADO: Record<EstadoSolicitudSobrecupo, string> = {
  pendiente_cliente: 'Pendiente',
  aprobada_directa: 'Aprobada Directa',
  pendiente_supervisor: 'Escalada a Supervisor',
  aprobada_supervisor: 'Aprobada Supervisor',
  rechazada_cliente: 'Rechazada',
  rechazada_supervisor: 'Rechazada',
};

@Component({
  selector: 'app-vista-cliente',
  imports: [DisenoUsuario, Estado, Boton, CampoFormulario],
  templateUrl: './vista-cliente.html',
  styleUrl: './vista-cliente.css',
})
export class VistaCliente implements OnInit {
  cliente = signal<ClienteResponseDTO | null>(null);
  parejas = signal<ParejaResponseDTO[]>([]);
  compras = signal<CompraResponseDTO[]>([]);
  solicitudes = signal<SolicitudSobrecupoResponseDTO[]>([]);
  restricciones = signal<RestriccionHorarioDTO[]>([]);

  formularioParejaVisible = signal(false);
  formularioRestriccionVisible = signal(false);
  errorPareja = '';
  errorRestriccion = '';
  errorSolicitud = signal('');

  formPareja: ParejaRequestDTO = {
    id_usuario: 0,
    nombre_usuario: '',
    contrasenia: '',
    primer_nombre: '',
    primer_apellido: '',
    cupo_asignado: 0,
    id_usuario_cliente: 0,
  };
  formRestriccion: RestriccionHorarioDTO = {
    motivo: '',
    dia_bloqueo: '',
    hora_bloqueo_inicio: '',
    hora_bloqueo_fin: '',
    id_usuario_pareja: 0,
  };

  totalCupoAsignado = computed(() =>
    this.parejas().reduce((suma, p) => suma + p.cupo_asignado, 0));
  totalCupoGastadoParejas = computed(() =>
    this.parejas().reduce((suma, p) => suma + p.cupo_gastado, 0));
  totalCupoDisponible = computed(() =>
    this.parejas().reduce((suma, p) => suma + p.cupo_disponible, 0));

  totalCompras = computed(() =>
    this.compras().reduce((suma, c) => suma + c.monto, 0));

  totalSolicitado = computed(() =>
    this.solicitudes().reduce((suma, s) => suma + s.monto_solicitado, 0));

  constructor(
    private clienteService: ClienteService,
    private parejaService: ParejaService,
    private compraService: CompraService,
    private sobrecupoService: SobrecupoService,
    private restriccionService: RestriccionService,
    private sesionService: SesionService,
  ) {}

  ngOnInit(): void {
    this.cargarTodo();
  }

  private idClienteActual(): number {
    return this.sesionService.usuario()?.id_usuario ?? 0;
  }

  cargarTodo(): void {
    const id = this.idClienteActual();
    if (!id) return;
    this.clienteService.getCliente(id).subscribe(data => this.cliente.set(data));
    this.compraService.getComprasPorCliente(id).subscribe(data => this.compras.set(data));
    this.sobrecupoService.getSolicitudesPorCliente(id).subscribe(data => this.solicitudes.set(data));

    this.parejaService.getParejasPorCliente(id).subscribe(parejas => {
      this.parejas.set(parejas);
      if (parejas.length === 0) {
        this.restricciones.set([]);
        return;
      }
      forkJoin(parejas.map(p => this.restriccionService.getRestriccionesPorPareja(p.id_usuario)))
        .subscribe(listas => this.restricciones.set(listas.flat()));
    });
  }

  private formParejaVacio(): ParejaRequestDTO {
    return {
      id_usuario: 0,
      nombre_usuario: '',
      contrasenia: '',
      primer_nombre: '',
      primer_apellido: '',
      cupo_asignado: 0,
      id_usuario_cliente: this.idClienteActual(),
    };
  }

  private formRestriccionVacio(): RestriccionHorarioDTO {
    return {
      motivo: '',
      dia_bloqueo: '',
      hora_bloqueo_inicio: '',
      hora_bloqueo_fin: '',
      id_usuario_pareja: 0,
    };
  }

  abrirFormularioPareja(): void {
    this.formPareja = this.formParejaVacio();
    this.errorPareja = '';
    this.formularioParejaVisible.set(true);
  }

  guardarPareja(): void {
    if (!this.formPareja.id_usuario || !this.formPareja.nombre_usuario || !this.formPareja.contrasenia
      || !this.formPareja.primer_nombre || !this.formPareja.primer_apellido) {
      this.errorPareja = 'Completa todos los campos obligatorios.';
      return;
    }
    this.formPareja.id_usuario_cliente = this.idClienteActual();
    this.parejaService.crearPareja(this.formPareja).subscribe({
      next: () => {
        this.formularioParejaVisible.set(false);
        this.cargarTodo();
      },
      error: (e) => {
        this.errorPareja = e.status === 400
          ? (e.error?.mensaje ?? 'El cupo asignado excede el cupo propio disponible.')
          : 'Error al crear la pareja.';
      }
    });
  }

  abrirFormularioRestriccion(): void {
    this.formRestriccion = this.formRestriccionVacio();
    this.errorRestriccion = '';
    this.formularioRestriccionVisible.set(true);
  }

  guardarRestriccion(): void {
    if (!this.formRestriccion.id_usuario_pareja || !this.formRestriccion.dia_bloqueo
      || !this.formRestriccion.hora_bloqueo_inicio || !this.formRestriccion.hora_bloqueo_fin) {
      this.errorRestriccion = 'Completa todos los campos obligatorios.';
      return;
    }
    this.restriccionService.crearRestriccion(this.formRestriccion).subscribe({
      next: () => {
        this.formularioRestriccionVisible.set(false);
        this.cargarTodo();
      },
      error: (e) => {
        this.errorRestriccion = e.status === 400
          ? (e.error?.mensaje ?? 'Horario inválido.')
          : 'Error al crear la restricción.';
      }
    });
  }

  eliminarRestriccion(id: number): void {
    this.restriccionService.eliminarRestriccion(id).subscribe({
      next: () => this.cargarTodo(),
      error: (e) => console.error('Error al eliminar la restricción.', e)
    });
  }

  etiquetaEstado(estado: EstadoSolicitudSobrecupo): string {
    return ETIQUETAS_ESTADO[estado];
  }

  nombrePareja(idUsuarioPareja: number): string {
    const pareja = this.parejas().find(p => p.id_usuario === idUsuarioPareja);
    return pareja ? `${pareja.primer_nombre} ${pareja.primer_apellido}` : String(idUsuarioPareja);
  }

  aprobarSolicitud(id: number): void {
    this.errorSolicitud.set('');
    this.sobrecupoService.decidirComoCliente(id, { decision: 'Aprobar' }).subscribe({
      next: () => this.cargarTodo(),
      error: (e) => {
        this.errorSolicitud.set(e.status === 400
          ? (e.error?.mensaje ?? 'Cupo propio insuficiente para aprobar directamente.')
          : 'Error al aprobar la solicitud.');
      }
    });
  }

  escalarSolicitud(id: number): void {
    this.errorSolicitud.set('');
    this.sobrecupoService.decidirComoCliente(id, { decision: 'Escalar' }).subscribe({
      next: () => this.cargarTodo(),
      error: (e) => {
        this.errorSolicitud.set(e.status === 400
          ? (e.error?.mensaje ?? 'No se pudo escalar la solicitud.')
          : 'Error al escalar la solicitud.');
      }
    });
  }

  rechazarSolicitud(id: number): void {
    this.errorSolicitud.set('');
    this.sobrecupoService.decidirComoCliente(id, { decision: 'Rechazar' }).subscribe({
      next: () => this.cargarTodo(),
      error: (e) => this.errorSolicitud.set('Error al rechazar la solicitud.')
    });
  }
}
