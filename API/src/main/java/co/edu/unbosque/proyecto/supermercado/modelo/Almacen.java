package co.edu.unbosque.proyecto.supermercado.modelo;

public class Almacen {

    private Long idAlmacen;
    private String nombreAlmacen;
    private String ubicacionCiudad;
    private String ubicacionAvenida;
    private String ubicacionCalle;

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
}
