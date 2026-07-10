import { Component, OnInit, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { CompraService } from '../../services/compra.service';
import { CompraResponseDTO } from '../../models/model';

@Component({
  selector: 'app-compras',
  imports: [DisenoAdmin],
  templateUrl: './compras.html',
  styleUrl: './compras.css',
})
export class Compras implements OnInit {
  compras = signal<CompraResponseDTO[]>([]);

  constructor(private compraService: CompraService) {}

  ngOnInit(): void {
    this.compraService.getCompras().subscribe(data => this.compras.set(data));
  }
}
