package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AprobacionCupoInicialDTO {

    @NotNull
    @DecimalMin(value = "0", message = "El cupo autorizado no puede ser negativo")
    private BigDecimal cupoAutorizado;

    public AprobacionCupoInicialDTO() {
    }

    public BigDecimal getCupoAutorizado() {
        return cupoAutorizado;
    }

    public void setCupoAutorizado(BigDecimal cupoAutorizado) {
        this.cupoAutorizado = cupoAutorizado;
    }
}
