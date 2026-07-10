import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CampoEntrada } from '../../atoms/campo-entrada/campo-entrada';

@Component({
  selector: 'app-campo-formulario',
  imports: [CampoEntrada],
  templateUrl: './campo-formulario.html',
  styleUrl: './campo-formulario.css',
})
export class CampoFormulario {
  @Input() id = '';
  @Input() etiqueta = '';
  @Input() tipo: 'text' | 'email' | 'password' | 'number' | 'date' | 'time' = 'text';
  @Input() placeholder = '';
  @Input() valor: string | number = '';
  @Input() error = '';
  @Output() valorChange = new EventEmitter<string>();
}
