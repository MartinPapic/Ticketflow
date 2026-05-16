package com.ticketflow.order_service.Controller;

import com.ticketflow.order_service.DTO.OrderDTO;
import com.ticketflow.order_service.Model.Order;
import com.ticketflow.order_service.Service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping
    public ResponseEntity<List<Order>> buscarTodos() {
        log.info("Buscando todas las órdenes");
        List<Order> lista = service.buscarTodos();
        if(lista.isEmpty()){
            log.warn("No se encontraron órdenes");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> buscarPorId(@PathVariable Long id) {
        log.info("Buscando orden con ID: {}", id);
        Order entity = service.buscarPorId(id);
        if (entity == null) {
            log.error("Orden con ID: {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Order> crear(@Valid @RequestBody OrderDTO dto) {
        log.info("Creando nueva orden para el usuario ID: {}", dto.getUserId());
        Order entity = new Order();
        entity.setUserId(dto.getUserId());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setOrderDate(dto.getOrderDate());
        entity.setStatus(dto.getStatus());
        
        Order saved = service.crear(entity);
        log.info("Orden creada exitosamente. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando orden con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
