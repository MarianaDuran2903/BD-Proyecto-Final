package co.edu.unbosque.proyecto.supermercado.modelo.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos que el cliente del API envía para registrar o actualizar un
 * Supervisor. idAlmacen es el id del almacén donde trabaja (se envía
 * como id simple, no como objeto completo).
 */
public class SupervisorRequestDTO {

    @NotBlank
    @Size(max = 30)
    private String nombreUsuario;

    @NotBlank
    @Size(max = 30)
    private String contrasenia;

    @NotBlank
    @Size(max = 30)
    private String cedula;

    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 30)
    private String correo;

    @Size(max = 30)
    private String telefono;

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

    @NotNull(message = "Debe indicar el almacén donde trabaja")
    private Long idAlmacen;

    public SupervisorRequestDTO() {
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

    public Long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }
}
