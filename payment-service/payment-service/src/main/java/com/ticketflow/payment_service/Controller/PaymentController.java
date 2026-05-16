package com.ticketflow.payment_service.Controller;

import com.ticketflow.payment_service.DTO.PaymentDTO;
import com.ticketflow.payment_service.Model.Payment;
import com.ticketflow.payment_service.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService service;

    @GetMapping
    public ResponseEntity<List<Payment>> buscarTodos() {
        log.info("Buscando todos los pagos");
        List<Payment> lista = service.buscarTodos();
        if(lista.isEmpty()){
            log.warn("No se encontraron pagos");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> buscarPorId(@PathVariable Long id) {
        log.info("Buscando pago con ID: {}", id);
        Payment entity = service.buscarPorId(id);
        if (entity == null) {
            log.error("Pago con ID: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando pago con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
