package co.edu.unbosque.proyecto.supermercado.servicio.implementacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.unbosque.proyecto.supermercado.modelo.*;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.DecisionSolicitudDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SolicitudSobrecupoRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SolicitudSobrecupoResponseDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.RecursoNoEncontradoException;
import co.edu.unbosque.proyecto.supermercado.modelo.excepciones.ReglaNegocioException;
import co.edu.unbosque.proyecto.supermercado.repositorio.*;
import co.edu.unbosque.proyecto.supermercado.servicio.SolicitudSobrecupoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudSobrecupoServiceImpl implements SolicitudSobrecupoService {

    private final SolicitudSobrecupoRepository solicitudRepository;
    private final ParejaRepository parejaRepository;
    private final ClienteRepository clienteRepository;
    private final SupervisorRepository supervisorRepository;
    private final AlmacenRepository almacenRepository;
    private final CompraRepository compraRepository;

    public SolicitudSobrecupoServiceImpl(SolicitudSobrecupoRepository solicitudRepository,
                                         ParejaRepository parejaRepository,
                                         ClienteRepository clienteRepository,
                                         SupervisorRepository supervisorRepository,
                                         AlmacenRepository almacenRepository,
                                         CompraRepository compraRepository) {
        this.solicitudRepository = solicitudRepository;
        this.parejaRepository = parejaRepository;
        this.clienteRepository = clienteRepository;
        this.supervisorRepository = supervisorRepository;
        this.almacenRepository = almacenRepository;
        this.compraRepository = compraRepository;
    }

    @Override
    @Transactional
    public SolicitudSobrecupoResponseDTO crear(SolicitudSobrecupoRequestDTO dto) {
        Pareja pareja = parejaRepository.findById(dto.getIdUsuarioPareja())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la pareja con id " + dto.getIdUsuarioPareja()));

        Cliente cliente = clienteRepository.findById(dto.getIdUsuarioCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el cliente con id " + dto.getIdUsuarioCliente()));

        if (!pareja.getIdUsuarioCliente().equals(cliente.getIdUsuario())) {
            throw new ReglaNegocioException("La pareja no pertenece al cliente indicado");
        }

        Almacen almacen = almacenRepository.findById(dto.getIdAlmacen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro el almacen con id " + dto.getIdAlmacen()));

        SolicitudSobrecupo solicitud = new SolicitudSobrecupo();
        solicitud.setFecha(LocalDate.now());
        solicitud.setHora(LocalTime.now());
        solicitud.setMontoSolicitado(dto.getMontoSolicitado());
        solicitud.setMontoAutorizado(null);
        solicitud.setEstado("Pendiente");
        solicitud.setIdCompra(null);
        solicitud.setIdUsuarioCliente(cliente.getIdUsuario());
        solicitud.setIdUsuarioPareja(pareja.getIdUsuario());
        solicitud.setIdUsuarioSupervisor(null);
        solicitud.setIdAlmacen(almacen.getIdAlmacen());

        return toResponseDTO(solicitudRepository.save(solicitud), cliente, pareja, null, almacen);
    }

    @Override
    public SolicitudSobrecupoResponseDTO obtenerPorId(Long codSolicitud) {
        SolicitudSobrecupo s = buscarOLanzarError(codSolicitud);
        return toResponseDTOConEntidades(s);
    }

    @Override
    public List<SolicitudSobrecupoResponseDTO> listarTodos() {
        return solicitudRepository.findAll().stream()
                .map(this::toResponseDTOConEntidades)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudSobrecupoResponseDTO> listarPorCliente(Long idUsuarioCliente) {
        return solicitudRepository.findByIdUsuarioCliente(idUsuarioCliente).stream()
                .map(this::toResponseDTOConEntidades)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudSobrecupoResponseDTO> listarPorPareja(Long idUsuarioPareja) {
        return solicitudRepository.findByIdUsuarioPareja(idUsuarioPareja).stream()
                .map(this::toResponseDTOConEntidades)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudSobrecupoResponseDTO> listarPendientesPorCliente(Long idUsuarioCliente) {
        return solicitudRepository.findPendientesByCliente(idUsuarioCliente).stream()
                .map(this::toResponseDTOConEntidades)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudSobrecupoResponseDTO> listarPendientesPorSupervisor(Long idUsuarioSupervisor) {
        return solicitudRepository.findPendientesBySupervisor(idUsuarioSupervisor).stream()
                .map(this::toResponseDTOConEntidades)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SolicitudSobrecupoResponseDTO decidirComoCliente(Long codSolicitud, DecisionSolicitudDTO dto) {
        SolicitudSobrecupo solicitud = buscarOLanzarError(codSolicitud);

        if (!"Pendiente".equals(solicitud.getEstado())) {
            throw new ReglaNegocioException(
                    "Solo se puede decidir sobre solicitudes en estado 'Pendiente'. Estado actual: "
                            + solicitud.getEstado());
        }

        if ("Rechazar".equals(dto.getDecision())) {
            solicitud.setEstado("Rechazada");
            return toResponseDTOConEntidades(solicitudRepository.update(solicitud));
        }

        // Aprobar: verificar si el cliente tiene cupo disponible para redistribuir
        Cliente cliente = clienteRepository.findById(solicitud.getIdUsuarioCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        BigDecimal sumaAsignada = parejaRepository.sumarCupoAsignadoPorCliente(cliente.getIdUsuario());
        BigDecimal cupoDisponibleCliente = cliente.getCupoTotalAutorizado().subtract(sumaAsignada);

        if (cupoDisponibleCliente.compareTo(solicitud.getMontoSolicitado()) >= 0) {
            // Cliente tiene cupo: aprueba directamente sin supervisor
            Pareja pareja = parejaRepository.findById(solicitud.getIdUsuarioPareja())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Pareja no encontrada"));

            BigDecimal nuevoCupoPareja = pareja.getCupoAsignado().add(solicitud.getMontoSolicitado());
            parejaRepository.actualizarCupoAsignado(pareja.getIdUsuario(), nuevoCupoPareja);

            Compra compra = registrarCompraPostAprobacion(solicitud, null);

            solicitud.setEstado("Aprobada Cliente");
            solicitud.setMontoAutorizado(solicitud.getMontoSolicitado());
            solicitud.setIdCompra(compra.getCodCompra());
            return toResponseDTOConEntidades(solicitudRepository.update(solicitud));

        } else {
            // Cliente no tiene cupo: escalar al supervisor del almacen
            List<Supervisor> supervisores = supervisorRepository.findByIdAlmacen(solicitud.getIdAlmacen());
            Supervisor supervisor = supervisores.stream()
                    .filter(sv -> "Activo".equals(sv.getEstado()))
                    .findFirst()
                    .orElseThrow(() -> new ReglaNegocioException(
                            "No hay supervisores activos en el almacen para gestionar el sobrecupo"));

            solicitud.setEstado("Pendiente Supervisor");
            solicitud.setIdUsuarioSupervisor(supervisor.getIdUsuario());
            return toResponseDTOConEntidades(solicitudRepository.update(solicitud));
        }
    }

    @Override
    @Transactional
    public SolicitudSobrecupoResponseDTO decidirComoSupervisor(Long codSolicitud, DecisionSolicitudDTO dto) {
        SolicitudSobrecupo solicitud = buscarOLanzarError(codSolicitud);

        if (!"Pendiente Supervisor".equals(solicitud.getEstado())) {
            throw new ReglaNegocioException(
                    "Solo se puede decidir sobre solicitudes en estado 'Pendiente Supervisor'. Estado actual: "
                            + solicitud.getEstado());
        }

        if ("Rechazar".equals(dto.getDecision())) {
            solicitud.setEstado("Rechazada");
            return toResponseDTOConEntidades(solicitudRepository.update(solicitud));
        }

        // Supervisor aprueba: aumentar cupo del cliente y de la pareja, luego registrar compra
        Cliente cliente = clienteRepository.findById(solicitud.getIdUsuarioCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        Pareja pareja = parejaRepository.findById(solicitud.getIdUsuarioPareja())
                .orElseThrow(() -> new RecursoNoEncontradoException("Pareja no encontrada"));

        BigDecimal monto = solicitud.getMontoSolicitado();

        // Incrementar cupo total del cliente
        clienteRepository.actualizarCupoTotal(
                cliente.getIdUsuario(), cliente.getCupoTotalAutorizado().add(monto));

        // Incrementar cupo asignado de la pareja
        parejaRepository.actualizarCupoAsignado(
                pareja.getIdUsuario(), pareja.getCupoAsignado().add(monto));

        Supervisor supervisor = supervisorRepository.findById(solicitud.getIdUsuarioSupervisor())
                .orElseThrow(() -> new RecursoNoEncontradoException("Supervisor no encontrado"));

        Compra compra = registrarCompraPostAprobacion(solicitud, supervisor.getIdUsuario());

        solicitud.setEstado("Aprobada Supervisor");
        solicitud.setMontoAutorizado(monto);
        solicitud.setIdCompra(compra.getCodCompra());
        return toResponseDTOConEntidades(solicitudRepository.update(solicitud));
    }

    // Crea la compra una vez que el sobrecupo fue aprobado
    private Compra registrarCompraPostAprobacion(SolicitudSobrecupo solicitud, Long idSupervisor) {
        Compra compra = new Compra();
        compra.setMonto(solicitud.getMontoSolicitado());
        compra.setFecha(LocalDate.now());
        compra.setHora(LocalTime.now());
        compra.setRequiereSobrecupo(true);
        compra.setIdUsuarioPareja(solicitud.getIdUsuarioPareja());
        compra.setIdAlmacen(solicitud.getIdAlmacen());
        compra.setIdUsuarioSupervisor(idSupervisor);
        return compraRepository.save(compra);
    }

    private SolicitudSobrecupo buscarOLanzarError(Long codSolicitud) {
        return solicitudRepository.findById(codSolicitud)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la solicitud con codigo " + codSolicitud));
    }

    // Resuelve todas las entidades relacionadas y construye el DTO de respuesta
    private SolicitudSobrecupoResponseDTO toResponseDTOConEntidades(SolicitudSobrecupo s) {
        Cliente cliente = clienteRepository.findById(s.getIdUsuarioCliente()).orElse(null);
        Pareja pareja = parejaRepository.findById(s.getIdUsuarioPareja()).orElse(null);
        Supervisor supervisor = s.getIdUsuarioSupervisor() != null
                ? supervisorRepository.findById(s.getIdUsuarioSupervisor()).orElse(null)
                : null;
        Almacen almacen = almacenRepository.findById(s.getIdAlmacen()).orElse(null);

        return toResponseDTO(s, cliente, pareja, supervisor, almacen);
    }

    private SolicitudSobrecupoResponseDTO toResponseDTO(SolicitudSobrecupo s, Cliente cliente,
                                                        Pareja pareja, Supervisor supervisor,
                                                        Almacen almacen) {
        SolicitudSobrecupoResponseDTO dto = new SolicitudSobrecupoResponseDTO();
        dto.setCodSolicitud(s.getCodSolicitud());
        dto.setFecha(s.getFecha());
        dto.setHora(s.getHora());
        dto.setMontoSolicitado(s.getMontoSolicitado());
        dto.setMontoAutorizado(s.getMontoAutorizado());
        dto.setEstado(s.getEstado());
        dto.setIdCompra(s.getIdCompra());

        if (cliente != null) {
            dto.setIdUsuarioCliente(cliente.getIdUsuario());
            dto.setNombreClienteCompleto(cliente.getPrimerNombre() + " " + cliente.getPrimerApellido());
        }
        if (pareja != null) {
            dto.setIdUsuarioPareja(pareja.getIdUsuario());
            dto.setNombreParejaCompleto(pareja.getPrimerNombre() + " " + pareja.getPrimerApellido());
        }
        if (supervisor != null) {
            dto.setIdUsuarioSupervisor(supervisor.getIdUsuario());
            dto.setNombreSupervisorCompleto(
                    supervisor.getPrimerNombre() + " " + supervisor.getPrimerApellido());
        }
        if (almacen != null) {
            dto.setIdAlmacen(almacen.getIdAlmacen());
            dto.setNombreAlmacen(almacen.getNombreAlmacen());
        }
        return dto;
    }
}
