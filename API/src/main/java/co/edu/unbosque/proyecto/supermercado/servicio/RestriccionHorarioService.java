package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.RestriccionHorarioDTO;

import java.util.List;

public interface RestriccionHorarioService {

    RestriccionHorarioDTO crear(RestriccionHorarioDTO dto);

    RestriccionHorarioDTO obtenerPorId(Long idRestriccion);

    List<RestriccionHorarioDTO> listarTodos();

    List<RestriccionHorarioDTO> listarPorPareja(Long idUsuarioPareja);

    RestriccionHorarioDTO actualizar(Long idRestriccion, RestriccionHorarioDTO dto);

    void eliminar(Long idRestriccion);
}
