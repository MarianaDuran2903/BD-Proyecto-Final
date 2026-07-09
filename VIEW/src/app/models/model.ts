export interface LoginRequestDTO {
  id_usuario: number;
  contrasenia: string;
}

export interface LoginResponseDTO {
  tipo: string;
  id_usuario: number;
  nombre_usuario: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  estado: string;
  cupo_total_autorizado?: number;
  cupo_asignado?: number;
  id_usuario_cliente?: number;
  id_almacen?: number;
  nombre_almacen?: string;
}

export interface ClienteRequestDTO {
  nombre_usuario: string;
  contrasenia: string;
  cedula: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  telefono?: string;
  cupo_total_autorizado: number;
}

export interface ClienteResponseDTO {
  id_usuario: number;
  nombre_usuario: string;
  cedula: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  telefono?: string;
  cupo_total_autorizado: number;
  estado: string;
}

export interface ParejaRequestDTO {
  nombre_usuario: string;
  contrasenia: string;
  cedula: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  cupo_asignado: number;
  id_usuario_cliente: number;
}

export interface ParejaResponseDTO {
  id_usuario: number;
  nombre_usuario: string;
  cedula: string;
  primer_nombre: string;
  segundo_nombre?: string;
  primer_apellido: string;
  segundo_apellido?: string;
  cupo_asignado: number;
  estado: string;
  id_usuario_cliente: number;
  nombre_cliente_titular: string;
}

export interface CompraRequestDTO {
  monto: number;
  fecha: string;
  hora: string;
  id_usuario_pareja: number;
  id_almacen: number;
}

export interface CompraResponseDTO {
  id_compra: number;
  monto: number;
  fecha: string;
  hora: string;
  requiere_sobrecupo: boolean;
  id_usuario_pareja: number;
  nombre_pareja_completo: string;
  id_almacen: number;
  nombre_almacen: string;
  id_usuario_supervisor?: number;
  nombre_supervisor_completo?: string;
}

export interface RestriccionHorarioDTO {
  id_restriccion?: number;
  motivo?: string;
  dia_bloqueo: string;
  hora_bloqueo_inicio: string;
  hora_bloqueo_fin: string;
  id_usuario_pareja: number;
}

export interface SolicitudSobrecupoRequestDTO {
  id_usuario_pareja: number;
  id_usuario_cliente: number;
  monto_solicitado: number;
  id_almacen: number;
}

export interface SolicitudSobrecupoResponseDTO {
  cod_solicitud: number;
  fecha: string;
  hora: string;
  monto_solicitado: number;
  monto_autorizado?: number;
  estado: string;
  id_compra?: number;
  id_usuario_cliente: number;
  nombre_cliente_completo: string;
  id_usuario_pareja: number;
  nombre_pareja_completo: string;
  id_usuario_supervisor?: number;
  nombre_supervisor_completo?: string;
  id_almacen: number;
  nombre_almacen: string;
}

export interface DecisionSolicitudDTO {
  decision: string;
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
