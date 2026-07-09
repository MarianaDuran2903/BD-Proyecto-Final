package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.RestriccionHorario;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.RestriccionHorarioDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.ParejaRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.RestriccionHorarioRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.RestriccionHorarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestriccionHorarioServiceImpl implements RestriccionHorarioService {

    private final RestriccionHorarioRepository restriccionRepository;
    private final ParejaRepository parejaRepository;

    public RestriccionHorarioServiceImpl(RestriccionHorarioRepository restriccionRepository,
                                         ParejaRepository parejaRepository) {
        this.restriccionRepository = restriccionRepository;
        this.parejaRepository = parejaRepository;
    }

    @Override
    @Transactional
    public RestriccionHorarioDTO crear(RestriccionHorarioDTO dto) {
        if (!parejaRepository.existsById(dto.getIdUsuarioPareja())) {
            throw new RecursoNoEncontradoException(
                    "No se encontro la pareja con id " + dto.getIdUsuarioPareja());
        }
        if (!dto.getHoraBloqueoFin().isAfter(dto.getHoraBloqueoInicio())) {
            throw new ReglaNegocioException(
                    "La hora de fin del bloqueo debe ser posterior a la hora de inicio");
        }

        RestriccionHorario restriccion = new RestriccionHorario();
        restriccion.setMotivo(dto.getMotivo());
        restriccion.setDiaBloqueo(dto.getDiaBloqueo());
        restriccion.setHoraBloqueoInicio(dto.getHoraBloqueoInicio());
        restriccion.setHoraBloqueoFin(dto.getHoraBloqueoFin());
        restriccion.setIdUsuarioPareja(dto.getIdUsuarioPareja());

        return toDTO(restriccionRepository.save(restriccion));
    }

    @Override
    public RestriccionHorarioDTO obtenerPorId(Long idRestriccion) {
        return toDTO(buscarOLanzarError(idRestriccion));
    }

    @Override
    public List<RestriccionHorarioDTO> listarTodos() {
        return restriccionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestriccionHorarioDTO> listarPorPareja(Long idUsuarioPareja) {
        return restriccionRepository.findByIdUsuarioPareja(idUsuarioPareja).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RestriccionHorarioDTO actualizar(Long idRestriccion, RestriccionHorarioDTO dto) {
        RestriccionHorario restriccion = buscarOLanzarError(idRestriccion);

        if (!dto.getHoraBloqueoFin().isAfter(dto.getHoraBloqueoInicio())) {
            throw new ReglaNegocioException(
                    "La hora de fin del bloqueo debe ser posterior a la hora de inicio");
        }

        restriccion.setMotivo(dto.getMotivo());
        restriccion.setDiaBloqueo(dto.getDiaBloqueo());
        restriccion.setHoraBloqueoInicio(dto.getHoraBloqueoInicio());
        restriccion.setHoraBloqueoFin(dto.getHoraBloqueoFin());
        restriccion.setIdUsuarioPareja(dto.getIdUsuarioPareja());

        return toDTO(restriccionRepository.update(restriccion));
    }

    @Override
    @Transactional
    public void eliminar(Long idRestriccion) {
        buscarOLanzarError(idRestriccion);
        restriccionRepository.deleteById(idRestriccion);
    }

    private RestriccionHorario buscarOLanzarError(Long idRestriccion) {
        return restriccionRepository.findById(idRestriccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la restriccion con id " + idRestriccion));
    }

    private RestriccionHorarioDTO toDTO(RestriccionHorario r) {
        RestriccionHorarioDTO dto = new RestriccionHorarioDTO();
        dto.setIdRestriccion(r.getIdRestriccion());
        dto.setMotivo(r.getMotivo());
        dto.setDiaBloqueo(r.getDiaBloqueo());
        dto.setHoraBloqueoInicio(r.getHoraBloqueoInicio());
        dto.setHoraBloqueoFin(r.getHoraBloqueoFin());
        dto.setIdUsuarioPareja(r.getIdUsuarioPareja());
        return dto;
    }
}
