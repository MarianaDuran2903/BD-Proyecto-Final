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

    @PostMapping
    public ResponseEntity<CompraResponseDTO> registrar(@Valid @RequestBody CompraRequestDTO dto) {
        CompraResponseDTO creada = compraService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(compraService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> listarTodos() {
        return ResponseEntity.ok(compraService.listarTodos());
    }

    @GetMapping("/pareja/{idUsuarioPareja}")
    public ResponseEntity<List<CompraResponseDTO>> listarPorPareja(@PathVariable Long idUsuarioPareja) {
        return ResponseEntity.ok(compraService.listarPorPareja(idUsuarioPareja));
    }

    @GetMapping("/cliente/{idUsuarioCliente}")
    public ResponseEntity<List<CompraResponseDTO>> listarPorCliente(@PathVariable Long idUsuarioCliente) {
        return ResponseEntity.ok(compraService.listarPorCliente(idUsuarioCliente));
    }
}
