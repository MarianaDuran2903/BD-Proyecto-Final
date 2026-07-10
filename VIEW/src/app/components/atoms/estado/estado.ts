import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-estado',
  imports: [CommonModule],
  templateUrl: './estado.html',
  styleUrl: './estado.css',
})
export class Estado {
  @Input() valor = '';
}
