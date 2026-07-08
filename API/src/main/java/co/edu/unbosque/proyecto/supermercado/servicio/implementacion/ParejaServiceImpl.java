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
import co.edu.unbosque.proyecto.supermercado.servicio.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParejaServiceImpl implements ParejaService {

    private final ParejaRepository parejaRepository;
    private final ClienteRepository clienteRepository;
    private final ModelMapper mm = new ModelMapper();

    public ParejaServiceImpl(ParejaRepository parejaRepository, ClienteRepository clienteRepository) {
        this.parejaRepository = parejaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ParejaResponseDTO crear(ParejaRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdUsuarioCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el cliente titular con id " + dto.getIdUsuarioCliente()));

        if (parejaRepository.existsByCedula(dto.getCedula())) {
            throw new ReglaNegocioException("Ya existe una pareja registrada con la cédula " + dto.getCedula());
        }

        // Regla de negocio: Límite de Integridad. La suma de los cupos
        // asignados a las parejas de un cliente no puede superar el
        // cupo total autorizado del cliente.
        validarLimiteDeIntegridad(cliente, dto.getCupoAsignado(), null);

        Pareja pareja = mm.map(dto, Pareja.class);
        pareja.setIdUsuario(null);
        pareja.setEstado("Activo");
        pareja.setCliente(cliente);

        Pareja guardada = parejaRepository.save(pareja);
        return mapearAResponseDTO(guardada);
    }

    @Override
    public ParejaResponseDTO obtenerPorId(Long idUsuario) {
        Pareja pareja = buscarPorIdOLanzarError(idUsuario);
        return mapearAResponseDTO(pareja);
    }

    @Override
    public List<ParejaResponseDTO> listarPorCliente(Long idUsuarioCliente) {
        return parejaRepository.findByCliente_IdUsuario(idUsuarioCliente).stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParejaResponseDTO actualizar(Long idUsuario, ParejaRequestDTO dto) {
        Pareja pareja = buscarPorIdOLanzarError(idUsuario);
        Cliente cliente = clienteRepository.findById(dto.getIdUsuarioCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el cliente titular con id " + dto.getIdUsuarioCliente()));

        // Al actualizar, se excluye el cupo actual de esta pareja de la
        // suma antes de validar, para no contarlo dos veces.
        validarLimiteDeIntegridad(cliente, dto.getCupoAsignado(), pareja.getCupoAsignado());

        pareja.setNombreUsuario(dto.getNombreUsuario());
        pareja.setContrasenia(dto.getContrasenia());
        pareja.setCedula(dto.getCedula());
        pareja.setPrimerNombre(dto.getPrimerNombre());
        pareja.setSegundoNombre(dto.getSegundoNombre());
        pareja.setPrimerApellido(dto.getPrimerApellido());
        pareja.setSegundoApellido(dto.getSegundoApellido());
        pareja.setCupoAsignado(dto.getCupoAsignado());
        pareja.setCliente(cliente);

        Pareja actualizada = parejaRepository.save(pareja);
        return mapearAResponseDTO(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long idUsuario) {
        Pareja pareja = buscarPorIdOLanzarError(idUsuario);
        parejaRepository.delete(pareja);
    }

    /**
     * Valida que sumar el nuevo cupo (restando el cupo anterior de la
     * misma pareja si aplica, es decir en una actualización) no supere
     * el cupo_total_autorizado del cliente.
     *
     * @param cupoAnteriorDeEstaPareja usar null cuando es una creación
     *                                 (la pareja aún no existe, no hay
     *                                 nada que restar).
     */
    private void validarLimiteDeIntegridad(Cliente cliente, BigDecimal nuevoCupo, BigDecimal cupoAnteriorDeEstaPareja) {
        BigDecimal sumaActual = parejaRepository.sumarCupoAsignadoPorCliente(cliente.getIdUsuario());

        if (cupoAnteriorDeEstaPareja != null) {
            sumaActual = sumaActual.subtract(cupoAnteriorDeEstaPareja);
        }

        BigDecimal sumaProyectada = sumaActual.add(nuevoCupo);

        if (sumaProyectada.compareTo(cliente.getCupoTotalAutorizado()) > 0) {
            throw new ReglaNegocioException(
                    "El cupo asignado excede el cupo total autorizado del cliente. "
                            + "Cupo total: " + cliente.getCupoTotalAutorizado()
                            + ", suma ya asignada a otras parejas: " + sumaActual
                            + ", cupo solicitado: " + nuevoCupo);
        }
    }

    private Pareja buscarPorIdOLanzarError(Long idUsuario) {
        return parejaRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró una pareja con id " + idUsuario));
    }

    private ParejaResponseDTO mapearAResponseDTO(Pareja pareja) {
        ParejaResponseDTO dto = mm.map(pareja, ParejaResponseDTO.class);
        dto.setIdUsuarioCliente(pareja.getCliente().getIdUsuario());
        dto.setNombreClienteTitular(
                pareja.getCliente().getPrimerNombre() + " " + pareja.getCliente().getPrimerApellido());
        return dto;
    }
}
