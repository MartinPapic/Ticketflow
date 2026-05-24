package com.ticketflow.order_service.Service;

import com.ticketflow.order_service.Exception.ResourceNotFoundException;
import com.ticketflow.order_service.Exception.BusinessValidationException;

import org.springframework.transaction.annotation.Transactional;

import com.ticketflow.order_service.Client.UserClient;
import com.ticketflow.order_service.Model.Order;
import com.ticketflow.order_service.Repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private UserClient userClient;

    public List<Order> buscarTodos() {
        log.info("Buscando todos los registros de Order");
        return repository.findAll();
    }

    public Order buscarPorId(Long id) {
        log.info("Buscando Order con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Order con ID " + id + " no existe."));
    }

    @Transactional
    public Order crear(Order entity) {
        log.info("Validando existencia del usuario ID: {} para la orden", entity.getUserId());
        
        try {
            Object user = userClient.buscarPorId(entity.getUserId());
            if (user == null) {
                log.error("Usuario ID: {} no encontrado. Abortando orden.", entity.getUserId());
                throw new ResourceNotFoundException("El usuario asociado ID " + entity.getUserId() + " no existe.");
            }
            log.info("Usuario validado. Guardando orden.");
            return repository.save(entity);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al validar usuario con user-service: {}", e.getMessage());
            throw new BusinessValidationException("Error en validación de usuario: " + e.getMessage());
        }
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando Order con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El registro de Order con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public Order actualizar(Long id, Order entity) {
        log.info("Actualizando Order con ID: {}", id);
        Order existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Order con ID " + id + " no existe."));
        
        try {
            Object user = userClient.buscarPorId(entity.getUserId());
            if (user == null) {
                log.error("Usuario ID: {} no encontrado. Abortando actualización de orden.", entity.getUserId());
                throw new ResourceNotFoundException("El usuario asociado ID " + entity.getUserId() + " no existe.");
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al validar usuario con user-service: {}", e.getMessage());
            throw new BusinessValidationException("Error en validación de usuario: " + e.getMessage());
        }

        existente.setUserId(entity.getUserId());
        existente.setTotalAmount(entity.getTotalAmount());
        existente.setOrderDate(entity.getOrderDate());
        existente.setStatus(entity.getStatus());
        
        existente.getItems().clear();
        if (entity.getItems() != null) {
            for (com.ticketflow.order_service.Model.OrderItem item : entity.getItems()) {
                item.setOrder(existente);
                existente.getItems().add(item);
            }
        }
        
        return repository.save(existente);
    }
}
