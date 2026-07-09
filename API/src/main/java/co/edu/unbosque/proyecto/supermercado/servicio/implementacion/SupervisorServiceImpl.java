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
import co.edu.unbosque.proyecto.supermercado.repositorio.ClienteRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ParejaRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.SupervisorRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.SupervisorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    private final SupervisorRepository supervisorRepository;
    private final AlmacenRepository almacenRepository;
    private final ClienteRepository clienteRepository;
    private final ParejaRepository parejaRepository;

    public SupervisorServiceImpl(SupervisorRepository supervisorRepository,
                                 AlmacenRepository almacenRepository,
                                 ClienteRepository clienteRepository,
                                 ParejaRepository parejaRepository) {
        this.supervisorRepository = supervisorRepository;
        this.almacenRepository = almacenRepository;
        this.clienteRepository = clienteRepository;
        this.parejaRepository = parejaRepository;
    }

    @Override
    @Transactional
    public SupervisorResponseDTO crear(SupervisorRequestDTO dto) {
        if (supervisorRepository.existsById(dto.getIdUsuario())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con la cedula " + dto.getIdUsuario());
        }
        validarCedulaNoRegistradaEnOtroRol(dto.getIdUsuario());

        Almacen almacen = buscarAlmacenOLanzarError(dto.getIdAlmacen());

        Supervisor supervisor = new Supervisor();
        supervisor.setIdUsuario(dto.getIdUsuario());
        supervisor.setNombreUsuario(dto.getNombreUsuario());
        supervisor.setContrasenia(dto.getContrasenia());
        supervisor.setEstado("Activo");
        supervisor.setCorreo(dto.getCorreo());
        supervisor.setTelefono(dto.getTelefono());
        supervisor.setPrimerNombre(dto.getPrimerNombre());
        supervisor.setSegundoNombre(dto.getSegundoNombre());
        supervisor.setPrimerApellido(dto.getPrimerApellido());
        supervisor.setSegundoApellido(dto.getSegundoApellido());
        supervisor.setIdAlmacen(almacen.getIdAlmacen());

        return toResponseDTO(supervisorRepository.save(supervisor), almacen);
    }

    @Override
    public SupervisorResponseDTO obtenerPorId(Long idUsuario) {
        Supervisor supervisor = buscarOLanzarError(idUsuario);
        Almacen almacen = buscarAlmacenOLanzarError(supervisor.getIdAlmacen());
        return toResponseDTO(supervisor, almacen);
    }

    @Override
    public List<SupervisorResponseDTO> listarTodos() {
        return supervisorRepository.findAll().stream()
                .map(s -> {
                    Almacen almacen = buscarAlmacenOLanzarError(s.getIdAlmacen());
                    return toResponseDTO(s, almacen);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SupervisorResponseDTO> listarPorAlmacen(Long idAlmacen) {
        Almacen almacen = buscarAlmacenOLanzarError(idAlmacen);
        return supervisorRepository.findByIdAlmacen(idAlmacen).stream()
                .map(s -> toResponseDTO(s, almacen))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupervisorResponseDTO actualizar(Long idUsuario, SupervisorRequestDTO dto) {
        Supervisor supervisor = buscarOLanzarError(idUsuario);
        Almacen almacen = buscarAlmacenOLanzarError(dto.getIdAlmacen());

        supervisor.setNombreUsuario(dto.getNombreUsuario());
        supervisor.setContrasenia(dto.getContrasenia());
        supervisor.setCorreo(dto.getCorreo());
        supervisor.setTelefono(dto.getTelefono());
        supervisor.setPrimerNombre(dto.getPrimerNombre());
        supervisor.setSegundoNombre(dto.getSegundoNombre());
        supervisor.setPrimerApellido(dto.getPrimerApellido());
        supervisor.setSegundoApellido(dto.getSegundoApellido());
        supervisor.setIdAlmacen(almacen.getIdAlmacen());

        return toResponseDTO(supervisorRepository.update(supervisor), almacen);
    }

    @Override
    @Transactional
    public void eliminar(Long idUsuario) {
        buscarOLanzarError(idUsuario);
        supervisorRepository.deleteById(idUsuario);
    }

    // Una misma cedula (id_usuario) no puede existir simultaneamente en mas de una tabla de usuario
    private void validarCedulaNoRegistradaEnOtroRol(Long idUsuario) {
        if (clienteRepository.existsById(idUsuario)) {
            throw new ReglaNegocioException(
                    "La cedula " + idUsuario + " ya esta registrada como Cliente");
        }
        if (parejaRepository.existsById(idUsuario)) {
            throw new ReglaNegocioException(
                    "La cedula " + idUsuario + " ya esta registrada como Pareja");
        }
    }

    private Supervisor buscarOLanzarError(Long idUsuario) {
        return supervisorRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro un supervisor con id " + idUsuario));
    }

    private Almacen buscarAlmacenOLanzarError(Long idAlmacen) {
        return almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el almacen con id " + idAlmacen));
    }

    private SupervisorResponseDTO toResponseDTO(Supervisor s, Almacen almacen) {
        SupervisorResponseDTO dto = new SupervisorResponseDTO();
        dto.setIdUsuario(s.getIdUsuario());
        dto.setNombreUsuario(s.getNombreUsuario());
        dto.setCorreo(s.getCorreo());
        dto.setTelefono(s.getTelefono());
        dto.setPrimerNombre(s.getPrimerNombre());
        dto.setSegundoNombre(s.getSegundoNombre());
        dto.setPrimerApellido(s.getPrimerApellido());
        dto.setSegundoApellido(s.getSegundoApellido());
        dto.setEstado(s.getEstado());
        dto.setIdAlmacen(almacen.getIdAlmacen());
        dto.setNombreAlmacen(almacen.getNombreAlmacen());
        return dto;
    }
}
