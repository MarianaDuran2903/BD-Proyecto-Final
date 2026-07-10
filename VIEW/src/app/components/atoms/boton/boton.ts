import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-boton',
  imports: [],
  templateUrl: './boton.html',
  styleUrl: './boton.css',
})
export class Boton {
  @Input() etiqueta = '';
  @Input() variante: 'primario' | 'secundario' | 'peligro' | 'fantasma' = 'primario';
  @Input() tipo: 'button' | 'submit' = 'button';
  @Input() deshabilitado = false;
  @Input() anchoCompleto = false;
}
