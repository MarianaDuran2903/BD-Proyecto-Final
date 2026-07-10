import { Component, OnInit, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { Boton } from '../../components/atoms/boton/boton';
import { Estado } from '../../components/atoms/estado/estado';
import { SobrecupoService } from '../../services/sobrecupo.service';
import { SolicitudSobrecupoResponseDTO } from '../../models/model';

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
