package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Datos que se devuelven al consultar una AutorizacionSobrecupo.
 * Trae los nombres ya resueltos de cliente y supervisor, para que
 * el frontend no tenga que hacer consultas adicionales.
 */
public class AutorizacionSobrecupoResponseDTO {

    private Long idAutorizacion;
    private LocalDate fecha;
    private LocalTime hora;
    private BigDecimal montoAutorizado;

    private Long idCompra;

    private Long idUsuarioCliente;
    private String nombreClienteCompleto;

    private Long idUsuarioSupervisor;
    private String nombreSupervisorCompleto;

    public AutorizacionSobrecupoResponseDTO() {
    }

    public Long getIdAutorizacion() {
        return idAutorizacion;
    }

    public void setIdAutorizacion(Long idAutorizacion) {
        this.idAutorizacion = idAutorizacion;
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

    public BigDecimal getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(BigDecimal montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
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

    public String getNombreClienteCompleto() {
        return nombreClienteCompleto;
    }

    public void setNombreClienteCompleto(String nombreClienteCompleto) {
        this.nombreClienteCompleto = nombreClienteCompleto;
    }

    public Long getIdUsuarioSupervisor() {
        return idUsuarioSupervisor;
    }

    public void setIdUsuarioSupervisor(Long idUsuarioSupervisor) {
        this.idUsuarioSupervisor = idUsuarioSupervisor;
    }

    public String getNombreSupervisorCompleto() {
        return nombreSupervisorCompleto;
    }

    public void setNombreSupervisorCompleto(String nombreSupervisorCompleto) {
        this.nombreSupervisorCompleto = nombreSupervisorCompleto;
    }
}
