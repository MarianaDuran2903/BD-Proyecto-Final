package co.edu.unbosque.proyecto.supermercado.modelo.excepciones;

/**
 * Se lanza cuando una operación viola una regla de negocio del
 * enunciado, por ejemplo:
 * - La suma de cupos de las parejas supera el cupo total del cliente
 *   (Límite de Integridad).
 * - Se intenta registrar una compra en un horario bloqueado para
 *   esa pareja.
 * - Se intenta autorizar un sobrecupo sobre una compra que no lo
 *   requiere o que ya tiene una autorización registrada.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
