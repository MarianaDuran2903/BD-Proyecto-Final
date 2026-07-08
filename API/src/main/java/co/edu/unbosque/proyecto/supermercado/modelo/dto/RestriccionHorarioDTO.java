package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO único para RestriccionHorario: se usa tanto para crear como para
 * consultar. No tiene datos sensibles, así que no se justifica
 * separarlo en Request/Response.
 */
public class RestriccionHorarioDTO {

    private Long idRestriccion;

    @Size(max = 30)
    private String motivo;

    @NotBlank(message = "El día de bloqueo es obligatorio")
    @Size(max = 30)
    private String diaBloqueo;

    @NotNull(message = "La hora de inicio del bloqueo es obligatoria")
    private LocalTime horaBloqueoInicio;

    @NotNull(message = "La hora de fin del bloqueo es obligatoria")
    private LocalTime horaBloqueoFin;

    @NotNull(message = "Debe indicar la pareja a la que aplica la restricción")
    private Long idUsuarioPareja;

    public RestriccionHorarioDTO() {
    }

    public Long getIdRestriccion() {
        return idRestriccion;
    }

    public void setIdRestriccion(Long idRestriccion) {
        this.idRestriccion = idRestriccion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiaBloqueo() {
        return diaBloqueo;
    }

    public void setDiaBloqueo(String diaBloqueo) {
        this.diaBloqueo = diaBloqueo;
    }

    public LocalTime getHoraBloqueoInicio() {
        return horaBloqueoInicio;
    }

    public void setHoraBloqueoInicio(LocalTime horaBloqueoInicio) {
        this.horaBloqueoInicio = horaBloqueoInicio;
    }

    public LocalTime getHoraBloqueoFin() {
        return horaBloqueoFin;
    }

    public void setHoraBloqueoFin(LocalTime horaBloqueoFin) {
        this.horaBloqueoFin = horaBloqueoFin;
    }

    public Long getIdUsuarioPareja() {
        return idUsuarioPareja;
    }

    public void setIdUsuarioPareja(Long idUsuarioPareja) {
        this.idUsuarioPareja = idUsuarioPareja;
    }
}