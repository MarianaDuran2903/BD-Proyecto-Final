package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class CompraResponseDTO {

    private Long codCompra;
    private BigDecimal monto;
    private LocalDate fecha;
    private LocalTime hora;

    private Long idUsuarioPareja;
    private String nombreParejaCompleto;

    private Long idUsuarioCliente;
    private String nombreClienteCompleto;

    private Long idAlmacen;
    private String nombreAlmacen;

    private Long idUsuarioSupervisor;
    private String nombreSupervisorCompleto;

    public CompraResponseDTO() {
    }

    public Long getCodCompra() {
        return codCompra;
    }

    public void setCodCompra(Long codCompra) {
        this.codCompra = codCompra;
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

    public String getNombreParejaCompleto() {
        return nombreParejaCompleto;
    }

    public void setNombreParejaCompleto(String nombreParejaCompleto) {
        this.nombreParejaCompleto = nombreParejaCompleto;
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
