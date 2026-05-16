package com.ticketflow.access_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessDTO {
    private Long id;

    @NotNull(message = "El ID del ticket es obligatorio")
    private Long ticketId;

    @NotBlank(message = "La puerta de acceso (gate) es obligatoria")
    private String gate;

    private String accessTime;
    private String status;
}
