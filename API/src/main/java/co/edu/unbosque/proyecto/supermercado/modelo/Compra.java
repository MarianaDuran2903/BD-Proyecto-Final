package co.edu.unbosque.proyecto.supermercado.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Registro detallado de cada compra realizada por una Pareja:
 * monto, fecha, hora y almacén donde se efectuó.
 */
@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "requiere_sobrecupo", nullable = false)
    private Boolean requiereSobrecupo = Boolean.FALSE;

    // Lado dueño de la relación Realizar (Compra N - 1 Pareja)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_pareja", nullable = false)
    private Pareja pareja;

    // Lado dueño de la relación Efectuar (Compra N - 1 Almacen)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    // Lado dueño de la relación Autorizar directa (Compra N - 1 Supervisor)
    // Cardinalidad (0,1): una compra puede no tener supervisor asociado.
    // Pendiente de revisión con el equipo por posible redundancia con
    // AutorizacionSobrecupo.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_supervisor")
    private Supervisor supervisor;

    // Lado inverso de la relación Tener (Compra 1 - 0..1 AutorizacionSobrecupo)
    @OneToOne(mappedBy = "compra")
    private AutorizacionSobrecupo autorizacionSobrecupo;

    public Compra() {
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
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

    public Pareja getPareja() {
        return pareja;
    }

    public void setPareja(Pareja pareja) {
        this.pareja = pareja;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public AutorizacionSobrecupo getAutorizacionSobrecupo() {
        return autorizacionSobrecupo;
    }

    public void setAutorizacionSobrecupo(AutorizacionSobrecupo autorizacionSobrecupo) {
        this.autorizacionSobrecupo = autorizacionSobrecupo;
    }
}