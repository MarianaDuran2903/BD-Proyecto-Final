package co.edu.unbosque.proyecto.supermercado.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class RestriccionHorario {

    private Long idRestriccion;
    private String motivo;
    private LocalDate diaBloqueo;
    private LocalTime horaBloqueoInicio;
    private LocalTime horaBloqueoFin;
    private Long idUsuarioPareja;

    public RestriccionHorario() {
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

    public LocalDate getDiaBloqueo() {
        return diaBloqueo;
    }

    public void setDiaBloqueo(LocalDate diaBloqueo) {
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
