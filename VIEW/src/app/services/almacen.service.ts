import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AlmacenDTO } from '../models/model';

@Injectable({ providedIn: 'root' })
export class AlmacenService {
  private readonly BASE_URL = 'http://localhost:8585/compra/api/almacenes';

  constructor(private http: HttpClient) {}

  getAlmacenes(): Observable<AlmacenDTO[]> {
    return this.http.get<AlmacenDTO[]>(this.BASE_URL);
  }

  getAlmacen(id: number): Observable<AlmacenDTO> {
    return this.http.get<AlmacenDTO>(`${this.BASE_URL}/${id}`);
  }
}
