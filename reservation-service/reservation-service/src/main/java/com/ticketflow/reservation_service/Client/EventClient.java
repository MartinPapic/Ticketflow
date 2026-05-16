package com.ticketflow.reservation_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "http://localhost:8082/api/v1/event")
public interface EventClient {
    @GetMapping("/{id}")
    Object buscarPorId(@PathVariable("id") Long id);
}
