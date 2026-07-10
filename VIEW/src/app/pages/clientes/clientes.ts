import { Component, OnInit, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { CampoFormulario } from '../../components/molecules/campo-formulario/campo-formulario';
import { Boton } from '../../components/atoms/boton/boton';
import { ClienteService } from '../../services/cliente.service';
import { ClienteRequestDTO, ClienteResponseDTO } from '../../models/model';

@Component({
  selector: 'app-clientes',
  imports: [DisenoAdmin, CampoFormulario, Boton],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css',
})
export class Clientes implements OnInit {
  clientes = signal<ClienteResponseDTO[]>([]);
  formularioVisible = signal(false);
  editando = signal<ClienteResponseDTO | null>(null);

  form: ClienteRequestDTO = {
    nombre_usuario: '',
    contrasenia: '',
    cedula: '',
    primer_nombre: '',
    segundo_nombre: '',
    primer_apellido: '',
    segundo_apellido: '',
    telefono: '',
    cupo_total_autorizado: 0
  };

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void { this.cargar(); }

  cargar(): void {
    this.clienteService.getClientes().subscribe((data) => this.clientes.set(data));
  }

  abrirCrear(): void {
    this.editando.set(null);
    this.form = {
      nombre_usuario: '',
      contrasenia: '',
      cedula: '',
      primer_nombre: '',
      segundo_nombre: '',
      primer_apellido: '',
      segundo_apellido: '',
      telefono: '',
      cupo_total_autorizado: 0
    };
    this.formularioVisible.set(true);
  }

  abrirEditar(c: ClienteResponseDTO): void {
    this.editando.set(c);
    this.form = {
      nombre_usuario: c.nombre_usuario,
      contrasenia: '',
      cedula: String(c.id_usuario),
      primer_nombre: c.primer_nombre,
      segundo_nombre: c.segundo_nombre ?? '',
      primer_apellido: c.primer_apellido,
      segundo_apellido: c.segundo_apellido ?? '',
      telefono: c.telefono ?? '',
      cupo_total_autorizado: c.cupo_total_autorizado
    };
    this.formularioVisible.set(true);
  }

  guardar(): void {
    const editando = this.editando();
    if (editando) {
      this.clienteService.actualizarCliente(editando.id_usuario, this.form).subscribe({
        next: () => { this.formularioVisible.set(false); this.cargar(); },
        error: (e) => console.error(e.status === 409 ? 'Ya existe un cliente con ese documento.' : 'Error al actualizar.')
      });
    } else {
      this.clienteService.crearCliente(this.form).subscribe({
        next: () => { this.formularioVisible.set(false); this.cargar(); },
        error: (e) => console.error(e.status === 409 ? 'Ya existe un cliente con ese documento.' : 'Error al crear.')
      });
    }
  }
}
