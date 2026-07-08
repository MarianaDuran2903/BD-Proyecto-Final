package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.time.LocalDate;
import java.time.LocalTime;

import co.edu.unbosque.proyecto.supermercado.modelo.AutorizacionSobrecupo;
import co.edu.unbosque.proyecto.supermercado.modelo.Cliente;
import co.edu.unbosque.proyecto.supermercado.modelo.Compra;
import co.edu.unbosque.proyecto.supermercado.modelo.Supervisor;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.AutorizacionSobrecupoRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.AutorizacionSobrecupoResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.AutorizacionSobrecupoRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ClienteRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.CompraRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.SupervisorRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.AutorizacionSobrecupoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorizacionSobrecupoServiceImpl implements AutorizacionSobrecupoService {

    private final AutorizacionSobrecupoRepository autorizacionSobrecupoRepository;
    private final CompraRepository compraRepository;
    private final ClienteRepository clienteRepository;
    private final SupervisorRepository supervisorRepository;
    private final ModelMapper mm = new ModelMapper();

    public AutorizacionSobrecupoServiceImpl(AutorizacionSobrecupoRepository autorizacionSobrecupoRepository,
                                            CompraRepository compraRepository,
                                            ClienteRepository clienteRepository,
                                            SupervisorRepository supervisorRepository) {
        this.autorizacionSobrecupoRepository = autorizacionSobrecupoRepository;
        this.compraRepository = compraRepository;
        this.clienteRepository = clienteRepository;
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    @Transactional
    public AutorizacionSobrecupoResponseDTO crear(AutorizacionSobrecupoRequestDTO dto) {
        Compra compra = compraRepository.findById(dto.getIdCompra())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la compra con id " + dto.getIdCompra()));

        if (!Boolean.TRUE.equals(compra.getRequiereSobrecupo())) {
            throw new ReglaNegocioException(
                    "La compra " + compra.getIdCompra() + " no quedó marcada como pendiente de sobrecupo");
        }

        if (autorizacionSobrecupoRepository.findByCompra_IdCompra(dto.getIdCompra()).isPresent()) {
            throw new ReglaNegocioException(
                    "La compra " + compra.getIdCompra() + " ya tiene una autorización de sobrecupo registrada");
        }

        Cliente cliente = clienteRepository.findById(dto.getIdUsuarioCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el cliente con id " + dto.getIdUsuarioCliente()));

        // Excepción de Sobrecupo: la autorización solo es válida si el
        // cliente es el titular de la pareja que realizó la compra.
        if (!compra.getPareja().getCliente().getIdUsuario().equals(cliente.getIdUsuario())) {
            throw new ReglaNegocioException(
                    "El cliente indicado no es el titular de la pareja que realizó esta compra");
        }

        Supervisor supervisor = supervisorRepository.findById(dto.getIdUsuarioSupervisor())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el supervisor con id " + dto.getIdUsuarioSupervisor()));

        // El supervisor que aprueba debe pertenecer al almacén donde
        // ocurrió la compra (aprobación "en el punto de venta").
        if (!supervisor.getAlmacen().getIdAlmacen().equals(compra.getAlmacen().getIdAlmacen())) {
            throw new ReglaNegocioException(
                    "El supervisor no pertenece al almacén donde se realizó la compra");
        }

        AutorizacionSobrecupo autorizacion = new AutorizacionSobrecupo();
        autorizacion.setFecha(LocalDate.now());
        autorizacion.setHora(LocalTime.now());
        autorizacion.setMontoAutorizado(dto.getMontoAutorizado());
        autorizacion.setCompra(compra);
        autorizacion.setCliente(cliente);
        autorizacion.setSupervisor(supervisor);

        AutorizacionSobrecupo guardada = autorizacionSobrecupoRepository.save(autorizacion);

        // Mantiene sincronizada la FK directa Compra-Supervisor que aún
        // está pendiente de revisión con el equipo (ver notas del MERE).
        compra.setSupervisor(supervisor);
        compraRepository.save(compra);

        return mapearAResponseDTO(guardada);
    }

    @Override
    public AutorizacionSobrecupoResponseDTO obtenerPorCompra(Long idCompra) {
        AutorizacionSobrecupo autorizacion = autorizacionSobrecupoRepository.findByCompra_IdCompra(idCompra)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La compra " + idCompra + " no tiene autorización de sobrecupo registrada"));
        return mapearAResponseDTO(autorizacion);
    }

    private AutorizacionSobrecupoResponseDTO mapearAResponseDTO(AutorizacionSobrecupo autorizacion) {
        AutorizacionSobrecupoResponseDTO dto = mm.map(autorizacion, AutorizacionSobrecupoResponseDTO.class);
        dto.setIdCompra(autorizacion.getCompra().getIdCompra());

        dto.setIdUsuarioCliente(autorizacion.getCliente().getIdUsuario());
        dto.setNombreClienteCompleto(
                autorizacion.getCliente().getPrimerNombre() + " " + autorizacion.getCliente().getPrimerApellido());

        dto.setIdUsuarioSupervisor(autorizacion.getSupervisor().getIdUsuario());
        dto.setNombreSupervisorCompleto(
                autorizacion.getSupervisor().getPrimerNombre() + " " + autorizacion.getSupervisor().getPrimerApellido());

        return dto;
    }
}