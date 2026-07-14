package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class EditarCupoPropioDTO {

    @NotNull(message = "El cupo propio es obligatorio")
    @DecimalMin(value = "0.0", message = "El cupo propio no puede ser negativo")
    private BigDecimal cupoPropio;

    public EditarCupoPropioDTO() {
    }

    public BigDecimal getCupoPropio() {
        return cupoPropio;
    }

    public void setCupoPropio(BigDecimal cupoPropio) {
        this.cupoPropio = cupoPropio;
    }
}
