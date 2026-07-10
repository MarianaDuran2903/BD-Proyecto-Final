import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequestDTO, LoginResponseDTO } from '../models/model';


@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly BASE_URL = 'http://localhost:8585/compra/api/auth';

  constructor(private http: HttpClient) { }

  login(body: LoginRequestDTO): Observable<LoginResponseDTO> {
    return this.http.post<LoginResponseDTO>(`${this.BASE_URL}/login`, body);
  }
}



