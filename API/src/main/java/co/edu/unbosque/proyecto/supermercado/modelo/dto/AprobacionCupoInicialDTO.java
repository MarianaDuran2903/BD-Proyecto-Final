package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

// DTO que usa el Supervisor para aprobar (o editar) el cupo inicial
// de un Cliente en estado 'Pendiente'. cupoAutorizado puede ser $0
// (equivale a "casi-rechazar", no hay un estado de rechazo aparte).
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
