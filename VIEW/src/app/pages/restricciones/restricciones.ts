import { Component, OnInit, signal } from '@angular/core';
import { DisenoAdmin } from '../../components/templates/diseno-admin/diseno-admin';
import { CampoFormulario } from '../../components/molecules/campo-formulario/campo-formulario';
import { Boton } from '../../components/atoms/boton/boton';
import { RestriccionService } from '../../services/restriccion.service';
import { ParejaService } from '../../services/pareja.service';
import { RestriccionHorarioDTO, ParejaResponseDTO } from '../../models/model';

@Component({
  selector: 'app-restricciones',
  imports: [DisenoAdmin, CampoFormulario, Boton],
  templateUrl: './restricciones.html',
  styleUrl: './restricciones.css',
})
export class Restricciones implements OnInit {
  restricciones = signal<RestriccionHorarioDTO[]>([]);
  parejas = signal<ParejaResponseDTO[]>([]);
  formularioVisible = signal(false);

  form: RestriccionHorarioDTO = {
    dia_bloqueo: '',
    hora_bloqueo_inicio: '',
    hora_bloqueo_fin: '',
    id_usuario_pareja: 0
  };

  dias = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];

  constructor(
    private restriccionService: RestriccionService,
    private parejaService: ParejaService
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.parejaService.getParejas().subscribe(data => this.parejas.set(data));
  }

  cargar(): void {
    this.restriccionService.getRestricciones().subscribe(data => this.restricciones.set(data));
  }

  abrirCrear(): void {
    this.form = {
      dia_bloqueo: '',
      hora_bloqueo_inicio: '',
      hora_bloqueo_fin: '',
      id_usuario_pareja: 0
    };
    this.formularioVisible.set(true);
  }

  guardar(): void {
    this.restriccionService.crearRestriccion(this.form).subscribe({
      next: () => { this.formularioVisible.set(false); this.cargar(); },
      error: (e) => console.error('Error al guardar restricción.', e)
    });
  }

  eliminar(id: number): void {
    if (confirm('¿Eliminar esta restricción?')) {
      this.restriccionService.eliminarRestriccion(id).subscribe({
        next: () => this.cargar(),
        error: () => console.error('Error al eliminar restricción.')
      });
    }
  }
}
