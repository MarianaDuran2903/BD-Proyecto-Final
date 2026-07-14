package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Cliente;
import co.edu.unbosque.proyecto.supermercado.modelo.Pareja;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ParejaRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ParejaResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.ClienteRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ParejaRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.SupervisorRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.ParejaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParejaServiceImpl implements ParejaService {

    private final ParejaRepository parejaRepository;
    private final ClienteRepository clienteRepository;
    private final SupervisorRepository supervisorRepository;

    public ParejaServiceImpl(ParejaRepository parejaRepository, ClienteRepository clienteRepository,
                             SupervisorRepository supervisorRepository) {
        this.parejaRepository = parejaRepository;
        this.clienteRepository = clienteRepository;
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    @Transactional
    public ParejaResponseDTO crear(ParejaRequestDTO dto) {
        if (parejaRepository.existsById(dto.getIdUsuario())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con la cedula " + dto.getIdUsuario());
        }
        validarCedulaNoRegistradaEnOtroRol(dto.getIdUsuario());

        Cliente cliente = buscarClienteOLanzarError(dto.getIdUsuarioCliente());
        transferirCupoPropio(cliente, dto.getCupoAsignado());

        Pareja pareja = new Pareja();
        pareja.setIdUsuario(dto.getIdUsuario());
        pareja.setNombreUsuario(dto.getNombreUsuario());
        pareja.setContrasenia(dto.getContrasenia());
        pareja.setEstado("Activo");
        pareja.setPrimerNombre(dto.getPrimerNombre());
        pareja.setSegundoNombre(dto.getSegundoNombre());
        pareja.setPrimerApellido(dto.getPrimerApellido());
        pareja.setSegundoApellido(dto.getSegundoApellido());
        pareja.setTelefono(dto.getTelefono());
        pareja.setCupoAsignado(dto.getCupoAsignado());
        pareja.setIdUsuarioCliente(cliente.getIdUsuario());

        return toResponseDTO(parejaRepository.save(pareja), cliente);
    }

    @Override
    public ParejaResponseDTO obtenerPorId(Long idUsuario) {
        Pareja pareja = buscarOLanzarError(idUsuario);
        Cliente cliente = buscarClienteOLanzarError(pareja.getIdUsuarioCliente());
        return toResponseDTO(pareja, cliente);
    }

    @Override
    public List<ParejaResponseDTO> listarTodos() {
        return parejaRepository.findAll().stream()
                .map(p -> {
                    Cliente cliente = buscarClienteOLanzarError(p.getIdUsuarioCliente());
                    return toResponseDTO(p, cliente);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ParejaResponseDTO> listarPorCliente(Long idUsuarioCliente) {
        Cliente cliente = buscarClienteOLanzarError(idUsuarioCliente);
        return parejaRepository.findByIdUsuarioCliente(idUsuarioCliente).stream()
                .map(p -> toResponseDTO(p, cliente))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParejaResponseDTO actualizar(Long idUsuario, ParejaRequestDTO dto) {
        Pareja pareja = buscarOLanzarError(idUsuario);
        Cliente clienteAnterior = buscarClienteOLanzarError(pareja.getIdUsuarioCliente());
        Cliente cliente = dto.getIdUsuarioCliente().equals(clienteAnterior.getIdUsuario())
                ? clienteAnterior
                : buscarClienteOLanzarError(dto.getIdUsuarioCliente());

        transferirCupoPropio(clienteAnterior, pareja.getCupoAsignado().negate());
        transferirCupoPropio(cliente, dto.getCupoAsignado());

        pareja.setNombreUsuario(dto.getNombreUsuario());
        pareja.setContrasenia(dto.getContrasenia());
        pareja.setPrimerNombre(dto.getPrimerNombre());
        pareja.setSegundoNombre(dto.getSegundoNombre());
        pareja.setPrimerApellido(dto.getPrimerApellido());
        pareja.setSegundoApellido(dto.getSegundoApellido());
        pareja.setTelefono(dto.getTelefono());
        pareja.setCupoAsignado(dto.getCupoAsignado());
        pareja.setIdUsuarioCliente(cliente.getIdUsuario());

        return toResponseDTO(parejaRepository.update(pareja), cliente);
    }

    @Override
    @Transactional
    public ParejaResponseDTO inactivar(Long idUsuario) {
        Pareja pareja = buscarOLanzarError(idUsuario);
        Cliente cliente = buscarClienteOLanzarError(pareja.getIdUsuarioCliente());

        if ("Inactivo".equals(pareja.getEstado())) {
            throw new ReglaNegocioException("La pareja ya esta inactiva.");
        }

        transferirCupoPropio(cliente, pareja.getCupoAsignado().negate());

        pareja.setCupoAsignado(BigDecimal.ZERO);
        pareja.setEstado("Inactivo");

        return toResponseDTO(parejaRepository.update(pareja), cliente);
    }

    @Override
    @Transactional
    public ParejaResponseDTO activar(Long idUsuario) {
        Pareja pareja = buscarOLanzarError(idUsuario);
        Cliente cliente = buscarClienteOLanzarError(pareja.getIdUsuarioCliente());

        if ("Activo".equals(pareja.getEstado())) {
            throw new ReglaNegocioException("La pareja ya esta activa.");
        }

        pareja.setEstado("Activo");

        return toResponseDTO(parejaRepository.update(pareja), cliente);
    }

    @Override
    @Transactional
    public void eliminar(Long idUsuario) {
        Pareja pareja = buscarOLanzarError(idUsuario);
        Cliente cliente = buscarClienteOLanzarError(pareja.getIdUsuarioCliente());

        transferirCupoPropio(cliente, pareja.getCupoAsignado().negate());
        parejaRepository.deleteById(idUsuario);
    }

    private void transferirCupoPropio(Cliente cliente, BigDecimal monto) {
        if (monto.signum() > 0) {
            BigDecimal sumaAsignadaActual = parejaRepository.sumarCupoAsignadoPorCliente(cliente.getIdUsuario());
            BigDecimal disponible = cliente.getCupoTotalAutorizado()
                    .subtract(cliente.getCupoPropio())
                    .subtract(sumaAsignadaActual);
            if (disponible.compareTo(monto) < 0) {
                throw new ReglaNegocioException(
                        "Cupo total disponible insuficiente. Disponible: " + disponible
                                + ", cupo asignado solicitado: " + monto);
            }
        }
        cliente.setCupoPropio(cliente.getCupoPropio().subtract(monto));
        clienteRepository.update(cliente);
    }

    private void validarCedulaNoRegistradaEnOtroRol(Long idUsuario) {
        if (clienteRepository.existsById(idUsuario)) {
            throw new ReglaNegocioException(
                    "La cedula " + idUsuario + " ya esta registrada como Cliente");
        }
        if (supervisorRepository.existsById(idUsuario)) {
            throw new ReglaNegocioException(
                    "La cedula " + idUsuario + " ya esta registrada como Supervisor");
        }
    }

    private Pareja buscarOLanzarError(Long idUsuario) {
        return parejaRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro una pareja con id " + idUsuario));
    }

    private Cliente buscarClienteOLanzarError(Long idUsuarioCliente) {
        return clienteRepository.findById(idUsuarioCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el cliente titular con id " + idUsuarioCliente));
    }

    private ParejaResponseDTO toResponseDTO(Pareja p, Cliente cliente) {
        ParejaResponseDTO dto = new ParejaResponseDTO();
        dto.setIdUsuario(p.getIdUsuario());
        dto.setNombreUsuario(p.getNombreUsuario());
        dto.setPrimerNombre(p.getPrimerNombre());
        dto.setSegundoNombre(p.getSegundoNombre());
        dto.setPrimerApellido(p.getPrimerApellido());
        dto.setSegundoApellido(p.getSegundoApellido());
        dto.setTelefono(p.getTelefono());
        dto.setCupoAsignado(p.getCupoAsignado());
        dto.setEstado(p.getEstado());
        dto.setIdUsuarioCliente(cliente.getIdUsuario());
        dto.setNombreClienteTitular(cliente.getPrimerNombre() + " " + cliente.getPrimerApellido());
        dto.setContrasenia(p.getContrasenia());

        BigDecimal disponible = parejaRepository.calcularSaldoDisponible(p.getIdUsuario());
        dto.setCupoDisponible(disponible);
        dto.setCupoGastado(p.getCupoAsignado().subtract(disponible));

        return dto;
    }
}
