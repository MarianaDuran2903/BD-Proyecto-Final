package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

// DTO dedicado para que el propio Cliente edite su cupo_propio, sin tener
// que reenviar contrasenia ni el resto de sus datos (a diferencia de
// ClienteRequestDTO, que se usa para el PUT general /api/clientes/{id}).
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
