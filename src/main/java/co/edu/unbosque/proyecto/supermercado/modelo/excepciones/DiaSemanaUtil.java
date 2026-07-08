package co.edu.unbosque.proyecto.supermercado.modelo.excepciones;

import java.time.DayOfWeek;

/**
 * Traduce el DayOfWeek de Java (en inglés) al nombre en español que
 * se guarda en RestriccionHorario.diaBloqueo (ej: "Lunes", "Martes").
 */
public class DiaSemanaUtil {

    private DiaSemanaUtil() {
    }

    public static String aEspanol(DayOfWeek diaSemana) {
        switch (diaSemana) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miercoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sabado";
            case SUNDAY: return "Domingo";
            default: throw new IllegalArgumentException("Día de la semana no reconocido: " + diaSemana);
        }
    }
}
