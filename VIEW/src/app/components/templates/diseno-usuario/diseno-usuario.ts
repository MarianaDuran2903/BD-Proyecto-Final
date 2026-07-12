import { Component, Input, inject } from '@angular/core';
import { Router } from '@angular/router';
import { SesionService } from '../../../services/sesion.service';

@Component({
  selector: 'app-diseno-usuario',
  imports: [],
  templateUrl: './diseno-usuario.html',
  styleUrl: './diseno-usuario.css',
})
export class DisenoUsuario {
  @Input() rol = '';

  private sesion = inject(SesionService);
  private router = inject(Router);

  get nombreUsuario(): string {
    const usuario = this.sesion.usuario();
    return usuario ? `${usuario.primer_nombre}` : '';
  }

  cerrarSesion(): void {
    this.sesion.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
