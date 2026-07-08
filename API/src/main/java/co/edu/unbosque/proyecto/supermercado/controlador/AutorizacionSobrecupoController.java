package co.edu.unbosque.proyecto.supermercado.controlador;

import co.edu.unbosque.proyecto.supermercado.modelo.dto.AutorizacionSobrecupoRequestDTO;
import co.edu.unbosque.proyecto.supermercado.modelo.dto.AutorizacionSobrecupoResponseDTO;
import co.edu.unbosque.proyecto.supermercado.servicio.AutorizacionSobrecupoService;
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
@RequestMapping("/api/autorizaciones-sobrecupo")
public class AutorizacionSobrecupoController {

    private final AutorizacionSobrecupoService autorizacionSobrecupoService;

    public AutorizacionSobrecupoController(AutorizacionSobrecupoService autorizacionSobrecupoService) {
        this.autorizacionSobrecupoService = autorizacionSobrecupoService;
    }

    // El supervisor usa este endpoint en el punto de venta para aprobar
    // el sobrecupo de una compra, con la autorización del cliente titular.
    @PostMapping
    public ResponseEntity<AutorizacionSobrecupoResponseDTO> crear(
            @Valid @RequestBody AutorizacionSobrecupoRequestDTO dto) {
        AutorizacionSobrecupoResponseDTO creada = autorizacionSobrecupoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/compra/{idCompra}")
    public ResponseEntity<AutorizacionSobrecupoResponseDTO> obtenerPorCompra(@PathVariable Long idCompra) {
        return ResponseEntity.ok(autorizacionSobrecupoService.obtenerPorCompra(idCompra));
    }
}