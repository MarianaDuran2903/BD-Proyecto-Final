import { Component, OnInit, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
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
  SolicitudSobrecupoRequestDTO
} from '../../models/model';

@Component({
  selector: 'app-vista-pareja',
  imports: [DisenoAdmin, Estado, Boton, CampoFormulario],
  templateUrl: './vista-pareja.html',
  styleUrl: './vista-pareja.css',
})
export class VistaPareja implements OnInit {
  pareja = signal<ParejaResponseDTO | null>(null);
  compras = signal<CompraResponseDTO[]>([]);
  restricciones = signal<RestriccionHorarioDTO[]>([]);
  solicitudes = signal<SolicitudSobrecupoResponseDTO[]>([]);
  formularioVisible = signal(false);

  form: SolicitudSobrecupoRequestDTO = {
    id_usuario_pareja: 0,
    id_usuario_cliente: 0,
    monto_solicitado: 0,
  };

  constructor(
    private parejaService: ParejaService,
    private compraService: CompraService,
    private restriccionService: RestriccionService,
    private sobrecupoService: SobrecupoService,
    private sesionService: SesionService,
  ) {}

  ngOnInit(): void {
    const usuario = this.sesionService.usuario();
    if (usuario) {
      const id = usuario.id_usuario;
      this.parejaService.getPareja(id).subscribe(data => {
        this.pareja.set(data);
        this.form.id_usuario_pareja = data.id_usuario;
        this.form.id_usuario_cliente = data.id_usuario_cliente;
      });
      this.compraService.getComprasPorPareja(id).subscribe(data => this.compras.set(data));
      this.restriccionService.getRestriccionesPorPareja(id).subscribe(data => this.restricciones.set(data));
      this.sobrecupoService.getSolicitudesPorPareja(id).subscribe(data => this.solicitudes.set(data));
    }
  }

  solicitarSobrecupo(): void {
    this.sobrecupoService.crearSolicitud(this.form).subscribe({
      next: () => {
        this.formularioVisible.set(false);
        const usuario = this.sesionService.usuario();
        if (usuario) {
          this.sobrecupoService.getSolicitudesPorPareja(usuario.id_usuario).subscribe(data => this.solicitudes.set(data));
        }
      },
      error: (e) => console.error('Error al solicitar sobrecupo.', e)
    });
  }
}
