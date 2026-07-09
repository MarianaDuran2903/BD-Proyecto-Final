package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.DecisionSolicitudDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SolicitudSobrecupoRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SolicitudSobrecupoResponseDTO;

import java.util.List;

public interface SolicitudSobrecupoService {

    SolicitudSobrecupoResponseDTO crear(SolicitudSobrecupoRequestDTO dto);

    SolicitudSobrecupoResponseDTO obtenerPorId(Long codSolicitud);

    List<SolicitudSobrecupoResponseDTO> listarTodos();

    List<SolicitudSobrecupoResponseDTO> listarPorCliente(Long idUsuarioCliente);

    List<SolicitudSobrecupoResponseDTO> listarPorPareja(Long idUsuarioPareja);

    List<SolicitudSobrecupoResponseDTO> listarPendientesPorCliente(Long idUsuarioCliente);

    List<SolicitudSobrecupoResponseDTO> listarPendientesPorSupervisor(Long idUsuarioSupervisor);

    SolicitudSobrecupoResponseDTO decidirComoCliente(Long codSolicitud, DecisionSolicitudDTO dto);

    SolicitudSobrecupoResponseDTO decidirComoSupervisor(Long codSolicitud, DecisionSolicitudDTO dto);
}
