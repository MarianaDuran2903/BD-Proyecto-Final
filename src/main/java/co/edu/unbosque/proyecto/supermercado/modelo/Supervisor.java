package co.edu.unbosque.proyecto.supermercado.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Supervisor de un almacén, encargado de aprobar sobrecupos en el
 * punto de venta. Es subtipo de Usuario.
 */
@Entity
@Table(name = "supervisor")
public class Supervisor extends Usuario {

    @Column(name = "cedula", length = 30, nullable = false, unique = true)
    private String cedula;

    @Column(name = "correo", length = 30)
    private String correo;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "primer_nombre", length = 30, nullable = false)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 30)
    private String segundoNombre;

    @Column(name = "primer_apellido", length = 30, nullable = false)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 30)
    private String segundoApellido;

    // Lado dueño de la relación Trabajar (Supervisor N - 1 Almacen)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    // Lado inverso de la relación Autorizar (Supervisor 1 - N Compra)
    // Relación directa Compra-Supervisor, pendiente de revisión con el equipo
    @OneToMany(mappedBy = "supervisor")
    private List<Compra> comprasAutorizadas = new ArrayList<>();

    // Lado inverso de la relación Aprobar (Supervisor 1 - N AutorizacionSobrecupo)
    @OneToMany(mappedBy = "supervisor")
    private List<AutorizacionSobrecupo> autorizacionesAprobadas = new ArrayList<>();

    public Supervisor() {
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public List<Compra> getComprasAutorizadas() {
        return comprasAutorizadas;
    }

    public void setComprasAutorizadas(List<Compra> comprasAutorizadas) {
        this.comprasAutorizadas = comprasAutorizadas;
    }

    public List<AutorizacionSobrecupo> getAutorizacionesAprobadas() {
        return autorizacionesAprobadas;
    }

    public void setAutorizacionesAprobadas(List<AutorizacionSobrecupo> autorizacionesAprobadas) {
        this.autorizacionesAprobadas = autorizacionesAprobadas;
    }
}