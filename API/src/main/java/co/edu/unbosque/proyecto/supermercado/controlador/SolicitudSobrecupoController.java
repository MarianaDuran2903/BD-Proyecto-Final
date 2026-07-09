package co.edu.unbosque.proyecto.supermercado.controlador;

import java.util.List;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.DecisionSolicitudDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SolicitudSobrecupoRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SolicitudSobrecupoResponseDTO;
import co.edu.unbosque.proyecto.supermercado.servicio.SolicitudSobrecupoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/solicitudes-sobrecupo")
public class SolicitudSobrecupoController {

    private final SolicitudSobrecupoService solicitudService;

    public SolicitudSobrecupoController(SolicitudSobrecupoService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudSobrecupoResponseDTO> crear(
            @Valid @RequestBody SolicitudSobrecupoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.crear(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudSobrecupoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudSobrecupoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(solicitudService.listarTodos());
    }

    @GetMapping("/cliente/{idUsuarioCliente}")
    public ResponseEntity<List<SolicitudSobrecupoResponseDTO>> listarPorCliente(
            @PathVariable Long idUsuarioCliente) {
        return ResponseEntity.ok(solicitudService.listarPorCliente(idUsuarioCliente));
    }

    @GetMapping("/pareja/{idUsuarioPareja}")
    public ResponseEntity<List<SolicitudSobrecupoResponseDTO>> listarPorPareja(
            @PathVariable Long idUsuarioPareja) {
        return ResponseEntity.ok(solicitudService.listarPorPareja(idUsuarioPareja));
    }

    @GetMapping("/pendientes-cliente/{idUsuarioCliente}")
    public ResponseEntity<List<SolicitudSobrecupoResponseDTO>> listarPendientesPorCliente(
            @PathVariable Long idUsuarioCliente) {
        return ResponseEntity.ok(solicitudService.listarPendientesPorCliente(idUsuarioCliente));
    }

    @GetMapping("/pendientes-supervisor/{idUsuarioSupervisor}")
    public ResponseEntity<List<SolicitudSobrecupoResponseDTO>> listarPendientesPorSupervisor(
            @PathVariable Long idUsuarioSupervisor) {
        return ResponseEntity.ok(solicitudService.listarPendientesPorSupervisor(idUsuarioSupervisor));
    }

    @PutMapping("/{id}/decision-cliente")
    public ResponseEntity<SolicitudSobrecupoResponseDTO> decidirComoCliente(
            @PathVariable Long id, @Valid @RequestBody DecisionSolicitudDTO dto) {
        return ResponseEntity.ok(solicitudService.decidirComoCliente(id, dto));
    }

    @PutMapping("/{id}/decision-supervisor")
    public ResponseEntity<SolicitudSobrecupoResponseDTO> decidirComoSupervisor(
            @PathVariable Long id, @Valid @RequestBody DecisionSolicitudDTO dto) {
        return ResponseEntity.ok(solicitudService.decidirComoSupervisor(id, dto));
    }
}
