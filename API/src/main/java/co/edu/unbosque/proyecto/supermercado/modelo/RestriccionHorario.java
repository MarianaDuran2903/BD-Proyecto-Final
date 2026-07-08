package co.edu.unbosque.proyecto.supermercado.modelo;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Franja de bloqueo configurada por el cliente para restringir las
 * compras de una Pareja en días u horas específicas.
 */
@Entity
@Table(name = "restriccion_horario")
public class RestriccionHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restriccion")
    private Long idRestriccion;

    @Column(name = "motivo", length = 30)
    private String motivo;

    @Column(name = "dia_bloqueo", length = 30, nullable = false)
    private String diaBloqueo;

    @Column(name = "hora_bloqueo_inicio", nullable = false)
    private LocalTime horaBloqueoInicio;

    @Column(name = "hora_bloqueo_fin", nullable = false)
    private LocalTime horaBloqueoFin;

    // Lado dueño de la relación Tener (RestriccionHorario N - 1 Pareja)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_pareja", nullable = false)
    private Pareja pareja;

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

    public Pareja getPareja() {
        return pareja;
    }

    public void setPareja(Pareja pareja) {
        this.pareja = pareja;
    }
}