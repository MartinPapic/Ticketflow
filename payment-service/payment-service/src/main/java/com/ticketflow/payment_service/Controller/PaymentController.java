package com.ticketflow.payment_service.Controller;

import org.springframework.http.HttpStatus;

import com.ticketflow.payment_service.DTO.PaymentDTO;
import com.ticketflow.payment_service.Model.Payment;
import com.ticketflow.payment_service.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Payment", description = "Operaciones del microservicio de Payment")
@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
@Tag(name = "Payment", description = "API for Payment operations")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @Operation(summary = "Obtener todos los registros", description = "Retorna una lista completa de entidades")
    @GetMapping
    public ResponseEntity<List<Payment>> buscarTodos() {
        log.info("Petición GET para listar todos los Payments");
        List<Payment> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            log.warn("No se encontraron registros de Payment");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar por ID", description = "Busca un registro específico por su identificador único")
    @GetMapping("/{id}")
    public ResponseEntity<Payment> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Payment con ID: {}", id);
        Payment entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Crear nuevo registro", description = "Registra una nueva entidad en la base de datos")
    @PostMapping
    public ResponseEntity<Payment> crear(@Valid @RequestBody PaymentDTO dto) {
        log.info("Procesando pago para la orden ID: {}", dto.getOrderId());
        Payment entity = new Payment();
        entity.setOrderId(dto.getOrderId());
        entity.setAmount(dto.getAmount());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setStatus(dto.getStatus());
        
        Payment saved = service.crear(entity);
        log.info("Pago procesado exitosamente. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Actualizar registro", description = "Actualiza completamente un registro existente")
    @PutMapping("/{id}")
    public ResponseEntity<Payment> actualizar(@PathVariable Long id, @Valid @RequestBody PaymentDTO dto) {
        log.info("Petición PUT para actualizar Payment con ID: {}", id);
        Payment entity = new Payment();
        entity.setOrderId(dto.getOrderId());
        entity.setAmount(dto.getAmount());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setStatus(dto.getStatus());
        
        Payment updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar registro", description = "Elimina un registro de la base de datos por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Payment con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
