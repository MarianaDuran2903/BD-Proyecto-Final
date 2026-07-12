import { Component, EventEmitter, Output } from '@angular/core';
import { CampoFormulario } from '../../molecules/campo-formulario/campo-formulario';
import { Boton } from '../../atoms/boton/boton';
import { LoginRequestDTO } from '../../../models/model';

@Component({
  selector: 'app-formulario-login',
  imports: [CampoFormulario, Boton],
  templateUrl: './formulario-login.html',
  styleUrl: './formulario-login.css',
})
export class FormularioLogin {
  @Output() enviarFormulario = new EventEmitter<LoginRequestDTO>();
  @Output() irARegistro = new EventEmitter<void>();

  idUsuario = '';
  contrasenia = '';
  error = '';

  alEnviar(): void {
    if (!this.idUsuario || !this.contrasenia) {
      this.error = 'Completa todos los campos.';
      return;
    }
    this.error = '';
    this.enviarFormulario.emit({
      id_usuario: Number(this.idUsuario),
      contrasenia: this.contrasenia
    });
  }
}
