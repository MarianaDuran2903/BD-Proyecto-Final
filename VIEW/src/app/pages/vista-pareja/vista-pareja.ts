import { Component, OnInit, computed, signal } from '@angular/core';
import { DisenoUsuario } from '../../components/templates/diseno-usuario/diseno-usuario';
import { Estado } from '../../components/atoms/estado/estado';
import { Boton } from '../../components/atoms/boton/boton';
import { CampoFormulario } from '../../components/molecules/campo-formulario/campo-formulario';
import { ParejaService } from '../../services/pareja.service';
import { CompraService } from '../../services/compra.service';
import { RestriccionService } from '../../services/restriccion.service';
import { SobrecupoService } from '../../services/sobrecupo.service';
import { SesionService } from '../../services/sesion.service';
import {
  ParejaResponseDTO,
  CompraResponseDTO,
  RestriccionHorarioDTO,
  SolicitudSobrecupoResponseDTO,
  SolicitudSobrecupoRequestDTO,
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
  selector: 'app-vista-pareja',
  imports: [DisenoUsuario, Estado, Boton, CampoFormulario],
  templateUrl: './vista-pareja.html',
  styleUrl: './vista-pareja.css',
})
export class VistaPareja implements OnInit {
  pareja = signal<ParejaResponseDTO | null>(null);
  compras = signal<CompraResponseDTO[]>([]);
  restricciones = signal<RestriccionHorarioDTO[]>([]);
  solicitudes = signal<SolicitudSobrecupoResponseDTO[]>([]);
  formularioVisible = signal(false);
  errorSolicitud = '';

  form: SolicitudSobrecupoRequestDTO = {
    id_usuario_pareja: 0,
    id_usuario_cliente: 0,
    monto_solicitado: 0
  };

  totalCompras = computed(() =>
    this.compras().reduce((suma, c) => suma + c.monto, 0));

  totalSolicitado = computed(() =>
    this.solicitudes().reduce((suma, s) => suma + s.monto_solicitado, 0));

  constructor(
    private parejaService: ParejaService,
    private compraService: CompraService,
    private restriccionService: RestriccionService,
    private sobrecupoService: SobrecupoService,
    private sesionService: SesionService,
  ) {}

  ngOnInit(): void {
    this.cargarTodo();
  }

  private idParejaActual(): number {
    return this.sesionService.usuario()?.id_usuario ?? 0;
  }

  cargarTodo(): void {
    const id = this.idParejaActual();
    if (!id) return;
    this.parejaService.getPareja(id).subscribe(data => {
      this.pareja.set(data);
      this.form.id_usuario_pareja = data.id_usuario;
      this.form.id_usuario_cliente = data.id_usuario_cliente;
    });
    this.compraService.getComprasPorPareja(id).subscribe(data => this.compras.set(data));
    this.restriccionService.getRestriccionesPorPareja(id).subscribe(data => this.restricciones.set(data));
    this.sobrecupoService.getSolicitudesPorPareja(id).subscribe(data => this.solicitudes.set(data));
  }

  abrirFormulario(): void {
    this.form.monto_solicitado = 0;
    this.errorSolicitud = '';
    this.formularioVisible.set(true);
  }

  solicitarSobrecupo(): void {
    if (!this.form.monto_solicitado || this.form.monto_solicitado <= 0) {
      this.errorSolicitud = 'Ingresa un monto válido.';
      return;
    }
    this.sobrecupoService.crearSolicitud(this.form).subscribe({
      next: () => {
        this.formularioVisible.set(false);
        this.cargarTodo();
      },
      error: (e) => {
        this.errorSolicitud = e.status === 400
          ? (e.error?.mensaje ?? 'No se pudo enviar la solicitud.')
          : 'Error al solicitar sobrecupo.';
      }
    });
  }

  etiquetaEstado(estado: EstadoSolicitudSobrecupo): string {
    return ETIQUETAS_ESTADO[estado];
  }
}
