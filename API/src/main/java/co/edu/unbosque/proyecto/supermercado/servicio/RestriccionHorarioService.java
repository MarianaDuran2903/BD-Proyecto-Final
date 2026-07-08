package co.edu.unbosque.proyecto.supermercado.servicio;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.RestriccionHorarioDTO;

import java.util.List;

public interface RestriccionHorarioService {

    RestriccionHorarioDTO crear(RestriccionHorarioDTO dto);

    List<RestriccionHorarioDTO> listarPorPareja(Long idUsuarioPareja);

    void eliminar(Long idRestriccion);
}
