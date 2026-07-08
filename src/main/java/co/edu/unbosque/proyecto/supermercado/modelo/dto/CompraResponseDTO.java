package co.edu.unbosque.proyecto.supermercado.modelo.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Datos que se devuelven al consultar una Compra.
 * Trae los nombres ya resueltos de pareja, almacén y supervisor
 * (si aplica), para que el frontend no tenga que hacer consultas
 * adicionales por cada id.
 */
public class CompraResponseDTO {

    private Long idCompra;
    private BigDecimal monto;
    private LocalDate fecha;
    private LocalTime hora;
    private Boolean requiereSobrecupo;

    private Long idUsuarioPareja;
    private String nombreParejaCompleto;

    private Long idAlmacen;
    private String nombreAlmacen;

    private Long idUsuarioSupervisor;
    private String nombreSupervisorCompleto;

    public CompraResponseDTO() {
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Boolean getRequiereSobrecupo() {
        return requiereSobrecupo;
    }

    public void setRequiereSobrecupo(Boolean requiereSobrecupo) {
        this.requiereSobrecupo = requiereSobrecupo;
    }

    public Long getIdUsuarioPareja() {
        return idUsuarioPareja;
    }

    public void setIdUsuarioPareja(Long idUsuarioPareja) {
        this.idUsuarioPareja = idUsuarioPareja;
    }

    public String getNombreParejaCompleto() {
        return nombreParejaCompleto;
    }

    public void setNombreParejaCompleto(String nombreParejaCompleto) {
        this.nombreParejaCompleto = nombreParejaCompleto;
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

    public Long getIdUsuarioSupervisor() {
        return idUsuarioSupervisor;
    }

    public void setIdUsuarioSupervisor(Long idUsuarioSupervisor) {
        this.idUsuarioSupervisor = idUsuarioSupervisor;
    }

    public String getNombreSupervisorCompleto() {
        return nombreSupervisorCompleto;
    }

    public void setNombreSupervisorCompleto(String nombreSupervisorCompleto) {
        this.nombreSupervisorCompleto = nombreSupervisorCompleto;
    }
}
