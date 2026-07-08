package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {

    ClienteResponseDTO crear(ClienteRequestDTO dto);

    ClienteResponseDTO obtenerPorId(Long idUsuario);

    List<ClienteResponseDTO> listarTodos();

    ClienteResponseDTO actualizar(Long idUsuario, ClienteRequestDTO dto);

    void eliminar(Long idUsuario);
}
