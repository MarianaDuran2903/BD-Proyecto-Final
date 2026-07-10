import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { SesionService } from '../../../services/sesion.service';

@Component({
  selector: 'app-barra-navegacion',
  imports: [],
  templateUrl: './barra-navegacion.html',
  styleUrl: './barra-navegacion.css',
})
export class BarraNavegacion {
  private sesion = inject(SesionService);
  private router = inject(Router);

  get nombreUsuario(): string {
    return this.sesion.usuario()?.nombre_usuario ?? '';
  }

  cerrarSesion(): void {
    this.sesion.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
