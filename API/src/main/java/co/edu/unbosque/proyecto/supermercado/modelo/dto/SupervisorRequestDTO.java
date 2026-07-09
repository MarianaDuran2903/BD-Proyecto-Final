package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SupervisorRequestDTO {

    @NotNull(message = "La cedula (id de usuario) es obligatoria")
    private Long idUsuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 30)
    private String nombreUsuario;

    @NotBlank(message = "La contrasenia es obligatoria")
    @Size(max = 30)
    private String contrasenia;

    @Email(message = "El correo no tiene un formato valido")
    @Size(max = 30)
    private String correo;

    @Size(max = 30)
    private String telefono;

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

    @NotNull(message = "Debe indicar el almacen donde trabaja")
    private Long idAlmacen;

    public SupervisorRequestDTO() {
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
