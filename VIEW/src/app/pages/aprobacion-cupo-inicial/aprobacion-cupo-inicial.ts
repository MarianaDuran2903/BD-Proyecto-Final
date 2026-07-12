import { Component, OnInit, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { Boton } from '../../components/atoms/boton/boton';
import { ClienteService } from '../../services/cliente.service';
import { ClienteResponseDTO } from '../../models/model';

interface FilaPendiente {
  cliente: ClienteResponseDTO;
  montoEditable: number;
}

@Component({
  selector: 'app-aprobacion-cupo-inicial',
  imports: [DisenoAdmin, Boton],
  templateUrl: './aprobacion-cupo-inicial.html',
  styleUrl: './aprobacion-cupo-inicial.css',
})
export class AprobacionCupoInicial implements OnInit {
  filas = signal<FilaPendiente[]>([]);
  error = '';

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.clienteService.getPendientes().subscribe(data => {
      this.filas.set(data.map(cliente => ({
        cliente,
        montoEditable: cliente.cupo_total_solicitado ?? 0
      })));
    });
  }

  actualizarMonto(idUsuario: number, valor: string): void {
    this.filas.update(filas => filas.map(f =>
      f.cliente.id_usuario === idUsuario ? { ...f, montoEditable: +valor } : f
    ));
  }

  aprobar(idUsuario: number): void {
    this.error = '';
    const fila = this.filas().find(f => f.cliente.id_usuario === idUsuario);
    if (!fila) return;
    if (fila.montoEditable < 0) {
      this.error = 'El cupo autorizado no puede ser negativo.';
      return;
    }
    this.clienteService.aprobarCupoInicial(idUsuario, { cupo_autorizado: fila.montoEditable }).subscribe({
      next: () => this.cargar(),
      error: (e) => {
        this.error = e.status === 400
          ? (e.error?.mensaje ?? 'No se pudo aprobar el cupo inicial.')
          : 'Error al aprobar el cupo inicial.';
      }
    });
  }
}
