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
 * Entidad reificada que registra el evento de autorización de un
 * sobrecupo: la compra que lo generó, el cliente titular que autoriza
 * y el supervisor que aprueba en el punto de venta.
 */
@Entity
@Table(name = "autorizacion_sobrecupo")
public class AutorizacionSobrecupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autorizacion")
    private Long idAutorizacion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "monto_autorizado", nullable = false)
    private BigDecimal montoAutorizado;

    // Lado dueño de la relación Tener (AutorizacionSobrecupo 1 - 1 Compra)
    // unique = true refleja que cada compra tiene a lo sumo una autorización.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compra", nullable = false, unique = true)
    private Compra compra;

    // Lado dueño de la relación Autorizar (AutorizacionSobrecupo N - 1 Cliente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_cliente", nullable = false)
    private Cliente cliente;

    // Lado dueño de la relación Aprobar (AutorizacionSobrecupo N - 1 Supervisor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_supervisor", nullable = false)
    private Supervisor supervisor;

    public AutorizacionSobrecupo() {
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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }
}