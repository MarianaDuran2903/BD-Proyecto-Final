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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper mm = new ModelMapper();

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsByCedula(dto.getCedula())) {
            throw new ReglaNegocioException("Ya existe un cliente registrado con la cédula " + dto.getCedula());
        }

        Cliente cliente = mm.map(dto, Cliente.class);
        cliente.setIdUsuario(null); // aseguramos que lo genere la BD
        cliente.setEstado("Activo");

        Cliente guardado = clienteRepository.save(cliente);
        return mm.map(guardado, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO obtenerPorId(Long idUsuario) {
        Cliente cliente = buscarPorIdOLanzarError(idUsuario);
        return mm.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(cliente -> mm.map(cliente, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClienteResponseDTO actualizar(Long idUsuario, ClienteRequestDTO dto) {
        Cliente cliente = buscarPorIdOLanzarError(idUsuario);

        cliente.setNombreUsuario(dto.getNombreUsuario());
        cliente.setContrasenia(dto.getContrasenia());
        cliente.setCedula(dto.getCedula());
        cliente.setPrimerNombre(dto.getPrimerNombre());
        cliente.setSegundoNombre(dto.getSegundoNombre());
        cliente.setPrimerApellido(dto.getPrimerApellido());
        cliente.setSegundoApellido(dto.getSegundoApellido());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCupoTotalAutorizado(dto.getCupoTotalAutorizado());

        Cliente actualizado = clienteRepository.save(cliente);
        return mm.map(actualizado, ClienteResponseDTO.class);
    }

    @Override
    @Transactional
    public void eliminar(Long idUsuario) {
        Cliente cliente = buscarPorIdOLanzarError(idUsuario);
        clienteRepository.delete(cliente);
    }

    private Cliente buscarPorIdOLanzarError(Long idUsuario) {
        return clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un cliente con id " + idUsuario));
    }
}
