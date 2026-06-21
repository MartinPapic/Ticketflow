package com.ticketflow.payment_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${gateway.url:http://localhost:8080}/api/v1/order")
public interface OrderClient {

    @GetMapping("/{id}")
    Object buscarPorId(@PathVariable("id") Long id);
}
