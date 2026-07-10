import { Component } from '@angular/core';
import { BarraNavegacion } from '../../organisms/barra-navegacion/barra-navegacion';
import { BarraLateral } from '../../organisms/barra-lateral/barra-lateral';

@Component({
  selector: 'app-diseno-admin',
  imports: [BarraNavegacion, BarraLateral],
  templateUrl: './diseno-admin.html',
  styleUrl: './diseno-admin.css',
})
export class DisenoAdmin {}
