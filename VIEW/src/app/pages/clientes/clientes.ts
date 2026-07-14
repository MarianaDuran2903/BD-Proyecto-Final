import { Component, OnInit, computed, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { ClienteService } from '../../services/cliente.service';
import { ParejaService } from '../../services/pareja.service';
import { ClienteResponseDTO, ParejaResponseDTO } from '../../models/model';

@Component({
  selector: 'app-clientes',
  imports: [DisenoAdmin, DecimalPipe],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css',
})
export class Clientes implements OnInit {
  filtro = signal('');
  private clientesBase = signal<ClienteResponseDTO[]>([]);
  private parejasBase = signal<ParejaResponseDTO[]>([]);

  clientesFiltrados = computed<ClienteResponseDTO[]>(() => {
    const texto = this.filtro().trim().toLowerCase();
    if (!texto) return this.clientesBase();
    return this.clientesBase().filter(
      (c) =>
        String(c.id_usuario).includes(texto) ||
        `${c.primer_nombre} ${c.primer_apellido}`.toLowerCase().includes(texto)
    );
  });

  parejasFiltradas = computed<ParejaResponseDTO[]>(() => {
    const texto = this.filtro().trim().toLowerCase();
    if (!texto) return this.parejasBase();
    return this.parejasBase().filter(
      (p) =>
        String(p.id_usuario).includes(texto) ||
        `${p.primer_nombre} ${p.primer_apellido}`.toLowerCase().includes(texto) ||
        p.nombre_cliente_titular.toLowerCase().includes(texto)
    );
  });

  totalAutorizado = computed(() =>
    this.clientesFiltrados().reduce((suma, c) => suma + c.cupo_total_autorizado, 0));
  totalPropio = computed(() =>
    this.clientesFiltrados().reduce((suma, c) => suma + c.cupo_propio, 0));
  totalSaldoReal = computed(() =>
    this.clientesFiltrados().reduce((suma, c) => suma + c.saldo_real, 0));
  totalAsignadoParejas = computed(() =>
    this.clientesFiltrados().reduce((suma, c) => suma + c.cupo_asignado_parejas, 0));
  totalDisponible = computed(() =>
    this.clientesFiltrados().reduce((suma, c) => suma + c.cupo_total_disponible, 0));

  totalCupoAsignado = computed(() =>
    this.parejasFiltradas().reduce((suma, p) => suma + p.cupo_asignado, 0));
  totalCupoGastado = computed(() =>
    this.parejasFiltradas().reduce((suma, p) => suma + p.cupo_gastado, 0));
  totalCupoDisponibleParejas = computed(() =>
    this.parejasFiltradas().reduce((suma, p) => suma + p.cupo_disponible, 0));

  constructor(
    private clienteService: ClienteService,
    private parejaService: ParejaService,
  ) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.clienteService.getClientes().subscribe((data) => this.clientesBase.set(data));
    this.parejaService.getParejas().subscribe((data) => this.parejasBase.set(data));
  }
}
