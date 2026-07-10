import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CompraRequestDTO, CompraResponseDTO } from '../models/model';

@Injectable({ providedIn: 'root' })
export class CompraService {

  private readonly BASE_URL = 'http://localhost:8585/compra/api/compras';

  constructor(private http: HttpClient) {}

  getCompras(): Observable<CompraResponseDTO[]> {
    return this.http.get<CompraResponseDTO[]>(this.BASE_URL);
  }

  getCompra(id: number): Observable<CompraResponseDTO> {
    return this.http.get<CompraResponseDTO>(`${this.BASE_URL}/${id}`);
  }

  getComprasPorPareja(idPareja: number): Observable<CompraResponseDTO[]> {
    return this.http.get<CompraResponseDTO[]>(`${this.BASE_URL}/pareja/${idPareja}`);
  }

  getComprasPorCliente(idCliente: number): Observable<CompraResponseDTO[]> {
    return this.http.get<CompraResponseDTO[]>(`${this.BASE_URL}/cliente/${idCliente}`);
  }

  registrarCompra(body: CompraRequestDTO): Observable<CompraResponseDTO> {
    return this.http.post<CompraResponseDTO>(this.BASE_URL, body);
  }
}
