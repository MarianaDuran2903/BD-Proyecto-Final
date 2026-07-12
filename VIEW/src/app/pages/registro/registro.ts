import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { DisenoAuth } from '../../components/templates/diseno-auth/diseno-auth';
import { FormularioRegistro } from '../../components/organisms/formulario-registro/formulario-registro';
import { ClienteService } from '../../services/cliente.service';
import { ClienteRequestDTO } from '../../models/model';

@Component({
  selector: 'app-registro',
  imports: [DisenoAuth, FormularioRegistro],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class Registro {
  constructor(
    private clienteService: ClienteService,
    private router: Router,
  ) {}

  alRegistrar(data: ClienteRequestDTO): void {
    this.clienteService.crearCliente(data).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (e) => {
        const msg = e.status === 409 || e.status === 400
          ? 'No se pudo crear la cuenta. Verifica que el documento no esté ya registrado.'
          : 'Error al registrar. Intenta de nuevo.';
        console.error(msg);
      },
    });
  }

  irALogin(): void {
    this.router.navigate(['/login']);
  }
}
