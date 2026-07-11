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

    public SolicitudSobrecupoServiceImpl(SolicitudSobrecupoRepository solicitudRepository,
                                         ParejaRepository parejaRepository,
                                         ClienteRepository clienteRepository,
                                         SupervisorRepository supervisorRepository) {
        this.solicitudRepository = solicitudRepository;
        this.parejaRepository = parejaRepository;
        this.clienteRepository = clienteRepository;
        this.supervisorRepository = supervisorRepository;
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

        SolicitudSobrecupo solicitud = new SolicitudSobrecupo();
        solicitud.setFecha(LocalDate.now());
        solicitud.setHora(LocalTime.now());
        solicitud.setMontoSolicitado(dto.getMontoSolicitado());
        solicitud.setMontoAutorizado(null);
        solicitud.setEstado("pendiente_cliente");
        solicitud.setIdUsuarioCliente(cliente.getIdUsuario());
        solicitud.setIdUsuarioPareja(pareja.getIdUsuario());
        solicitud.setIdUsuarioSupervisor(null);

        return toResponseDTO(solicitudRepository.save(solicitud), cliente, pareja, null);
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

        if (!"pendiente_cliente".equals(solicitud.getEstado())) {
            throw new ReglaNegocioException(
                    "Solo se puede decidir sobre solicitudes en estado 'pendiente_cliente'. Estado actual: "
                            + solicitud.getEstado());
        }

        if ("Rechazar".equals(dto.getDecision())) {
            solicitud.setEstado("rechazada_cliente");
            return toResponseDTOConEntidades(solicitudRepository.update(solicitud));
        }

        // Aprobar: verificar si el cliente tiene cupo propio disponible para reasignar
        Cliente cliente = clienteRepository.findById(solicitud.getIdUsuarioCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        BigDecimal disponible = clienteRepository.calcularSaldoPropioDisponible(cliente.getIdUsuario());

        if (disponible.compareTo(solicitud.getMontoSolicitado()) >= 0) {
            // Cliente tiene cupo propio: aprueba directamente sin supervisor.
            // Se reasigna dinero ya autorizado: cupo_propio baja, cupo_asignado de la pareja sube
            // en la misma proporcion, el total del cliente no cambia.
            Pareja pareja = parejaRepository.findById(solicitud.getIdUsuarioPareja())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Pareja no encontrada"));

            cliente.setCupoPropio(cliente.getCupoPropio().subtract(solicitud.getMontoSolicitado()));
            clienteRepository.update(cliente);

            BigDecimal nuevoCupoPareja = pareja.getCupoAsignado().add(solicitud.getMontoSolicitado());
            parejaRepository.actualizarCupoAsignado(pareja.getIdUsuario(), nuevoCupoPareja);

            solicitud.setEstado("aprobada_directa");
            solicitud.setMontoAutorizado(solicitud.getMontoSolicitado());
            return toResponseDTOConEntidades(solicitudRepository.update(solicitud));

        } else {
            // Cliente no tiene cupo propio suficiente: escalar a cualquier supervisor activo
            Supervisor supervisor = supervisorRepository.findAll().stream()
                    .filter(sv -> "Activo".equals(sv.getEstado()))
                    .findFirst()
                    .orElseThrow(() -> new ReglaNegocioException(
                            "No hay supervisores activos para gestionar el sobrecupo"));

            solicitud.setEstado("pendiente_supervisor");
            solicitud.setIdUsuarioSupervisor(supervisor.getIdUsuario());
            return toResponseDTOConEntidades(solicitudRepository.update(solicitud));
        }
    }

    @Override
    @Transactional
    public SolicitudSobrecupoResponseDTO decidirComoSupervisor(Long codSolicitud, DecisionSolicitudDTO dto) {
        SolicitudSobrecupo solicitud = buscarOLanzarError(codSolicitud);

        if (!"pendiente_supervisor".equals(solicitud.getEstado())) {
            throw new ReglaNegocioException(
                    "Solo se puede decidir sobre solicitudes en estado 'pendiente_supervisor'. Estado actual: "
                            + solicitud.getEstado());
        }

        if ("Rechazar".equals(dto.getDecision())) {
            solicitud.setEstado("rechazada_supervisor");
            return toResponseDTOConEntidades(solicitudRepository.update(solicitud));
        }

        // Supervisor aprueba: es dinero nuevo, solo aumenta el cupo asignado de la pareja.
        // No se toca el cupo_propio del cliente.
        Pareja pareja = parejaRepository.findById(solicitud.getIdUsuarioPareja())
                .orElseThrow(() -> new RecursoNoEncontradoException("Pareja no encontrada"));

        BigDecimal monto = solicitud.getMontoSolicitado();
        parejaRepository.actualizarCupoAsignado(pareja.getIdUsuario(), pareja.getCupoAsignado().add(monto));

        solicitud.setEstado("aprobada_supervisor");
        solicitud.setMontoAutorizado(monto);
        return toResponseDTOConEntidades(solicitudRepository.update(solicitud));
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

        return toResponseDTO(s, cliente, pareja, supervisor);
    }

    private SolicitudSobrecupoResponseDTO toResponseDTO(SolicitudSobrecupo s, Cliente cliente,
                                                        Pareja pareja, Supervisor supervisor) {
        SolicitudSobrecupoResponseDTO dto = new SolicitudSobrecupoResponseDTO();
        dto.setCodSolicitud(s.getCodSolicitud());
        dto.setFecha(s.getFecha());
        dto.setHora(s.getHora());
        dto.setMontoSolicitado(s.getMontoSolicitado());
        dto.setMontoAutorizado(s.getMontoAutorizado());
        dto.setEstado(s.getEstado());

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
        return dto;
    }
}
