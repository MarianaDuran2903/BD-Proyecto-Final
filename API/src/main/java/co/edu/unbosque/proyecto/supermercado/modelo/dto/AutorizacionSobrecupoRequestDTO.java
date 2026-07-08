package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Datos que el supervisor envía en el punto de venta para registrar
 * una autorización de sobrecupo. fecha y hora no se incluyen: las
 * asigna el servidor en el momento de guardar (LocalDate.now() /
 * LocalTime.now()), para evitar que se manipulen desde el cliente.
 */
public class AutorizacionSobrecupoRequestDTO {

    @NotNull(message = "Debe indicar la compra que originó el sobrecupo")
    private Long idCompra;

    @NotNull(message = "Debe indicar el cliente titular que autoriza")
    private Long idUsuarioCliente;

    @NotNull(message = "Debe indicar el supervisor que aprueba")
    private Long idUsuarioSupervisor;

    @NotNull(message = "El monto autorizado es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto autorizado debe ser mayor a cero")
    private BigDecimal montoAutorizado;

    public AutorizacionSobrecupoRequestDTO() {
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Long getIdUsuarioCliente() {
        return idUsuarioCliente;
    }

    public void setIdUsuarioCliente(Long idUsuarioCliente) {
        this.idUsuarioCliente = idUsuarioCliente;
    }

    public Long getIdUsuarioSupervisor() {
        return idUsuarioSupervisor;
    }

    public void setIdUsuarioSupervisor(Long idUsuarioSupervisor) {
        this.idUsuarioSupervisor = idUsuarioSupervisor;
    }

    public BigDecimal getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(BigDecimal montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
    }
}
