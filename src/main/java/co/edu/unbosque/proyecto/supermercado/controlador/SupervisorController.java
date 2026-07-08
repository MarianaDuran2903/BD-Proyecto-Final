package co.edu.unbosque.proyecto.supermercado.controlador;

import java.util.List;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.SupervisorRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.SupervisorResponseDTO;
import co.edu.unbosque.proyecto.supermercado.servicio.SupervisorService;
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
@RequestMapping("/api/supervisores")
public class SupervisorController {

    private final SupervisorService supervisorService;

    public SupervisorController(SupervisorService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @PostMapping
    public ResponseEntity<SupervisorResponseDTO> crear(@Valid @RequestBody SupervisorRequestDTO dto) {
        SupervisorResponseDTO creado = supervisorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupervisorResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(supervisorService.obtenerPorId(id));
    }

    // Todos los supervisores que trabajan en un almacén (relación Trabajar)
    @GetMapping("/almacen/{idAlmacen}")
    public ResponseEntity<List<SupervisorResponseDTO>> listarPorAlmacen(@PathVariable Long idAlmacen) {
        return ResponseEntity.ok(supervisorService.listarPorAlmacen(idAlmacen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupervisorResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody SupervisorRequestDTO dto) {
        return ResponseEntity.ok(supervisorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        supervisorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
