package com.ticketflow.ticket_service.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TicketDTO {
    private Long id;

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @NotNull(message = "El ID del evento es obligatorio")
    private Long eventId;

    @NotNull(message = "El ID del asiento es obligatorio")
    private Long seatId;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    private Double price;

    private String status;
}
