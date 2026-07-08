package co.edu.unbosque.proyecto.supermercado.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Pareja asociada a un Cliente titular, con su propio cupo de crédito
 * individual. Es subtipo de Usuario.
 */
@Entity
@Table(name = "pareja")
public class Pareja extends Usuario {

    @Column(name = "cedula", length = 30, nullable = false, unique = true)
    private String cedula;

    @Column(name = "primer_nombre", length = 30, nullable = false)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 30)
    private String segundoNombre;

    @Column(name = "primer_apellido", length = 30, nullable = false)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 30)
    private String segundoApellido;

    @Column(name = "cupo_asignado", nullable = false)
    private BigDecimal cupoAsignado;

    // Lado dueño de la relación Poseer (Pareja N - 1 Cliente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_cliente", nullable = false)
    private Cliente cliente;

    // Lado inverso de la relación Realizar (Pareja 1 - N Compra)
    @OneToMany(mappedBy = "pareja", cascade = CascadeType.ALL)
    private List<Compra> compras = new ArrayList<>();

    // Lado inverso de la relación Tener (Pareja 1 - N RestriccionHorario)
    @OneToMany(mappedBy = "pareja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestriccionHorario> restricciones = new ArrayList<>();

    public Pareja() {
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public BigDecimal getCupoAsignado() {
        return cupoAsignado;
    }

    public void setCupoAsignado(BigDecimal cupoAsignado) {
        this.cupoAsignado = cupoAsignado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public List<RestriccionHorario> getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(List<RestriccionHorario> restricciones) {
        this.restricciones = restricciones;
    }
}