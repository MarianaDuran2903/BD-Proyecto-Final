package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.math.BigDecimal;

public class LoginResponseDTO {

    private String tipo; // "CLIENTE", "PAREJA" o "SUPERVISOR"
    private Long idUsuario;
    private String nombreUsuario;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String estado;

    // Solo para CLIENTE
    private BigDecimal cupoTotalAutorizado;

    // Solo para PAREJA
    private BigDecimal cupoAsignado;
    private Long idUsuarioCliente;

    // Solo para SUPERVISOR
    private Long idAlmacen;
    private String nombreAlmacen;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }

    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getCupoTotalAutorizado() { return cupoTotalAutorizado; }
    public void setCupoTotalAutorizado(BigDecimal cupoTotalAutorizado) { this.cupoTotalAutorizado = cupoTotalAutorizado; }

    public BigDecimal getCupoAsignado() { return cupoAsignado; }
    public void setCupoAsignado(BigDecimal cupoAsignado) { this.cupoAsignado = cupoAsignado; }

    public Long getIdUsuarioCliente() { return idUsuarioCliente; }
    public void setIdUsuarioCliente(Long idUsuarioCliente) { this.idUsuarioCliente = idUsuarioCliente; }

    public Long getIdAlmacen() { return idAlmacen; }
    public void setIdAlmacen(Long idAlmacen) { this.idAlmacen = idAlmacen; }

    public String getNombreAlmacen() { return nombreAlmacen; }
    public void setNombreAlmacen(String nombreAlmacen) { this.nombreAlmacen = nombreAlmacen; }
}
