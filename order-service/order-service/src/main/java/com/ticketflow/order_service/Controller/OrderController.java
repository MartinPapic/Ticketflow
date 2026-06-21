package com.ticketflow.order_service.Controller;

import org.springframework.http.HttpStatus;

import com.ticketflow.order_service.DTO.OrderDTO;
import com.ticketflow.order_service.Model.Order;
import com.ticketflow.order_service.Service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
@Tag(name = "Order", description = "API for Order operations")
public class OrderController {

    @Autowired
    private OrderService service;

    @Operation(summary = "Get all entities", description = "Retrieves a list of all entities")
    @GetMapping
    public ResponseEntity<List<Order>> buscarTodos() {
        log.info("Petición GET para listar todos los Orders");
        List<Order> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            log.warn("No se encontraron registros de Order");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Get entity by ID", description = "Retrieves an entity by its identifier")
    @GetMapping("/{id}")
    public ResponseEntity<Order> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Order con ID: {}", id);
        Order entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Create entity", description = "Creates a new entity in the system")
    @PostMapping
    public ResponseEntity<Order> crear(@Valid @RequestBody OrderDTO dto) {
        log.info("Creando nueva orden para el usuario ID: {}", dto.getUserId());
        Order entity = new Order();
        entity.setUserId(dto.getUserId());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setOrderDate(dto.getOrderDate());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        
        if (dto.getItems() != null) {
            java.util.List<com.ticketflow.order_service.Model.OrderItem> items = dto.getItems().stream().map(itemDto -> {
                com.ticketflow.order_service.Model.OrderItem item = new com.ticketflow.order_service.Model.OrderItem();
                item.setTicketId(itemDto.getTicketId());
                item.setPrice(itemDto.getPrice());
                item.setOrder(entity);
                return item;
            }).collect(java.util.stream.Collectors.toList());
            entity.setItems(items);
        }
        
        Order saved = service.crear(entity);
        log.info("Orden creada exitosamente con sus ítems. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Update entity", description = "Updates an existing entity by its identifier")
    @PutMapping("/{id}")
    public ResponseEntity<Order> actualizar(@PathVariable Long id, @Valid @RequestBody OrderDTO dto) {
        log.info("Petición PUT para actualizar Order con ID: {}", id);
        Order entity = new Order();
        entity.setUserId(dto.getUserId());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setOrderDate(dto.getOrderDate());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        
        if (dto.getItems() != null) {
            java.util.List<com.ticketflow.order_service.Model.OrderItem> items = dto.getItems().stream().map(itemDto -> {
                com.ticketflow.order_service.Model.OrderItem item = new com.ticketflow.order_service.Model.OrderItem();
                item.setTicketId(itemDto.getTicketId());
                item.setPrice(itemDto.getPrice());
                item.setOrder(entity);
                return item;
            }).collect(java.util.stream.Collectors.toList());
            entity.setItems(items);
        }
        
        Order updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete entity", description = "Deletes an entity by its identifier")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Order con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
