import { Component, OnInit, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { Estado } from '../../components/atoms/estado/estado';
import { Boton } from '../../components/atoms/boton/boton';
import { CampoFormulario } from '../../components/molecules/campo-formulario/campo-formulario';
import { ClienteService } from '../../services/cliente.service';
import { ParejaService } from '../../services/pareja.service';
import { CompraService } from '../../services/compra.service';
import { SobrecupoService } from '../../services/sobrecupo.service';
import { SesionService } from '../../services/sesion.service';
import {
  ClienteResponseDTO,
  ParejaResponseDTO,
  CompraResponseDTO,
  SolicitudSobrecupoResponseDTO
} from '../../models/model';

@Component({
  selector: 'app-vista-cliente',
  imports: [DisenoAdmin, Estado, Boton, CampoFormulario],
  templateUrl: './vista-cliente.html',
  styleUrl: './vista-cliente.css',
})
export class VistaCliente implements OnInit {
  cliente = signal<ClienteResponseDTO | null>(null);
  parejas = signal<ParejaResponseDTO[]>([]);
  compras = signal<CompraResponseDTO[]>([]);
  solicitudes = signal<SolicitudSobrecupoResponseDTO[]>([]);

  constructor(
    private clienteService: ClienteService,
    private parejaService: ParejaService,
    private compraService: CompraService,
    private sobrecupoService: SobrecupoService,
    private sesionService: SesionService,
  ) {}

  ngOnInit(): void {
    const usuario = this.sesionService.usuario();
    if (usuario) {
      const id = usuario.id_usuario;
      this.clienteService.getCliente(id).subscribe(data => this.cliente.set(data));
      this.parejaService.getParejasPorCliente(id).subscribe(data => this.parejas.set(data));
      this.compraService.getComprasPorCliente(id).subscribe(data => this.compras.set(data));
      this.sobrecupoService.getSolicitudesPorCliente(id).subscribe(data => this.solicitudes.set(data));
    }
  }
}
