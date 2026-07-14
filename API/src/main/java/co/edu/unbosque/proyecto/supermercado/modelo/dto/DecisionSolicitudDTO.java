package co.edu.unbosque.proyecto.supermercado.modelo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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
