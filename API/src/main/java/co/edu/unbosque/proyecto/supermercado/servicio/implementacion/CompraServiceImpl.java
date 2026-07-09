package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.Almacen;
import co.edu.unbosque.proyecto.supermercado.modelo.Compra;
import co.edu.unbosque.proyecto.supermercado.modelo.Pareja;
import co.edu.unbosque.proyecto.supermercado.modelo.Supervisor;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.AlmacenRepository;
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
    private final AlmacenRepository almacenRepository;
    private final SupervisorRepository supervisorRepository;
    private final RestriccionHorarioRepository restriccionHorarioRepository;

    public CompraServiceImpl(CompraRepository compraRepository,
                             ParejaRepository parejaRepository,
                             AlmacenRepository almacenRepository,
                             SupervisorRepository supervisorRepository,
                             RestriccionHorarioRepository restriccionHorarioRepository) {
        this.compraRepository = compraRepository;
        this.parejaRepository = parejaRepository;
        this.almacenRepository = almacenRepository;
        this.supervisorRepository = supervisorRepository;
        this.restriccionHorarioRepository = restriccionHorarioRepository;
    }

    @Override
    @Transactional
    public CompraResponseDTO registrar(CompraRequestDTO dto) {
        Pareja pareja = parejaRepository.findById(dto.getIdUsuarioPareja())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la pareja con id " + dto.getIdUsuarioPareja()));

        Almacen almacen = almacenRepository.findById(dto.getIdAlmacen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el almacen con id " + dto.getIdAlmacen()));

        // Verificar si la pareja tiene una restriccion de horario para esta fecha y hora
        boolean bloqueada = restriccionHorarioRepository.existeBloqueoActivo(
                pareja.getIdUsuario(), dto.getFecha(), dto.getHora());

        if (bloqueada) {
            throw new ReglaNegocioException(
                    "La pareja tiene una restriccion de horario configurada para el "
                            + dto.getFecha() + " a las " + dto.getHora()
                            + ". La compra no puede realizarse.");
        }

        // Verificar si tiene cupo suficiente
        BigDecimal saldoDisponible = parejaRepository.calcularSaldoDisponible(pareja.getIdUsuario());

        if (dto.getMonto().compareTo(saldoDisponible) > 0) {
            throw new ReglaNegocioException(
                    "Cupo insuficiente. Saldo disponible: " + saldoDisponible
                            + ". Debe solicitar un sobrecupo antes de realizar esta compra.");
        }

        Compra compra = new Compra();
        compra.setMonto(dto.getMonto());
        compra.setFecha(dto.getFecha());
        compra.setHora(dto.getHora());
        compra.setRequiereSobrecupo(false);
        compra.setIdUsuarioPareja(pareja.getIdUsuario());
        compra.setIdAlmacen(almacen.getIdAlmacen());
        compra.setIdUsuarioSupervisor(null);

        return toResponseDTO(compraRepository.save(compra), pareja, almacen, null);
    }

    @Override
    public CompraResponseDTO obtenerPorId(Long codCompra) {
        Compra compra = compraRepository.findById(codCompra)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la compra con codigo " + codCompra));

        Pareja pareja = parejaRepository.findById(compra.getIdUsuarioPareja())
                .orElseThrow(() -> new RecursoNoEncontradoException("Pareja no encontrada"));

        Almacen almacen = almacenRepository.findById(compra.getIdAlmacen())
                .orElseThrow(() -> new RecursoNoEncontradoException("Almacen no encontrado"));

        Supervisor supervisor = null;
        if (compra.getIdUsuarioSupervisor() != null) {
            supervisor = supervisorRepository.findById(compra.getIdUsuarioSupervisor()).orElse(null);
        }

        return toResponseDTO(compra, pareja, almacen, supervisor);
    }

    @Override
    public List<CompraResponseDTO> listarTodos() {
        return compraRepository.findAll().stream()
                .map(c -> {
                    Pareja pareja = parejaRepository.findById(c.getIdUsuarioPareja()).orElse(null);
                    Almacen almacen = almacenRepository.findById(c.getIdAlmacen()).orElse(null);
                    Supervisor supervisor = c.getIdUsuarioSupervisor() != null
                            ? supervisorRepository.findById(c.getIdUsuarioSupervisor()).orElse(null)
                            : null;
                    return toResponseDTO(c, pareja, almacen, supervisor);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CompraResponseDTO> listarPorPareja(Long idUsuarioPareja) {
        Pareja pareja = parejaRepository.findById(idUsuarioPareja)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la pareja con id " + idUsuarioPareja));

        return compraRepository.findByIdUsuarioPareja(idUsuarioPareja).stream()
                .map(c -> {
                    Almacen almacen = almacenRepository.findById(c.getIdAlmacen()).orElse(null);
                    Supervisor supervisor = c.getIdUsuarioSupervisor() != null
                            ? supervisorRepository.findById(c.getIdUsuarioSupervisor()).orElse(null)
                            : null;
                    return toResponseDTO(c, pareja, almacen, supervisor);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CompraResponseDTO> listarPorCliente(Long idUsuarioCliente) {
        return compraRepository.findByIdUsuarioCliente(idUsuarioCliente).stream()
                .map(c -> {
                    Pareja pareja = parejaRepository.findById(c.getIdUsuarioPareja()).orElse(null);
                    Almacen almacen = almacenRepository.findById(c.getIdAlmacen()).orElse(null);
                    Supervisor supervisor = c.getIdUsuarioSupervisor() != null
                            ? supervisorRepository.findById(c.getIdUsuarioSupervisor()).orElse(null)
                            : null;
                    return toResponseDTO(c, pareja, almacen, supervisor);
                })
                .collect(Collectors.toList());
    }

    private CompraResponseDTO toResponseDTO(Compra c, Pareja pareja, Almacen almacen,
                                            Supervisor supervisor) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setCodCompra(c.getCodCompra());
        dto.setMonto(c.getMonto());
        dto.setFecha(c.getFecha());
        dto.setHora(c.getHora());
        dto.setRequiereSobrecupo(c.getRequiereSobrecupo());

        if (pareja != null) {
            dto.setIdUsuarioPareja(pareja.getIdUsuario());
            dto.setNombreParejaCompleto(pareja.getPrimerNombre() + " " + pareja.getPrimerApellido());
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
