package com.ticketflow.payment_service.Controller;

import com.ticketflow.payment_service.DTO.PaymentDTO;
import com.ticketflow.payment_service.Model.Payment;
import com.ticketflow.payment_service.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @GetMapping
    public ResponseEntity<List<Payment>> buscarTodos() {
        List<Payment> lista = service.buscarTodos();
        if(lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> buscarPorId(@PathVariable Long id) {
        Payment entity = service.buscarPorId(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Payment> crear(@RequestBody PaymentDTO dto) {
        Payment entity = new Payment();
        // Here we'd map DTO to Entity. For simplicity, just saving new entity or manual map
        // assuming standard mapper logic
        entity.setOrderId(dto.getOrderId());
        entity.setAmount(dto.getAmount());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setStatus(dto.getStatus());
        return ResponseEntity.ok(service.crear(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
