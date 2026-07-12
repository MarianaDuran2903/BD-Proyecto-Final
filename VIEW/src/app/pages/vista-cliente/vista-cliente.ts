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
  editandoId = signal<number | null>(null);
  formularioRestriccionVisible = signal(false);
  formularioCupoPropioVisible = signal(false);
  nuevoCupoPropio = 0;
  errorCupoPropio = '';
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

  abrirFormularioCupoPropio(): void {
    this.nuevoCupoPropio = this.cliente()?.cupo_propio ?? 0;
    this.errorCupoPropio = '';
    this.formularioCupoPropioVisible.set(true);
  }

  guardarCupoPropio(): void {
    const id = this.idClienteActual();
    if (!id) return;
    if (this.nuevoCupoPropio == null || this.nuevoCupoPropio < 0) {
      this.errorCupoPropio = 'Ingresa un valor válido.';
      return;
    }
    this.clienteService.editarCupoPropio(id, { cupo_propio: this.nuevoCupoPropio }).subscribe({
      next: () => {
        this.formularioCupoPropioVisible.set(false);
        this.cargarTodo();
      },
      error: (e) => {
        this.errorCupoPropio = e.error?.mensaje ?? 'No se pudo actualizar el cupo propio.';
      }
    });
  }

  abrirFormularioPareja(): void {
    this.formPareja = this.formParejaVacio();
    this.editandoId.set(null);
    this.errorPareja = '';
    this.formularioParejaVisible.set(true);
  }

  abrirFormularioEditarPareja(p: ParejaResponseDTO): void {
    this.formPareja = {
      id_usuario: p.id_usuario,
      nombre_usuario: p.nombre_usuario,
      contrasenia: p.contrasenia,
      primer_nombre: p.primer_nombre,
      segundo_nombre: p.segundo_nombre,
      primer_apellido: p.primer_apellido,
      segundo_apellido: p.segundo_apellido,
      telefono: p.telefono,
      cupo_asignado: p.cupo_asignado,
      id_usuario_cliente: p.id_usuario_cliente,
    };
    this.editandoId.set(p.id_usuario);
    this.errorPareja = '';
    this.formularioParejaVisible.set(true);
  }

  guardarPareja(): void {
    if (!this.formPareja.id_usuario || !this.formPareja.contrasenia
      || !this.formPareja.primer_nombre || !this.formPareja.primer_apellido
      || this.formPareja.cupo_asignado == null || this.formPareja.cupo_asignado < 0) {
      this.errorPareja = 'Completa todos los campos obligatorios.';
      return;
    }
    this.formPareja.id_usuario_cliente = this.idClienteActual();

    const idEditando = this.editandoId();
    if (idEditando) {
      // Editando una pareja existente: no se regenera nombre_usuario, se conserva el que ya tenía.
      this.parejaService.actualizarPareja(idEditando, this.formPareja).subscribe({
        next: () => {
          this.formularioParejaVisible.set(false);
          this.editandoId.set(null);
          this.cargarTodo();
        },
        error: (e) => {
          this.errorPareja = e.status === 400
            ? (e.error?.mensaje ?? 'El cupo asignado excede el cupo total disponible.')
            : 'Error al actualizar la pareja.';
        }
      });
      return;
    }

    // El mockup no pide "Nombre de Usuario" para la Pareja: se genera igual
    // que en el registro público de Cliente (nombre + documento).
    this.formPareja.nombre_usuario = `${this.formPareja.primer_nombre.trim().toLowerCase()}${this.formPareja.id_usuario}`;
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

  inactivarPareja(id: number): void {
    if (!confirm('¿Inactivar esta pareja? Su cupo asignado volverá a tu cupo propio.')) return;
    this.parejaService.inactivarPareja(id).subscribe({
      next: () => this.cargarTodo(),
      error: (e) => this.errorPareja = e.error?.mensaje ?? 'No se pudo inactivar la pareja.'
    });
  }

  activarPareja(id: number): void {
    this.parejaService.activarPareja(id).subscribe({
      next: () => this.cargarTodo(),
      error: (e) => this.errorPareja = e.error?.mensaje ?? 'No se pudo activar la pareja.'
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
