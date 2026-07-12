import { Component, OnInit, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { Boton } from '../../components/atoms/boton/boton';
import { Estado } from '../../components/atoms/estado/estado';
import { SobrecupoService } from '../../services/sobrecupo.service';
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
  imports: [DisenoAdmin, Boton, Estado],
  templateUrl: './solicitudes-sobrecupo.html',
  styleUrl: './solicitudes-sobrecupo.css',
})
export class SolicitudesSobrecupo implements OnInit {
  solicitudes = signal<SolicitudSobrecupoResponseDTO[]>([]);

  constructor(private sobrecupoService: SobrecupoService) {}

  ngOnInit(): void {
    this.sobrecupoService.getSolicitudes().subscribe(data => this.solicitudes.set(data));
  }

  etiquetaEstado(estado: EstadoSolicitudSobrecupo): string {
    return ETIQUETAS_ESTADO[estado];
  }

  aprobar(id: number): void {
    this.sobrecupoService.decidirComoCliente(id, { decision: 'Aprobar' }).subscribe({
      next: () => this.ngOnInit(),
      error: (e) => console.error('Error al aprobar.', e)
    });
  }

  rechazar(id: number): void {
    this.sobrecupoService.decidirComoCliente(id, { decision: 'Rechazar' }).subscribe({
      next: () => this.ngOnInit(),
      error: (e) => console.error('Error al rechazar.', e)
    });
  }
}
