import { AprobacionCupoInicialDTO, ClienteRegistroRequestDTO, ClienteRequestDTO, ClienteResponseDTO, EditarCupoPropioDTO } from './../models/model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';



@Injectable({ providedIn: 'root', })
export class ClienteService {

private readonly BASE_URL = 'http://localhost:8585/compra/api/clientes';

  constructor(private http: HttpClient) {}

  getClientes(): Observable<ClienteResponseDTO[]> {
    return this.http.get<ClienteResponseDTO[]>(this.BASE_URL);
  }

  getCliente(id: number): Observable<ClienteResponseDTO> {
    return this.http.get<ClienteResponseDTO>(`${this.BASE_URL}/${id}`);
  }

  crearCliente(body: ClienteRequestDTO): Observable<ClienteResponseDTO> {
    return this.http.post<ClienteResponseDTO>(this.BASE_URL, body);
  }

  registrarCliente(body: ClienteRegistroRequestDTO): Observable<ClienteResponseDTO> {
    return this.http.post<ClienteResponseDTO>(`${this.BASE_URL}/registro`, body);
  }

  getPendientes(): Observable<ClienteResponseDTO[]> {
    return this.http.get<ClienteResponseDTO[]>(`${this.BASE_URL}/pendientes`);
  }

  aprobarCupoInicial(id: number, body: AprobacionCupoInicialDTO): Observable<ClienteResponseDTO> {
    return this.http.put<ClienteResponseDTO>(`${this.BASE_URL}/${id}/aprobar-cupo-inicial`, body);
  }

  editarCupoPropio(id: number, body: EditarCupoPropioDTO): Observable<ClienteResponseDTO> {
    return this.http.put<ClienteResponseDTO>(`${this.BASE_URL}/${id}/cupo-propio`, body);
  }

  actualizarCliente(id: number, body: ClienteRequestDTO): Observable<ClienteResponseDTO> {
    return this.http.put<ClienteResponseDTO>(`${this.BASE_URL}/${id}`, body);
  }

  eliminarCliente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }
}
