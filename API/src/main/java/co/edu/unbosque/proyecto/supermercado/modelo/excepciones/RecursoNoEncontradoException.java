package co.edu.unbosque.proyecto.supermercado.modelo.excepciones;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
