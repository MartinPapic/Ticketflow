package com.ticketflow.payment_service.Service;

import com.ticketflow.payment_service.Client.OrderClient;
import com.ticketflow.payment_service.Model.Payment;
import com.ticketflow.payment_service.Repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private OrderClient orderClient;

    public List<Payment> buscarTodos() {
        return repository.findAll();
    }

    public Payment buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Payment crear(Payment entity) {
        log.info("Validando existencia de la orden ID: {} para procesar el pago", entity.getOrderId());
        
        try {
            Object order = orderClient.buscarPorId(entity.getOrderId());
            if (order == null) {
                log.error("Orden ID: {} no encontrada. Pago rechazado.", entity.getOrderId());
                throw new RuntimeException("Orden no existe");
            }
            log.info("Orden validada correctamente. Registrando pago por monto: {}", entity.getAmount());
            return repository.save(entity);
        } catch (Exception e) {
            log.error("Error al comunicarse con order-service: {}", e.getMessage());
            throw new RuntimeException("Error en validación de orden: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
