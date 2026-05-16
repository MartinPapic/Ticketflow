package com.ticketflow.payment_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private Double amount;

    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;

    private String paymentDate;
    private String status;
}
