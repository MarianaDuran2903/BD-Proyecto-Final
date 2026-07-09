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
import co.edu.unbosque.proyecto.supermercado.servicio.ParejaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParejaServiceImpl implements ParejaService {

    private final ParejaRepository parejaRepository;
    private final ClienteRepository clienteRepository;

    public ParejaServiceImpl(ParejaRepository parejaRepository, ClienteRepository clienteRepository) {
        this.parejaRepository = parejaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ParejaResponseDTO crear(ParejaRequestDTO dto) {
        if (parejaRepository.existsById(dto.getIdUsuario())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con la cedula " + dto.getIdUsuario());
        }

        Cliente cliente = buscarClienteOLanzarError(dto.getIdUsuarioCliente());
        validarLimiteDeIntegridad(cliente, dto.getCupoAsignado(), null);

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
        Cliente cliente = buscarClienteOLanzarError(dto.getIdUsuarioCliente());

        // Se descuenta el cupo actual de esta pareja antes de validar para no contarlo doble
        validarLimiteDeIntegridad(cliente, dto.getCupoAsignado(), pareja.getCupoAsignado());

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
    public void eliminar(Long idUsuario) {
        buscarOLanzarError(idUsuario);
        parejaRepository.deleteById(idUsuario);
    }

    // Regla de integridad: suma de cupos asignados no puede superar cupoTotalAutorizado del cliente
    private void validarLimiteDeIntegridad(Cliente cliente, BigDecimal nuevoCupo,
                                           BigDecimal cupoAnteriorDeEstaPareja) {
        BigDecimal sumaActual = parejaRepository.sumarCupoAsignadoPorCliente(cliente.getIdUsuario());

        if (cupoAnteriorDeEstaPareja != null) {
            sumaActual = sumaActual.subtract(cupoAnteriorDeEstaPareja);
        }

        BigDecimal sumaProyectada = sumaActual.add(nuevoCupo);

        if (sumaProyectada.compareTo(cliente.getCupoTotalAutorizado()) > 0) {
            throw new ReglaNegocioException(
                    "El cupo asignado excede el cupo total del cliente. "
                            + "Disponible para asignar: "
                            + cliente.getCupoTotalAutorizado().subtract(sumaActual)
                            + ", cupo solicitado: " + nuevoCupo);
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
        return dto;
    }
}
