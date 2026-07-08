package co.edu.unbosque.proyecto.supermercado.modelo.excepciones;

/**
 * Se lanza cuando se busca un registro por id y no existe
 * (por ejemplo, un Cliente, una Pareja o una Compra inexistente).
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
