import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DecisionSolicitudDTO, SolicitudSobrecupoRequestDTO, SolicitudSobrecupoResponseDTO } from '../models/model';

@Injectable({ providedIn: 'root' })
export class SobrecupoService {

  private readonly BASE_URL = 'http://localhost:8585/compra/api/solicitudes-sobrecupo';

  constructor(private http: HttpClient) {}

  getSolicitudes(): Observable<SolicitudSobrecupoResponseDTO[]> {
    return this.http.get<SolicitudSobrecupoResponseDTO[]>(this.BASE_URL);
  }

  getSolicitud(id: number): Observable<SolicitudSobrecupoResponseDTO> {
    return this.http.get<SolicitudSobrecupoResponseDTO>(`${this.BASE_URL}/${id}`);
  }

  getSolicitudesPorCliente(idCliente: number): Observable<SolicitudSobrecupoResponseDTO[]> {
    return this.http.get<SolicitudSobrecupoResponseDTO[]>(`${this.BASE_URL}/cliente/${idCliente}`);
  }

  getSolicitudesPorPareja(idPareja: number): Observable<SolicitudSobrecupoResponseDTO[]> {
    return this.http.get<SolicitudSobrecupoResponseDTO[]>(`${this.BASE_URL}/pareja/${idPareja}`);
  }

  getPendientesPorCliente(idCliente: number): Observable<SolicitudSobrecupoResponseDTO[]> {
    return this.http.get<SolicitudSobrecupoResponseDTO[]>(`${this.BASE_URL}/pendientes-cliente/${idCliente}`);
  }

  getPendientesPorSupervisor(idSupervisor: number): Observable<SolicitudSobrecupoResponseDTO[]> {
    return this.http.get<SolicitudSobrecupoResponseDTO[]>(`${this.BASE_URL}/pendientes-supervisor/${idSupervisor}`);
  }

  crearSolicitud(body: SolicitudSobrecupoRequestDTO): Observable<SolicitudSobrecupoResponseDTO> {
    return this.http.post<SolicitudSobrecupoResponseDTO>(this.BASE_URL, body);
  }

  decidirComoCliente(id: number, body: DecisionSolicitudDTO): Observable<SolicitudSobrecupoResponseDTO> {
    return this.http.put<SolicitudSobrecupoResponseDTO>(`${this.BASE_URL}/${id}/decision-cliente`, body);
  }

  decidirComoSupervisor(id: number, body: DecisionSolicitudDTO): Observable<SolicitudSobrecupoResponseDTO> {
    return this.http.put<SolicitudSobrecupoResponseDTO>(`${this.BASE_URL}/${id}/decision-supervisor`, body);
  }
}
