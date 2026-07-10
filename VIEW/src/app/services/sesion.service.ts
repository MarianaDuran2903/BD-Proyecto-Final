import { Injectable, signal } from '@angular/core';
import { LoginResponseDTO } from '../models/model';

@Injectable({providedIn: 'root'})
export class SesionService {
  private readonly _usuario = signal<LoginResponseDTO | null>(null);

  readonly usuario = this._usuario.asReadonly();

  setUsuario(usuario:LoginResponseDTO): void {
    this._usuario.set(usuario);
  }

  cerrarSesion(): void {
    this._usuario.set(null);
  }

  isAutenticado(): boolean {
    return this._usuario() !== null;
  }

  obtenerRol(): string | null {
    return this._usuario()?.tipo || null;
  }

}
