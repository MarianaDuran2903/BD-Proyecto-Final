import { ClienteRequestDTO, ClienteResponseDTO } from './../models/model';
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

  actualizarCliente(id: number, body: ClienteRequestDTO): Observable<ClienteResponseDTO> {
    return this.http.put<ClienteResponseDTO>(`${this.BASE_URL}/${id}`, body);
  }

  eliminarCliente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }
}
