import { Component, EventEmitter, Output } from '@angular/core';
import { CampoFormulario } from '../../molecules/campo-formulario/campo-formulario';
import { Boton } from '../../atoms/boton/boton';
import { ClienteRequestDTO } from '../../../models/model';

@Component({
  selector: 'app-formulario-registro',
  imports: [CampoFormulario, Boton],
  templateUrl: './formulario-registro.html',
  styleUrl: './formulario-registro.css',
})
export class FormularioRegistro {
  @Output() enviarFormulario = new EventEmitter<ClienteRequestDTO>();
  @Output() irALogin = new EventEmitter<void>();

  primerNombre = '';
  segundoNombre = '';
  primerApellido = '';
  segundoApellido = '';
  documento = '';
  contrasenia = '';
  cupoSolicitado = '';
  error = '';

  alEnviar(): void {
    if (!this.primerNombre || !this.primerApellido || !this.documento || !this.contrasenia || !this.cupoSolicitado) {
      this.error = 'Completa todos los campos obligatorios.';
      return;
    }
    if (this.contrasenia.length < 4) {
      this.error = 'La contraseña debe tener mínimo 4 caracteres.';
      return;
    }
    if (Number(this.cupoSolicitado) < 0) {
      this.error = 'El cupo total solicitado no puede ser negativo.';
      return;
    }

    this.error = '';
    const nombreUsuarioGenerado = `${this.primerNombre.trim().toLowerCase()}${this.documento.trim()}`;

    this.enviarFormulario.emit({
      id_usuario: Number(this.documento),
      nombre_usuario: nombreUsuarioGenerado,
      contrasenia: this.contrasenia,
      primer_nombre: this.primerNombre,
      segundo_nombre: this.segundoNombre || undefined,
      primer_apellido: this.primerApellido,
      segundo_apellido: this.segundoApellido || undefined,
      cupo_propio: Number(this.cupoSolicitado),
    });
  }
}
