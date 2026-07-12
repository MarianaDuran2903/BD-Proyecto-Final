package co.edu.unbosque.proyecto.supermercado.modelo;

import java.math.BigDecimal;

public class Cliente extends Usuario {

    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;
    private BigDecimal cupoPropio;
    private BigDecimal cupoTotalSolicitado;
    private BigDecimal cupoTotalAutorizado;

    public Cliente() {
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getCupoPropio() {
        return cupoPropio;
    }

    public void setCupoPropio(BigDecimal cupoPropio) {
        this.cupoPropio = cupoPropio;
    }

    public BigDecimal getCupoTotalSolicitado() {
        return cupoTotalSolicitado;
    }

    public void setCupoTotalSolicitado(BigDecimal cupoTotalSolicitado) {
        this.cupoTotalSolicitado = cupoTotalSolicitado;
    }

    public BigDecimal getCupoTotalAutorizado() {
        return cupoTotalAutorizado;
    }

    public void setCupoTotalAutorizado(BigDecimal cupoTotalAutorizado) {
        this.cupoTotalAutorizado = cupoTotalAutorizado;
    }
}
