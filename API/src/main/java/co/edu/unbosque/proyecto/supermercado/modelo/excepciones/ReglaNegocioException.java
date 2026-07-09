package co.edu.unbosque.proyecto.supermercado.modelo.excepciones;

public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
