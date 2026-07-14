import { Component, OnInit, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { Boton } from '../../components/atoms/boton/boton';
import { Estado } from '../../components/atoms/estado/estado';
import { SobrecupoService } from '../../services/sobrecupo.service';
import { SesionService } from '../../services/sesion.service';
import { EstadoSolicitudSobrecupo, SolicitudSobrecupoResponseDTO } from '../../models/model';

const ETIQUETAS_ESTADO: Record<EstadoSolicitudSobrecupo, string> = {
  pendiente_cliente: 'Pendiente Cliente',
  aprobada_directa: 'Aprobada Cliente',
  pendiente_supervisor: 'Pendiente Supervisor',
  aprobada_supervisor: 'Aprobada Supervisor',
  rechazada_cliente: 'Rechazada',
  rechazada_supervisor: 'Rechazada',
};

@Component({
  selector: 'app-solicitudes-sobrecupo',
  imports: [DisenoAdmin, Boton, Estado, DecimalPipe],
  templateUrl: './solicitudes-sobrecupo.html',
  styleUrl: './solicitudes-sobrecupo.css',
})
export class SolicitudesSobrecupo implements OnInit {
  solicitudes = signal<SolicitudSobrecupoResponseDTO[]>([]);
  error = '';

  constructor(
    private sobrecupoService: SobrecupoService,
    private sesionService: SesionService,
  ) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    const usuario = this.sesionService.usuario();
    if (!usuario) return;
    this.sobrecupoService.getPendientesPorSupervisor(usuario.id_usuario).subscribe((data) =>
      this.solicitudes.set(data)
    );
  }

  etiquetaEstado(estado: EstadoSolicitudSobrecupo): string {
    return ETIQUETAS_ESTADO[estado];
  }

  aprobar(id: number): void {
    this.error = '';
    this.sobrecupoService.decidirComoSupervisor(id, { decision: 'Aprobar' }).subscribe({
      next: () => this.cargar(),
      error: (e) => this.error = e.error?.mensaje ?? 'Error al aprobar.',
    });
  }

  rechazar(id: number): void {
    this.error = '';
    this.sobrecupoService.decidirComoSupervisor(id, { decision: 'Rechazar' }).subscribe({
      next: () => this.cargar(),
      error: (e) => this.error = e.error?.mensaje ?? 'Error al rechazar.',
    });
  }
}
