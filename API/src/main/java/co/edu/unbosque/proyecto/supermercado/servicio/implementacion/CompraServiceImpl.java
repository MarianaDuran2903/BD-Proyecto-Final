package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Almacen;
import co.edu.unbosque.proyecto.supermercado.modelo.Cliente;
import co.edu.unbosque.proyecto.supermercado.modelo.Compra;
import co.edu.unbosque.proyecto.supermercado.modelo.Pareja;
import co.edu.unbosque.proyecto.supermercado.modelo.Supervisor;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.AlmacenRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ClienteRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.CompraRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.ParejaRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.RestriccionHorarioRepository;
import co.edu.unbosque.proyecto.supermercado.repositorio.SupervisorRepository;
import co.edu.unbosque.proyecto.supermercado.servicio.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ParejaRepository parejaRepository;
    private final ClienteRepository clienteRepository;
    private final AlmacenRepository almacenRepository;
    private final SupervisorRepository supervisorRepository;
    private final RestriccionHorarioRepository restriccionHorarioRepository;

    public CompraServiceImpl(CompraRepository compraRepository,
                             ParejaRepository parejaRepository,
                             ClienteRepository clienteRepository,
                             AlmacenRepository almacenRepository,
                             SupervisorRepository supervisorRepository,
                             RestriccionHorarioRepository restriccionHorarioRepository) {
        this.compraRepository = compraRepository;
        this.parejaRepository = parejaRepository;
        this.clienteRepository = clienteRepository;
        this.almacenRepository = almacenRepository;
        this.supervisorRepository = supervisorRepository;
        this.restriccionHorarioRepository = restriccionHorarioRepository;
    }

    @Override
    @Transactional
    public CompraResponseDTO registrar(CompraRequestDTO dto) {
        boolean tienePareja = dto.getIdUsuarioPareja() != null;
        boolean tieneCliente = dto.getIdUsuarioCliente() != null;

        if (tienePareja == tieneCliente) {
            throw new ReglaNegocioException(
                    "Debe indicar exactamente un usuario que realiza la compra: pareja o cliente, no ambos ni ninguno.");
        }

        Almacen almacen = almacenRepository.findById(dto.getIdAlmacen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el almacen con id " + dto.getIdAlmacen()));

        Supervisor supervisorQueRegistra = supervisorRepository.findById(dto.getIdUsuarioSupervisor())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el supervisor con id " + dto.getIdUsuarioSupervisor()));

        Pareja pareja = null;
        Cliente cliente = null;

        if (tienePareja) {
            pareja = parejaRepository.findById(dto.getIdUsuarioPareja())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontro la pareja con id " + dto.getIdUsuarioPareja()));

            boolean bloqueada = restriccionHorarioRepository.existeBloqueoActivo(
                    pareja.getIdUsuario(), dto.getFecha(), dto.getHora());

            if (bloqueada) {
                throw new ReglaNegocioException(
                        "La pareja tiene una restriccion de horario configurada para el "
                                + dto.getFecha() + " a las " + dto.getHora()
                                + ". La compra no puede realizarse.");
            }

            BigDecimal saldoDisponible = parejaRepository.calcularSaldoDisponible(pareja.getIdUsuario());

            if (dto.getMonto().compareTo(saldoDisponible) > 0) {
                throw new ReglaNegocioException(
                        "Cupo insuficiente. Saldo disponible: " + saldoDisponible);
            }
        } else {
            cliente = clienteRepository.findById(dto.getIdUsuarioCliente())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontro el cliente con id " + dto.getIdUsuarioCliente()));

            BigDecimal saldoDisponible = clienteRepository.calcularSaldoPropioDisponible(cliente.getIdUsuario());

            if (dto.getMonto().compareTo(saldoDisponible) > 0) {
                throw new ReglaNegocioException(
                        "Cupo propio insuficiente. Saldo disponible: " + saldoDisponible);
            }
        }

        Compra compra = new Compra();
        compra.setMonto(dto.getMonto());
        compra.setFecha(dto.getFecha());
        compra.setHora(dto.getHora());
        compra.setIdUsuarioPareja(dto.getIdUsuarioPareja());
        compra.setIdUsuarioCliente(dto.getIdUsuarioCliente());
        compra.setIdAlmacen(almacen.getIdAlmacen());
        compra.setIdUsuarioSupervisor(supervisorQueRegistra.getIdUsuario());

        return toResponseDTO(compraRepository.save(compra), pareja, cliente, almacen, supervisorQueRegistra);
    }

    @Override
    public CompraResponseDTO obtenerPorId(Long codCompra) {
        Compra compra = compraRepository.findById(codCompra)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la compra con codigo " + codCompra));

        return toResponseDTOResuelto(compra);
    }

    @Override
    public List<CompraResponseDTO> listarTodos() {
        return compraRepository.findAll().stream()
                .map(this::toResponseDTOResuelto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompraResponseDTO> listarPorPareja(Long idUsuarioPareja) {
        parejaRepository.findById(idUsuarioPareja)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la pareja con id " + idUsuarioPareja));

        return compraRepository.findByIdUsuarioPareja(idUsuarioPareja).stream()
                .map(this::toResponseDTOResuelto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompraResponseDTO> listarPorCliente(Long idUsuarioCliente) {
        clienteRepository.findById(idUsuarioCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el cliente con id " + idUsuarioCliente));

        return compraRepository.findByIdUsuarioCliente(idUsuarioCliente).stream()
                .map(this::toResponseDTOResuelto)
                .collect(Collectors.toList());
    }

    private CompraResponseDTO toResponseDTOResuelto(Compra c) {
        Pareja pareja = c.getIdUsuarioPareja() != null
                ? parejaRepository.findById(c.getIdUsuarioPareja()).orElse(null)
                : null;
        Cliente cliente = c.getIdUsuarioCliente() != null
                ? clienteRepository.findById(c.getIdUsuarioCliente()).orElse(null)
                : null;
        Almacen almacen = almacenRepository.findById(c.getIdAlmacen()).orElse(null);
        Supervisor supervisor = supervisorRepository.findById(c.getIdUsuarioSupervisor()).orElse(null);

        return toResponseDTO(c, pareja, cliente, almacen, supervisor);
    }

    private CompraResponseDTO toResponseDTO(Compra c, Pareja pareja, Cliente cliente, Almacen almacen,
                                            Supervisor supervisor) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setCodCompra(c.getCodCompra());
        dto.setMonto(c.getMonto());
        dto.setFecha(c.getFecha());
        dto.setHora(c.getHora());

        if (pareja != null) {
            dto.setIdUsuarioPareja(pareja.getIdUsuario());
            dto.setNombreParejaCompleto(pareja.getPrimerNombre() + " " + pareja.getPrimerApellido());
        }
        if (cliente != null) {
            dto.setIdUsuarioCliente(cliente.getIdUsuario());
            dto.setNombreClienteCompleto(cliente.getPrimerNombre() + " " + cliente.getPrimerApellido());
        }
        if (almacen != null) {
            dto.setIdAlmacen(almacen.getIdAlmacen());
            dto.setNombreAlmacen(almacen.getNombreAlmacen());
        }
        if (supervisor != null) {
            dto.setIdUsuarioSupervisor(supervisor.getIdUsuario());
            dto.setNombreSupervisorCompleto(
                    supervisor.getPrimerNombre() + " " + supervisor.getPrimerApellido());
        }
        return dto;
    }
}
