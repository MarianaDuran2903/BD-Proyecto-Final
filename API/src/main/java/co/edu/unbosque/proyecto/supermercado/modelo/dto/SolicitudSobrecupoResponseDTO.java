package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class SolicitudSobrecupoResponseDTO {

    private Long codSolicitud;
    private LocalDate fecha;
    private LocalTime hora;
    private BigDecimal montoSolicitado;
    private BigDecimal montoAutorizado;  // null si todavia no fue aprobada
    private String estado;
    private Long idCompra;               // null hasta que la compra se registre

    private Long idUsuarioCliente;
    private String nombreClienteCompleto;

    private Long idUsuarioPareja;
    private String nombreParejaCompleto;

    private Long idUsuarioSupervisor;    // null si no intervino supervisor
    private String nombreSupervisorCompleto;

    private Long idAlmacen;
    private String nombreAlmacen;

    public SolicitudSobrecupoResponseDTO() {
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

    public Long getIdUsuarioPareja() {
        return idUsuarioPareja;
    }

    public void setIdUsuarioPareja(Long idUsuarioPareja) {
        this.idUsuarioPareja = idUsuarioPareja;
    }

    public String getNombreParejaCompleto() {
        return nombreParejaCompleto;
    }

    public void setNombreParejaCompleto(String nombreParejaCompleto) {
        this.nombreParejaCompleto = nombreParejaCompleto;
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

    public Long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }
}
