package com.ticketflow.access_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ticket-service", url = "http://localhost:8083/api/v1/ticket")
public interface TicketClient {
    @GetMapping("/{id}")
    Object buscarPorId(@PathVariable("id") Long id);
}
