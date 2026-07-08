package co.edu.unbosque.proyecto.supermercado.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Almacén / punto de venta de la cadena de supermercados.
 */
@Entity
@Table(name = "almacen")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_almacen")
    private Long idAlmacen;

    @Column(name = "nombre_almacen", length = 30, nullable = false)
    private String nombreAlmacen;

    @Column(name = "ubicacion_ciudad", length = 30, nullable = false)
    private String ubicacionCiudad;

    @Column(name = "ubicacion_avenida", length = 30)
    private String ubicacionAvenida;

    @Column(name = "ubicacion_calle", length = 30)
    private String ubicacionCalle;

    // Lado inverso de la relación Trabajar (Almacen 1 - N Supervisor)
    @OneToMany(mappedBy = "almacen")
    private List<Supervisor> supervisores = new ArrayList<>();

    // Lado inverso de la relación Efectuar (Almacen 1 - N Compra)
    @OneToMany(mappedBy = "almacen")
    private List<Compra> compras = new ArrayList<>();

    public Almacen() {
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

    public String getUbicacionCiudad() {
        return ubicacionCiudad;
    }

    public void setUbicacionCiudad(String ubicacionCiudad) {
        this.ubicacionCiudad = ubicacionCiudad;
    }

    public String getUbicacionAvenida() {
        return ubicacionAvenida;
    }

    public void setUbicacionAvenida(String ubicacionAvenida) {
        this.ubicacionAvenida = ubicacionAvenida;
    }

    public String getUbicacionCalle() {
        return ubicacionCalle;
    }

    public void setUbicacionCalle(String ubicacionCalle) {
        this.ubicacionCalle = ubicacionCalle;
    }

    public List<Supervisor> getSupervisores() {
        return supervisores;
    }

    public void setSupervisores(List<Supervisor> supervisores) {
        this.supervisores = supervisores;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }
}