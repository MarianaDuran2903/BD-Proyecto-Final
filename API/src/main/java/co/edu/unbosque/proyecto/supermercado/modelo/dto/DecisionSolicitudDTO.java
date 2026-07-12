package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// DTO que usa el cliente o el supervisor para aprobar, escalar o rechazar una solicitud
// decision debe ser exactamente "Aprobar", "Escalar" o "Rechazar"
// (Escalar solo aplica cuando decide el cliente: ver decidirComoCliente())
public class DecisionSolicitudDTO {

    @NotBlank(message = "La decision es obligatoria")
    @Pattern(regexp = "Aprobar|Escalar|Rechazar", message = "La decision debe ser 'Aprobar', 'Escalar' o 'Rechazar'")
    private String decision;

    public DecisionSolicitudDTO() {
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
