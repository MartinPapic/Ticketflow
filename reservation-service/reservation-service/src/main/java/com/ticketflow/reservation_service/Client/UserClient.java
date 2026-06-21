package com.ticketflow.reservation_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${gateway.url:http://localhost:8080}/api/v1/user")
public interface UserClient {
    @GetMapping("/{id}")
    Object buscarPorId(@PathVariable("id") Long id);
}
