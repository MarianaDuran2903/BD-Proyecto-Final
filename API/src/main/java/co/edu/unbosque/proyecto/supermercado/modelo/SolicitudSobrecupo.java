package co.edu.unbosque.proyecto.supermercado.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class SolicitudSobrecupo {

    private Long codSolicitud;
    private LocalDate fecha;
    private LocalTime hora;
    private BigDecimal montoSolicitado;
    private BigDecimal montoAutorizado;  // null mientras este pendiente_cliente
    // pendiente_cliente | aprobada_directa | pendiente_supervisor | aprobada_supervisor | rechazada_cliente | rechazada_supervisor
    private String estado;
    private Long idUsuarioCliente;
    private Long idUsuarioPareja;
    private Long idUsuarioSupervisor;    // null si el cliente aprueba directamente

    public SolicitudSobrecupo() {
    }

    public Long getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(Long codSolicitud) {
        this.codSolicitud = codSolicitud;
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

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public BigDecimal getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(BigDecimal montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdUsuarioCliente() {
        return idUsuarioCliente;
    }

    public void setIdUsuarioCliente(Long idUsuarioCliente) {
        this.idUsuarioCliente = idUsuarioCliente;
    }

    public Long getIdUsuarioPareja() {
        return idUsuarioPareja;
    }

    public void setIdUsuarioPareja(Long idUsuarioPareja) {
        this.idUsuarioPareja = idUsuarioPareja;
    }

    public Long getIdUsuarioSupervisor() {
        return idUsuarioSupervisor;
    }

    public void setIdUsuarioSupervisor(Long idUsuarioSupervisor) {
        this.idUsuarioSupervisor = idUsuarioSupervisor;
    }
}
