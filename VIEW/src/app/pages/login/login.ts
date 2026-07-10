import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { DisenoAuth } from '../../components/templates/diseno-auth/diseno-auth';
import { FormularioLogin } from '../../components/organisms/formulario-login/formulario-login';
import { AuthService } from '../../services/auth.service';
import { SesionService } from '../../services/sesion.service';
import { LoginRequestDTO } from '../../models/model';

@Component({
  selector: 'app-login',
  imports: [DisenoAuth, FormularioLogin],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  constructor(
    private authService: AuthService,
    private sesionService: SesionService,
    private router: Router,
  ) {}

  alIniciarSesion(data: LoginRequestDTO): void {
    this.authService.login(data).subscribe({
      next: (usuario) => {
        this.sesionService.setUsuario(usuario);
        if (usuario.tipo === 'CLIENTE') this.router.navigate(['/vista-cliente']);
        else if (usuario.tipo === 'PAREJA') this.router.navigate(['/vista-pareja']);
        else this.router.navigate(['/clientes']);
      },
      error: (e) => {
        const msg = e.status === 401 || e.status === 404
          ? 'Usuario o contraseña incorrectos.'
          : 'Error al iniciar sesión. Intenta de nuevo.';
        console.error(msg);
      },
    });
  }
}
