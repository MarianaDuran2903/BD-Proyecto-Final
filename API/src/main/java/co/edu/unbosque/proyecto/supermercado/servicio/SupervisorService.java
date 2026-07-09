package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.SupervisorRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SupervisorResponseDTO;

import java.util.List;

public interface SupervisorService {

    SupervisorResponseDTO crear(SupervisorRequestDTO dto);

    SupervisorResponseDTO obtenerPorId(Long idUsuario);

    List<SupervisorResponseDTO> listarTodos();

    List<SupervisorResponseDTO> listarPorAlmacen(Long idAlmacen);

    SupervisorResponseDTO actualizar(Long idUsuario, SupervisorRequestDTO dto);

    void eliminar(Long idUsuario);
}
