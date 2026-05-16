package com.ticketflow.order_service.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class OrderDTO {
    private Long id;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El monto total es obligatorio")
    @PositiveOrZero(message = "El monto total no puede ser negativo")
    private Double totalAmount;

    private String orderDate;
    private String status;
}
