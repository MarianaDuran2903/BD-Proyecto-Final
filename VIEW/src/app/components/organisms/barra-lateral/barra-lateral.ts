import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { SesionService } from '../../../services/sesion.service';

@Component({
  selector: 'app-barra-lateral',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './barra-lateral.html',
  styleUrl: './barra-lateral.css',
})
export class BarraLateral {
  readonly elementosNav = [
    { etiqueta: 'Clientes y Parejas', ruta: '/clientes' },
    { etiqueta: 'Aprobación Cupo Inicial', ruta: '/aprobacion-cupo-inicial' },
    { etiqueta: 'Parejas', ruta: '/parejas' },
    { etiqueta: 'Compras', ruta: '/compras' },
    { etiqueta: 'Restricciones', ruta: '/restricciones' },
    { etiqueta: 'Solicitudes de Sobrecupo', ruta: '/solicitudes-sobrecupo' },
  ];

  constructor(
    private sesionService: SesionService,
    private router: Router,
  ) {}

  get usuario() {
    return this.sesionService.usuario();
  }

  cerrarSesion(): void {
    this.sesionService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
