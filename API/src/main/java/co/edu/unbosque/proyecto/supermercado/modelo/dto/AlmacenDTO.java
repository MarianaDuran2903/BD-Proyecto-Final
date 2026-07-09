package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AlmacenDTO {

    private Long idAlmacen;

    @NotBlank
    @Size(max = 30)
    private String nombreAlmacen;

    @NotBlank
    @Size(max = 30)
    private String ubicacionCiudad;

    @Size(max = 30)
    private String ubicacionAvenida;

    @Size(max = 30)
    private String ubicacionCalle;

    public AlmacenDTO() {
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