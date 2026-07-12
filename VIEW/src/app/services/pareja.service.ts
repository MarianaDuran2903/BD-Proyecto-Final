import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ParejaRequestDTO, ParejaResponseDTO } from '../models/model';

@Injectable({ providedIn: 'root' })
export class ParejaService {

  private readonly BASE_URL = 'http://localhost:8585/compra/api/parejas';

  constructor(private http: HttpClient) {}

  getParejas(): Observable<ParejaResponseDTO[]> {
    return this.http.get<ParejaResponseDTO[]>(this.BASE_URL);
  }

  getPareja(id: number): Observable<ParejaResponseDTO> {
    return this.http.get<ParejaResponseDTO>(`${this.BASE_URL}/${id}`);
  }

  getParejasPorCliente(idCliente: number): Observable<ParejaResponseDTO[]> {
    return this.http.get<ParejaResponseDTO[]>(`${this.BASE_URL}/cliente/${idCliente}`);
  }

  crearPareja(body: ParejaRequestDTO): Observable<ParejaResponseDTO> {
    return this.http.post<ParejaResponseDTO>(this.BASE_URL, body);
  }

  actualizarPareja(id: number, body: ParejaRequestDTO): Observable<ParejaResponseDTO> {
    return this.http.put<ParejaResponseDTO>(`${this.BASE_URL}/${id}`, body);
  }

  inactivarPareja(id: number): Observable<ParejaResponseDTO> {
    return this.http.put<ParejaResponseDTO>(`${this.BASE_URL}/${id}/inactivar`, {});
  }

  activarPareja(id: number): Observable<ParejaResponseDTO> {
    return this.http.put<ParejaResponseDTO>(`${this.BASE_URL}/${id}/activar`, {});
  }

  eliminarPareja(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }
}
