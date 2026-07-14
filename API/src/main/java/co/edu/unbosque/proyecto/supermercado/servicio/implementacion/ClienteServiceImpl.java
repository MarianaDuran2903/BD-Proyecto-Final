package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Cliente;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.AprobacionCupoInicialDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteRegistroRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.EditarCupoPropioDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.ClienteRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ParejaRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.SupervisorRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ParejaRepository parejaRepository;
    private final SupervisorRepository supervisorRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository,
                              ParejaRepository parejaRepository,
                              SupervisorRepository supervisorRepository) {
        this.clienteRepository = clienteRepository;
        this.parejaRepository = parejaRepository;
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsById(dto.getIdUsuario())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con la cedula " + dto.getIdUsuario());
        }
        validarCedulaNoRegistradaEnOtroRol(dto.getIdUsuario());

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(dto.getIdUsuario());
        cliente.setNombreUsuario(dto.getNombreUsuario());
        cliente.setContrasenia(dto.getContrasenia());
        cliente.setEstado("Activo");
        cliente.setPrimerNombre(dto.getPrimerNombre());
        cliente.setSegundoNombre(dto.getSegundoNombre());
        cliente.setPrimerApellido(dto.getPrimerApellido());
        cliente.setSegundoApellido(dto.getSegundoApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCupoPropio(dto.getCupoPropio());
        cliente.setCupoTotalAutorizado(dto.getCupoPropio());

        return toResponseDTO(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public ClienteResponseDTO registrar(ClienteRegistroRequestDTO dto) {
        if (clienteRepository.existsById(dto.getIdUsuario())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con la cedula " + dto.getIdUsuario());
        }
        validarCedulaNoRegistradaEnOtroRol(dto.getIdUsuario());

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(dto.getIdUsuario());
        cliente.setNombreUsuario(dto.getNombreUsuario());
        cliente.setContrasenia(dto.getContrasenia());
        cliente.setEstado("Pendiente");
        cliente.setPrimerNombre(dto.getPrimerNombre());
        cliente.setSegundoNombre(dto.getSegundoNombre());
        cliente.setPrimerApellido(dto.getPrimerApellido());
        cliente.setSegundoApellido(dto.getSegundoApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCupoPropio(BigDecimal.ZERO);
        cliente.setCupoTotalAutorizado(BigDecimal.ZERO);
        cliente.setCupoTotalSolicitado(dto.getCupoTotalSolicitado());

        return toResponseDTO(clienteRepository.save(cliente));
    }

    @Override
    public ClienteResponseDTO obtenerPorId(Long idUsuario) {
        return toResponseDTO(buscarOLanzarError(idUsuario));
    }

    @Override
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponseDTO> listarPendientes() {
        return clienteRepository.findByEstado("Pendiente").stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClienteResponseDTO actualizar(Long idUsuario, ClienteRequestDTO dto) {
        Cliente cliente = buscarOLanzarError(idUsuario);

        cliente.setNombreUsuario(dto.getNombreUsuario());
        cliente.setContrasenia(dto.getContrasenia());
        cliente.setPrimerNombre(dto.getPrimerNombre());
        cliente.setSegundoNombre(dto.getSegundoNombre());
        cliente.setPrimerApellido(dto.getPrimerApellido());
        cliente.setSegundoApellido(dto.getSegundoApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCupoPropio(dto.getCupoPropio());

        return toResponseDTO(clienteRepository.update(cliente));
    }

    @Override
    @Transactional
    public ClienteResponseDTO aprobarCupoInicial(Long idUsuario, AprobacionCupoInicialDTO dto) {
        Cliente cliente = buscarOLanzarError(idUsuario);

        if (!"Pendiente".equals(cliente.getEstado())) {
            throw new ReglaNegocioException(
                    "Solo se puede aprobar el cupo inicial de clientes en estado 'Pendiente'. Estado actual: "
                            + cliente.getEstado());
        }

        cliente.setCupoPropio(dto.getCupoAutorizado());
        cliente.setCupoTotalAutorizado(dto.getCupoAutorizado());
        cliente.setEstado("Activo");
        clienteRepository.update(cliente);

        return toResponseDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO editarCupoPropio(Long idUsuario, EditarCupoPropioDTO dto) {
        Cliente cliente = buscarOLanzarError(idUsuario);

        if ("Pendiente".equals(cliente.getEstado())) {
            throw new ReglaNegocioException(
                    "El cliente aun no tiene un cupo inicial aprobado por el Supervisor.");
        }

        BigDecimal sumaAsignada = parejaRepository.sumarCupoAsignadoPorCliente(cliente.getIdUsuario());
        BigDecimal maximoPermitido = cliente.getCupoTotalAutorizado().subtract(sumaAsignada);
        if (dto.getCupoPropio().compareTo(maximoPermitido) > 0) {
            throw new ReglaNegocioException(
                    "El cupo propio no puede superar " + maximoPermitido
                            + " (cupo total autorizado menos lo ya asignado a tus parejas).");
        }

        cliente.setCupoPropio(dto.getCupoPropio());
        return toResponseDTO(clienteRepository.update(cliente));
    }

    @Override
    @Transactional
    public void eliminar(Long idUsuario) {
        buscarOLanzarError(idUsuario);
        clienteRepository.deleteById(idUsuario);
    }

    private void validarCedulaNoRegistradaEnOtroRol(Long idUsuario) {
        if (parejaRepository.existsById(idUsuario)) {
            throw new ReglaNegocioException(
                    "La cedula " + idUsuario + " ya esta registrada como Pareja");
        }
        if (supervisorRepository.existsById(idUsuario)) {
            throw new ReglaNegocioException(
                    "La cedula " + idUsuario + " ya esta registrada como Supervisor");
        }
    }

    private Cliente buscarOLanzarError(Long idUsuario) {
        return clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro un cliente con id " + idUsuario));
    }

    private ClienteResponseDTO toResponseDTO(Cliente c) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setIdUsuario(c.getIdUsuario());
        dto.setNombreUsuario(c.getNombreUsuario());
        dto.setPrimerNombre(c.getPrimerNombre());
        dto.setSegundoNombre(c.getSegundoNombre());
        dto.setPrimerApellido(c.getPrimerApellido());
        dto.setSegundoApellido(c.getSegundoApellido());
        dto.setTelefono(c.getTelefono());
        dto.setCupoPropio(c.getCupoPropio());

        BigDecimal sumaAsignada = parejaRepository.sumarCupoAsignadoPorCliente(c.getIdUsuario());
        dto.setCupoTotalAutorizado(c.getCupoTotalAutorizado());
        dto.setCupoTotalSolicitado(c.getCupoTotalSolicitado());
        dto.setCupoAsignadoParejas(sumaAsignada);
        dto.setCupoTotalDisponible(
                c.getCupoTotalAutorizado().subtract(c.getCupoPropio()).subtract(sumaAsignada));

        dto.setEstado(c.getEstado());
        return dto;
    }
}
