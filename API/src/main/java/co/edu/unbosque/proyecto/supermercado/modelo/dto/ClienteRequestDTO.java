package co.edu.unbosque.proyecto.supermercado.modelo.dto;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos que el cliente del API envía para registrar o actualizar un Cliente.
 * Incluye la contrasenia porque es lo único que se recibe en texto plano
 * al momento de crear la cuenta (nunca se devuelve en el Response).
 */
public class ClienteRequestDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 30)
    private String nombreUsuario;

    @NotBlank(message = "La contrasenia es obligatoria")
    @Size(max = 30)
    private String contrasenia;

    @NotBlank(message = "La cedula es obligatoria")
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

    @Size(max = 30)
    private String telefono;

    @NotNull(message = "El cupo total autorizado es obligatorio")
    @DecimalMin(value = "0.0", message = "El cupo total autorizado no puede ser negativo")
    private BigDecimal cupoTotalAutorizado;

    public ClienteRequestDTO() {
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getCupoTotalAutorizado() {
        return cupoTotalAutorizado;
    }

    public void setCupoTotalAutorizado(BigDecimal cupoTotalAutorizado) {
        this.cupoTotalAutorizado = cupoTotalAutorizado;
    }
}