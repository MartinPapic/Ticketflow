package com.ticketflow.payment_service.DTO;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private Long orderId;
    private Double amount;
    private String paymentMethod;
    private String paymentDate;
    private String status;
}
