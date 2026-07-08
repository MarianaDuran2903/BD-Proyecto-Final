package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Almacen;
import co.edu.unbosque.proyecto.supermercado.modelo.Supervisor;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SupervisorRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SupervisorResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.AlmacenRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.SupervisorRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.SupervisorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SupervisorServiceImpl implements SupervisorService {

    private final SupervisorRepository supervisorRepository;
    private final AlmacenRepository almacenRepository;
    private final ModelMapper mm = new ModelMapper();

    public SupervisorServiceImpl(SupervisorRepository supervisorRepository, AlmacenRepository almacenRepository) {
        this.supervisorRepository = supervisorRepository;
        this.almacenRepository = almacenRepository;
    }

    @Override
    @Transactional
    public SupervisorResponseDTO crear(SupervisorRequestDTO dto) {
        Almacen almacen = almacenRepository.findById(dto.getIdAlmacen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el almacén con id " + dto.getIdAlmacen()));

        if (supervisorRepository.existsByCedula(dto.getCedula())) {
            throw new ReglaNegocioException("Ya existe un supervisor registrado con la cédula " + dto.getCedula());
        }

        Supervisor supervisor = mm.map(dto, Supervisor.class);
        supervisor.setIdUsuario(null);
        supervisor.setEstado("Activo");
        supervisor.setAlmacen(almacen);

        Supervisor guardado = supervisorRepository.save(supervisor);
        return mapearAResponseDTO(guardado);
    }

    @Override
    public SupervisorResponseDTO obtenerPorId(Long idUsuario) {
        return mapearAResponseDTO(buscarPorIdOLanzarError(idUsuario));
    }

    @Override
    public List<SupervisorResponseDTO> listarPorAlmacen(Long idAlmacen) {
        return supervisorRepository.findByAlmacen_IdAlmacen(idAlmacen).stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupervisorResponseDTO actualizar(Long idUsuario, SupervisorRequestDTO dto) {
        Supervisor supervisor = buscarPorIdOLanzarError(idUsuario);
        Almacen almacen = almacenRepository.findById(dto.getIdAlmacen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el almacén con id " + dto.getIdAlmacen()));

        supervisor.setNombreUsuario(dto.getNombreUsuario());
        supervisor.setContrasenia(dto.getContrasenia());
        supervisor.setCedula(dto.getCedula());
        supervisor.setCorreo(dto.getCorreo());
        supervisor.setTelefono(dto.getTelefono());
        supervisor.setPrimerNombre(dto.getPrimerNombre());
        supervisor.setSegundoNombre(dto.getSegundoNombre());
        supervisor.setPrimerApellido(dto.getPrimerApellido());
        supervisor.setSegundoApellido(dto.getSegundoApellido());
        supervisor.setAlmacen(almacen);

        return mapearAResponseDTO(supervisorRepository.save(supervisor));
    }

    @Override
    @Transactional
    public void eliminar(Long idUsuario) {
        supervisorRepository.delete(buscarPorIdOLanzarError(idUsuario));
    }

    private Supervisor buscarPorIdOLanzarError(Long idUsuario) {
        return supervisorRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un supervisor con id " + idUsuario));
    }

    private SupervisorResponseDTO mapearAResponseDTO(Supervisor supervisor) {
        SupervisorResponseDTO dto = mm.map(supervisor, SupervisorResponseDTO.class);
        dto.setIdAlmacen(supervisor.getAlmacen().getIdAlmacen());
        dto.setNombreAlmacen(supervisor.getAlmacen().getNombreAlmacen());
        return dto;
    }
}