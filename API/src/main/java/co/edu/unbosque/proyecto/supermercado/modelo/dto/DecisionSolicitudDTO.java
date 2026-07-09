package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

// DTO que usa el cliente o el supervisor para aprobar o rechazar una solicitud
// decision debe ser exactamente "Aprobar" o "Rechazar"
public class DecisionSolicitudDTO {

    @NotBlank(message = "La decision es obligatoria")
    @Pattern(regexp = "Aprobar|Rechazar", message = "La decision debe ser 'Aprobar' o 'Rechazar'")
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
