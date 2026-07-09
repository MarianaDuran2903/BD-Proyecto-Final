package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.ParejaRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ParejaResponseDTO;

import java.util.List;

public interface ParejaService {

    ParejaResponseDTO crear(ParejaRequestDTO dto);

    ParejaResponseDTO obtenerPorId(Long idUsuario);

    List<ParejaResponseDTO> listarTodos();

    List<ParejaResponseDTO> listarPorCliente(Long idUsuarioCliente);

    ParejaResponseDTO actualizar(Long idUsuario, ParejaRequestDTO dto);

    void eliminar(Long idUsuario);
}
