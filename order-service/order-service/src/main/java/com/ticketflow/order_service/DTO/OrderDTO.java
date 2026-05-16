package com.ticketflow.order_service.DTO;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private Double totalAmount;
    private String orderDate;
    private String status;
}
