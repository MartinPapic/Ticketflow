package com.ticketflow.ticket_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "${gateway.url:http://localhost:8080}/api/v1/event")
public interface EventClient {

    @GetMapping("/{id}")
    Object buscarPorId(@PathVariable("id") Long id);
}
