package co.edu.unbosque.proyecto.supermercado.controlador;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiError {

    private LocalDateTime fecha;
    private int status;
    private String mensaje;
    private Map<String, String> errores;

    public ApiError() {
        this.fecha = LocalDateTime.now();
    }

    public ApiError(int status, String mensaje) {
        this();
        this.status = status;
        this.mensaje = mensaje;
    }

    public ApiError(int status, String mensaje, Map<String, String> errores) {
        this(status, mensaje);
        this.errores = errores;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Map<String, String> getErrores() {
        return errores;
    }

    public void setErrores(Map<String, String> errores) {
        this.errores = errores;
    }
}
