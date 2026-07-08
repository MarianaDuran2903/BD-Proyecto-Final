package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos que el cliente del API envía para registrar o actualizar una
 * Pareja. idUsuarioCliente es el id del Cliente titular al que
 * pertenece (se envía como id simple, no como objeto completo).
 */
public class ParejaRequestDTO {

    @NotBlank
    @Size(max = 30)
    private String nombreUsuario;

    @NotBlank
    @Size(max = 30)
    private String contrasenia;

    @NotBlank
    @Size(max = 30)
    private String cedula;

    @NotBlank
    @Size(max = 30)
    private String primerNombre;

    @Size(max = 30)
    private String segundoNombre;

    @NotBlank
    @Size(max = 30)
    private String primerApellido;

    @Size(max = 30)
    private String segundoApellido;

    @NotNull(message = "El cupo asignado es obligatorio")
    @DecimalMin(value = "0.0", message = "El cupo asignado no puede ser negativo")
    private BigDecimal cupoAsignado;

    @NotNull(message = "Debe indicar el cliente titular")
    private Long idUsuarioCliente;

    public ParejaRequestDTO() {
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
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

    public Long getIdUsuarioCliente() {
        return idUsuarioCliente;
    }

    public void setIdUsuarioCliente(Long idUsuarioCliente) {
        this.idUsuarioCliente = idUsuarioCliente;
    }
}
