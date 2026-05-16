package com.ticketflow.event_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventDTO {
    private Long id;

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    private String description;

    @NotNull(message = "El ID del recinto (venue) es obligatorio")
    private Long venueId;

    @NotBlank(message = "La fecha del evento es obligatoria")
    private String date;

    private String status;
}
