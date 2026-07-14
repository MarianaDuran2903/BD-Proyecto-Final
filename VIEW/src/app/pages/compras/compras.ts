import { Component, OnInit, signal, computed } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { CampoFormulario } from '../../components/molecules/campo-formulario/campo-formulario';
import { Boton } from '../../components/atoms/boton/boton';
import { CompraService } from '../../services/compra.service';
import { ParejaService } from '../../services/pareja.service';
import { ClienteService } from '../../services/cliente.service';
import { AlmacenService } from '../../services/almacen.service';
import { SesionService } from '../../services/sesion.service';
import {
  CompraResponseDTO,
  CompraRequestDTO,
  ParejaResponseDTO,
  ClienteResponseDTO,
  AlmacenDTO,
} from '../../models/model';

@Component({
  selector: 'app-compras',
  imports: [DisenoAdmin, CampoFormulario, Boton, DecimalPipe],
  templateUrl: './compras.html',
  styleUrl: './compras.css',
})
export class Compras implements OnInit {
  compras = signal<CompraResponseDTO[]>([]);
  parejas = signal<ParejaResponseDTO[]>([]);
  clientes = signal<ClienteResponseDTO[]>([]);
  almacenes = signal<AlmacenDTO[]>([]);
  error = '';

  tipoUsuario: 'PAREJA' | 'CLIENTE' = 'PAREJA';
  idUsuarioSeleccionado = 0;
  idAlmacenSeleccionado = 0;
  monto = 0;

  totalCompras = computed(() =>
    this.compras().reduce((acc, c) => acc + Number(c.monto), 0)
  );

  constructor(
    private compraService: CompraService,
    private parejaService: ParejaService,
    private clienteService: ClienteService,
    private almacenService: AlmacenService,
    private sesionService: SesionService,
  ) {}

  ngOnInit(): void {
    this.cargarHistorial();
    this.parejaService.getParejas().subscribe((data) => this.parejas.set(data));
    this.clienteService.getClientes().subscribe((data) => this.clientes.set(data));
    this.almacenService.getAlmacenes().subscribe((data) => this.almacenes.set(data));
  }

  cargarHistorial(): void {
    this.compraService.getCompras().subscribe((data) => this.compras.set(data));
  }

  cambiarTipoUsuario(tipo: 'PAREJA' | 'CLIENTE'): void {
    this.tipoUsuario = tipo;
    this.idUsuarioSeleccionado = 0;
  }

  confirmarCompra(): void {
    this.error = '';
    const usuario = this.sesionService.usuario();
    if (!usuario) return;

    if (!this.idUsuarioSeleccionado || !this.idAlmacenSeleccionado || this.monto <= 0) {
      this.error = 'Selecciona usuario, almacén e ingresa un monto válido.';
      return;
    }

    const ahora = new Date();
    const fecha = ahora.toISOString().slice(0, 10);
    const hora = ahora.toTimeString().slice(0, 8);

    const body: CompraRequestDTO = {
      monto: this.monto,
      fecha,
      hora,
      id_almacen: this.idAlmacenSeleccionado,
      id_usuario_supervisor: usuario.id_usuario,
      ...(this.tipoUsuario === 'PAREJA'
        ? { id_usuario_pareja: this.idUsuarioSeleccionado }
        : { id_usuario_cliente: this.idUsuarioSeleccionado }),
    };

    this.compraService.registrarCompra(body).subscribe({
      next: () => {
        this.idUsuarioSeleccionado = 0;
        this.idAlmacenSeleccionado = 0;
        this.monto = 0;
        this.cargarHistorial();
      },
      error: (e) => {
        this.error = e.error?.mensaje ?? 'No se pudo registrar la compra.';
      },
    });
  }
}
