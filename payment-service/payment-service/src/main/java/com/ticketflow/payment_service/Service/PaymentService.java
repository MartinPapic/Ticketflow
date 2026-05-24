package com.ticketflow.payment_service.Service;

import com.ticketflow.payment_service.Exception.ResourceNotFoundException;
import com.ticketflow.payment_service.Exception.BusinessValidationException;

import org.springframework.transaction.annotation.Transactional;

import com.ticketflow.payment_service.Client.OrderClient;
import com.ticketflow.payment_service.Model.Payment;
import com.ticketflow.payment_service.Repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private OrderClient orderClient;

    public List<Payment> buscarTodos() {
        log.info("Buscando todos los registros de Payment");
        return repository.findAll();
    }

    public Payment buscarPorId(Long id) {
        log.info("Buscando Payment con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Payment con ID " + id + " no existe."));
    }

    @Transactional
    public Payment crear(Payment entity) {
        log.info("Validando existencia de la orden ID: {} para procesar el pago", entity.getOrderId());
        
        try {
            Object order = orderClient.buscarPorId(entity.getOrderId());
            if (order == null) {
                log.error("Orden ID: {} no encontrada. Pago rechazado.", entity.getOrderId());
                throw new ResourceNotFoundException("La orden asociada ID " + entity.getOrderId() + " no existe.");
            }
            log.info("Orden validada correctamente. Registrando pago por monto: {}", entity.getAmount());
            return repository.save(entity);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al comunicarse con order-service: {}", e.getMessage());
            throw new BusinessValidationException("Error en validación de orden: " + e.getMessage());
        }
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando Payment con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El registro de Payment con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public Payment actualizar(Long id, Payment entity) {
        log.info("Actualizando Payment con ID: {}", id);
        Payment existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Payment con ID " + id + " no existe."));
        
        try {
            Object order = orderClient.buscarPorId(entity.getOrderId());
            if (order == null) {
                log.error("Orden ID: {} no encontrada. Pago rechazado.", entity.getOrderId());
                throw new ResourceNotFoundException("La orden asociada ID " + entity.getOrderId() + " no existe.");
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al comunicarse con order-service: {}", e.getMessage());
            throw new BusinessValidationException("Error en validación de orden: " + e.getMessage());
        }

        existente.setOrderId(entity.getOrderId());
        existente.setAmount(entity.getAmount());
        existente.setPaymentMethod(entity.getPaymentMethod());
        existente.setPaymentDate(entity.getPaymentDate());
        existente.setStatus(entity.getStatus());
        
        return repository.save(existente);
    }
}
