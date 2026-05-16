package com.ticketflow.venue_service.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VenueDTO {
    private Long id;

    @NotBlank(message = "El nombre del recinto es obligatorio")
    private String name;

    @NotBlank(message = "La dirección del recinto es obligatoria")
    private String address;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;
}
