package co.edu.unbosque.proyecto.supermercado.modelo;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Clase base para los usuarios del sistema (Cliente, Pareja, Supervisor).
 * Se usa @MappedSuperclass porque USUARIO no tiene tabla propia en la
 * base de datos: cada subtipo hereda estas columnas dentro de su propia
 * tabla (id_usuario, nombre_usuario, contrasenia, estado).
 */
@MappedSuperclass
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", length = 30, nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "contrasenia", length = 30, nullable = false)
    private String contrasenia;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    public Usuario() {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
