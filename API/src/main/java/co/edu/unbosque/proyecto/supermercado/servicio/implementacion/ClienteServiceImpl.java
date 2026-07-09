package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Cliente;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ClienteResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.ClienteRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsById(dto.getIdUsuario())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con la cedula " + dto.getIdUsuario());
        }

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
        cliente.setCupoTotalAutorizado(dto.getCupoTotalAutorizado());

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
        cliente.setCupoTotalAutorizado(dto.getCupoTotalAutorizado());

        return toResponseDTO(clienteRepository.update(cliente));
    }

    @Override
    @Transactional
    public void eliminar(Long idUsuario) {
        buscarOLanzarError(idUsuario);
        clienteRepository.deleteById(idUsuario);
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
        dto.setCupoTotalAutorizado(c.getCupoTotalAutorizado());
        dto.setEstado(c.getEstado());
        return dto;
    }
}
