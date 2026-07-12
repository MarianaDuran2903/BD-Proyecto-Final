package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.AprobacionCupoInicialDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteRegistroRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.EditarCupoPropioDTO;

import java.util.List;

public interface ClienteService {

    ClienteResponseDTO crear(ClienteRequestDTO dto);

    ClienteResponseDTO registrar(ClienteRegistroRequestDTO dto);

    ClienteResponseDTO obtenerPorId(Long idUsuario);

    List<ClienteResponseDTO> listarTodos();

    List<ClienteResponseDTO> listarPendientes();

    ClienteResponseDTO actualizar(Long idUsuario, ClienteRequestDTO dto);

    ClienteResponseDTO aprobarCupoInicial(Long idUsuario, AprobacionCupoInicialDTO dto);

    ClienteResponseDTO editarCupoPropio(Long idUsuario, EditarCupoPropioDTO dto);

    void eliminar(Long idUsuario);
}
