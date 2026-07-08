package co.edu.unbosque.proyecto.supermercado.modelo.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Datos que el punto de venta envía para registrar una Compra.
 * Solo se envían los ids de Pareja y Almacen (no los objetos completos).
 * requiereSobrecupo e idUsuarioSupervisor no se incluyen aquí: los
 * calcula/asigna el servidor según la lógica de negocio (si el monto
 * supera el cupo disponible de la pareja).
 */
public class CompraRequestDTO {

    @NotNull(message = "El monto de la compra es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotNull(message = "Debe indicar la pareja que realiza la compra")
    private Long idUsuarioPareja;

    @NotNull(message = "Debe indicar el almacén donde se efectúa la compra")
    private Long idAlmacen;

    public CompraRequestDTO() {
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Long getIdUsuarioPareja() {
        return idUsuarioPareja;
    }

    public void setIdUsuarioPareja(Long idUsuarioPareja) {
        this.idUsuarioPareja = idUsuarioPareja;
    }

    public Long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }
}
