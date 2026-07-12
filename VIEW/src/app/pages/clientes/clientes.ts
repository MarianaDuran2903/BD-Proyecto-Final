import { Component, OnInit, computed, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { ClienteService } from '../../services/cliente.service';
import { ClienteResponseDTO } from '../../models/model';

@Component({
  selector: 'app-clientes',
  imports: [DisenoAdmin],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css',
})
export class Clientes implements OnInit {
  clientes = signal<ClienteResponseDTO[]>([]);

  totalAutorizado = computed(() =>
    this.clientes().reduce((suma, c) => suma + c.cupo_total_autorizado, 0));
  totalPropio = computed(() =>
    this.clientes().reduce((suma, c) => suma + c.cupo_propio, 0));
  totalAsignadoParejas = computed(() =>
    this.clientes().reduce((suma, c) => suma + c.cupo_asignado_parejas, 0));
  totalDisponible = computed(() =>
    this.clientes().reduce((suma, c) => suma + c.cupo_total_disponible, 0));

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void { this.cargar(); }

  cargar(): void {
    this.clienteService.getClientes().subscribe((data) => this.clientes.set(data));
  }
}
