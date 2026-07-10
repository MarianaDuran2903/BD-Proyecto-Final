import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-campo-entrada',
  imports: [],
  templateUrl: './campo-entrada.html',
  styleUrl: './campo-entrada.css',
})
export class CampoEntrada {
  @Input() id = '';
  @Input() tipo: 'text' | 'email' | 'password' | 'number' | 'date' | 'time' = 'text';
  @Input() placeholder = '';
  @Input() valor: string | number = '';
  @Input() deshabilitado = false;
  @Input() tooltip = '';
  @Output() valorChange = new EventEmitter<string>();

  alEscribir(event: Event): void {
    this.valorChange.emit((event.target as HTMLInputElement).value);
  }
}
