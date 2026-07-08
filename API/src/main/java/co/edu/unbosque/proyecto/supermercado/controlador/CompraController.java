package co.edu.unbosque.proyecto.supermercado.controlador;
import java.util.List;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.CompraResponseDTO;
import co.edu.unbosque.proyecto.supermercado.servicio.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    // Registra la compra. Si el monto excede el cupo disponible de la
    // pareja, la compra igual se guarda pero queda marcada con
    // requiereSobrecupo = true (ver AutorizacionSobrecupoController
    // para el siguiente paso del flujo).
    @PostMapping
    public ResponseEntity<CompraResponseDTO> registrar(@Valid @RequestBody CompraRequestDTO dto) {
        CompraResponseDTO creada = compraService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(compraService.obtenerPorId(id));
    }

    // Historial de compras de una pareja (relación Realizar)
    @GetMapping("/pareja/{idUsuarioPareja}")
    public ResponseEntity<List<CompraResponseDTO>> listarPorPareja(@PathVariable Long idUsuarioPareja) {
        return ResponseEntity.ok(compraService.listarPorPareja(idUsuarioPareja));
    }
}
