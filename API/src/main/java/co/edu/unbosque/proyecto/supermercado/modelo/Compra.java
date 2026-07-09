package co.edu.unbosque.proyecto.supermercado.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Compra {

    private Long codCompra;
    private BigDecimal monto;
    private LocalDate fecha;
    private LocalTime hora;
    private Boolean requiereSobrecupo;
    private Long idUsuarioPareja;
    private Long idAlmacen;
    private Long idUsuarioSupervisor;

    public Compra() {
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

    public Boolean getRequiereSobrecupo() {
        return requiereSobrecupo;
    }

    public void setRequiereSobrecupo(Boolean requiereSobrecupo) {
        this.requiereSobrecupo = requiereSobrecupo;
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

    public Long getIdUsuarioSupervisor() {
        return idUsuarioSupervisor;
    }

    public void setIdUsuarioSupervisor(Long idUsuarioSupervisor) {
        this.idUsuarioSupervisor = idUsuarioSupervisor;
    }
}
