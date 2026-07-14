package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClienteRegistroRequestDTO {

    @NotNull(message = "La cedula (id de usuario) es obligatoria")
    private Long idUsuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 30)
    private String nombreUsuario;

    @NotBlank(message = "La contrasenia es obligatoria")
    @Size(max = 30)
    private String contrasenia;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 30)
    private String primerNombre;

    @Size(max = 30)
    private String segundoNombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 30)
    private String primerApellido;

    @Size(max = 30)
    private String segundoApellido;

    @Size(max = 30)
    private String telefono;

    @NotNull(message = "El cupo total solicitado es obligatorio")
    @DecimalMin(value = "0.0", message = "El cupo total solicitado no puede ser negativo")
    private BigDecimal cupoTotalSolicitado;

    public ClienteRegistroRequestDTO() {
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
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

    public BigDecimal getCupoTotalSolicitado() {
        return cupoTotalSolicitado;
    }

    public void setCupoTotalSolicitado(BigDecimal cupoTotalSolicitado) {
        this.cupoTotalSolicitado = cupoTotalSolicitado;
    }
}
