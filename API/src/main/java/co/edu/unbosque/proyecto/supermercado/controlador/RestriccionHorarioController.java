package co.edu.unbosque.proyecto.supermercado.controlador;

import java.util.List;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.RestriccionHorarioDTO;
import co.edu.unbosque.proyecto.supermercado.servicio.RestriccionHorarioService;
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
@RequestMapping("/api/restricciones-horario")
public class RestriccionHorarioController {

    private final RestriccionHorarioService restriccionHorarioService;

    public RestriccionHorarioController(RestriccionHorarioService restriccionHorarioService) {
        this.restriccionHorarioService = restriccionHorarioService;
    }

    @PostMapping
    public ResponseEntity<RestriccionHorarioDTO> crear(@Valid @RequestBody RestriccionHorarioDTO dto) {
        RestriccionHorarioDTO creada = restriccionHorarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestriccionHorarioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restriccionHorarioService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<RestriccionHorarioDTO>> listarTodos() {
        return ResponseEntity.ok(restriccionHorarioService.listarTodos());
    }

    @GetMapping("/pareja/{idUsuarioPareja}")
    public ResponseEntity<List<RestriccionHorarioDTO>> listarPorPareja(@PathVariable Long idUsuarioPareja) {
        return ResponseEntity.ok(restriccionHorarioService.listarPorPareja(idUsuarioPareja));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestriccionHorarioDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody RestriccionHorarioDTO dto) {
        return ResponseEntity.ok(restriccionHorarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        restriccionHorarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
