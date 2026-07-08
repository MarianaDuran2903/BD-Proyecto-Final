package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Pareja;
import co.edu.unbosque.proyecto.supermercado.modelo.RestriccionHorario;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.RestriccionHorarioDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.ParejaRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.RestriccionHorarioRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.RestriccionHorarioService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestriccionHorarioServiceImpl implements RestriccionHorarioService {

    private final RestriccionHorarioRepository restriccionHorarioRepository;
    private final ParejaRepository parejaRepository;
    private final ModelMapper mm = new ModelMapper();

    public RestriccionHorarioServiceImpl(RestriccionHorarioRepository restriccionHorarioRepository,
                                         ParejaRepository parejaRepository) {
        this.restriccionHorarioRepository = restriccionHorarioRepository;
        this.parejaRepository = parejaRepository;
    }

    @Override
    @Transactional
    public RestriccionHorarioDTO crear(RestriccionHorarioDTO dto) {
        Pareja pareja = parejaRepository.findById(dto.getIdUsuarioPareja())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la pareja con id " + dto.getIdUsuarioPareja()));

        if (!dto.getHoraBloqueoFin().isAfter(dto.getHoraBloqueoInicio())) {
            throw new ReglaNegocioException("La hora de fin del bloqueo debe ser posterior a la hora de inicio");
        }

        RestriccionHorario restriccion = mm.map(dto, RestriccionHorario.class);
        restriccion.setIdRestriccion(null);
        restriccion.setPareja(pareja);

        RestriccionHorario guardada = restriccionHorarioRepository.save(restriccion);
        return mapearADTO(guardada);
    }

    @Override
    public List<RestriccionHorarioDTO> listarPorPareja(Long idUsuarioPareja) {
        return restriccionHorarioRepository.findByPareja_IdUsuario(idUsuarioPareja).stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long idRestriccion) {
        RestriccionHorario restriccion = restriccionHorarioRepository.findById(idRestriccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la restricción con id " + idRestriccion));
        restriccionHorarioRepository.delete(restriccion);
    }

    private RestriccionHorarioDTO mapearADTO(RestriccionHorario restriccion) {
        RestriccionHorarioDTO dto = mm.map(restriccion, RestriccionHorarioDTO.class);
        dto.setIdUsuarioPareja(restriccion.getPareja().getIdUsuario());
        return dto;
    }
}
