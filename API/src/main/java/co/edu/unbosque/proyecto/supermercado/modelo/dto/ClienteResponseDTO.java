package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

public class ClienteResponseDTO {

    private Long idUsuario;
    private String nombreUsuario;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;
    private BigDecimal cupoPropio;
    private BigDecimal saldoReal;
    private BigDecimal cupoTotalAutorizado;
    private BigDecimal cupoTotalSolicitado;
    private BigDecimal cupoAsignadoParejas;
    private BigDecimal cupoTotalDisponible;
    private String estado;

    public ClienteResponseDTO() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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

    public BigDecimal getSaldoReal() {
        return saldoReal;
    }

    public void setSaldoReal(BigDecimal saldoReal) {
        this.saldoReal = saldoReal;
    }

    public BigDecimal getCupoTotalAutorizado() {
        return cupoTotalAutorizado;
    }

    public void setCupoTotalAutorizado(BigDecimal cupoTotalAutorizado) {
        this.cupoTotalAutorizado = cupoTotalAutorizado;
    }

    public BigDecimal getCupoTotalSolicitado() {
        return cupoTotalSolicitado;
    }

    public void setCupoTotalSolicitado(BigDecimal cupoTotalSolicitado) {
        this.cupoTotalSolicitado = cupoTotalSolicitado;
    }

    public BigDecimal getCupoAsignadoParejas() {
        return cupoAsignadoParejas;
    }

    public void setCupoAsignadoParejas(BigDecimal cupoAsignadoParejas) {
        this.cupoAsignadoParejas = cupoAsignadoParejas;
    }

    public BigDecimal getCupoTotalDisponible() {
        return cupoTotalDisponible;
    }

    public void setCupoTotalDisponible(BigDecimal cupoTotalDisponible) {
        this.cupoTotalDisponible = cupoTotalDisponible;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
