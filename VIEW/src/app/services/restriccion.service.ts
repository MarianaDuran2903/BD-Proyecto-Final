import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestriccionHorarioDTO } from '../models/model';

@Injectable({ providedIn: 'root' })
export class RestriccionService {

  private readonly BASE_URL = 'http://localhost:8585/compra/api/restricciones-horario';

  constructor(private http: HttpClient) {}

  getRestricciones(): Observable<RestriccionHorarioDTO[]> {
    return this.http.get<RestriccionHorarioDTO[]>(this.BASE_URL);
  }

  getRestriccion(id: number): Observable<RestriccionHorarioDTO> {
    return this.http.get<RestriccionHorarioDTO>(`${this.BASE_URL}/${id}`);
  }

  getRestriccionesPorPareja(idPareja: number): Observable<RestriccionHorarioDTO[]> {
    return this.http.get<RestriccionHorarioDTO[]>(`${this.BASE_URL}/pareja/${idPareja}`);
  }

  crearRestriccion(body: RestriccionHorarioDTO): Observable<RestriccionHorarioDTO> {
    return this.http.post<RestriccionHorarioDTO>(this.BASE_URL, body);
  }

  actualizarRestriccion(id: number, body: RestriccionHorarioDTO): Observable<RestriccionHorarioDTO> {
    return this.http.put<RestriccionHorarioDTO>(`${this.BASE_URL}/${id}`, body);
  }

  eliminarRestriccion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }
}
