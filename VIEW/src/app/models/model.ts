export interface LoginRequestDTO {
  id_usuario: number;
  contrasenia: string;
}

export interface LoginResponseDTO {
  tipo: string; // Depende si es CLIENTE, PAREJA o SUPERVISOR
  id_usuario: number;
  nombre_usuario: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  estado: string;
  // Solo CLIENTE
  cupo_propio?: number;
  cupo_total_autorizado?: number; // calculado: cupo_propio + suma de cupo_asignado de sus Parejas
  // Solo PAREJA
  cupo_asignado?: number;
  id_usuario_cliente?: number;
  // Solo SUPERVISOR
  id_almacen?: number;
  nombre_almacen?: string;
}

export interface ClienteRequestDTO {
  id_usuario: number;
  nombre_usuario: string;
  contrasenia: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  telefono?: string;
  cupo_propio: number;
}

export interface ClienteResponseDTO {
  id_usuario: number;
  nombre_usuario: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  telefono?: string;
  cupo_propio: number;
  cupo_total_autorizado: number;
  estado: string;
}

export interface ParejaRequestDTO {
  id_usuario: number;
  nombre_usuario: string;
  contrasenia: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  telefono?: string;
  cupo_asignado: number;
  id_usuario_cliente: number;
}

export interface ParejaResponseDTO {
  id_usuario: number;
  nombre_usuario: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  telefono?: string;
  cupo_asignado: number;
  estado: string;
  id_usuario_cliente: number;
  nombre_cliente_titular: string;
}

export interface CompraRequestDTO {
  monto: number;
  fecha: string;
  hora: string;
   id_usuario_pareja?: number;
  id_usuario_cliente?: number;
  id_almacen: number;
  id_usuario_supervisor: number;
}

export interface CompraResponseDTO {
  id_compra: number;
  monto: number;
  fecha: string;
  hora: string;
  id_usuario_pareja?: number;
  nombre_pareja_completo?: string;
  id_usuario_cliente?: number;
  nombre_cliente_completo?: string;
  id_almacen: number;
  nombre_almacen: string;
  id_usuario_supervisor: number;
  nombre_supervisor_completo: string;
}

export interface RestriccionHorarioDTO {
  id_restriccion?: number;
  motivo?: string;
  dia_bloqueo: string;
  hora_bloqueo_inicio: string;
  hora_bloqueo_fin: string;
  id_usuario_pareja: number;
}

export type EstadoSolicitudSobrecupo =
  | 'pendiente_cliente'
  | 'aprobada_directa'
  | 'pendiente_supervisor'
  | 'aprobada_supervisor'
  | 'rechazada_cliente'
  | 'rechazada_supervisor';

export interface SolicitudSobrecupoRequestDTO {
  id_usuario_pareja: number;
  id_usuario_cliente: number;
  monto_solicitado: number;
}

export interface SolicitudSobrecupoResponseDTO {
  cod_solicitud: number;
  fecha: string;
  hora: string;
  monto_solicitado: number;
  monto_autorizado?: number;
  estado: EstadoSolicitudSobrecupo;
  id_usuario_cliente: number;
  nombre_cliente_completo: string;
  id_usuario_pareja: number;
  nombre_pareja_completo: string;
  id_usuario_supervisor?: number;
  nombre_supervisor_completo?: string;
}

export interface DecisionSolicitudDTO {
  decision: 'Aprobar'
  | 'Rechazar';
}

export interface AlmacenDTO {
  id_almacen?: number;
  nombre_almacen: string;
  ubicacion_ciudad: string;
  ubicacion_avenida?: string;
  ubicacion_calle?: string;
}

export interface SupervisorResponseDTO {
  id_usuario: number;
  nombre_usuario: string;
  correo?: string;
  telefono?: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  estado: string;
  id_almacen: number;
  nombre_almacen: string;
}
