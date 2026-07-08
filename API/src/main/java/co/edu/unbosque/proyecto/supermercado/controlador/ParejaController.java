package co.edu.unbosque.proyecto.supermercado.controlador;

import java.util.List;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.ParejaRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.ParejaResponseDTO;
import co.edu.unbosque.proyecto.supermercado.servicio.ParejaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/parejas")
public class ParejaController {

    private final ParejaService parejaService;

    public ParejaController(ParejaService parejaService) {
        this.parejaService = parejaService;
    }

    @PostMapping
    public ResponseEntity<ParejaResponseDTO> crear(@Valid @RequestBody ParejaRequestDTO dto) {
        ParejaResponseDTO creada = parejaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParejaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(parejaService.obtenerPorId(id));
    }

    // Todas las parejas de un cliente titular (relación Poseer)
    @GetMapping("/cliente/{idUsuarioCliente}")
    public ResponseEntity<List<ParejaResponseDTO>> listarPorCliente(@PathVariable Long idUsuarioCliente) {
        return ResponseEntity.ok(parejaService.listarPorCliente(idUsuarioCliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParejaResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody ParejaRequestDTO dto) {
        return ResponseEntity.ok(parejaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        parejaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}