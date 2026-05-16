package com.ticketflow.order_service.Service;

import com.ticketflow.order_service.Client.UserClient;
import com.ticketflow.order_service.Model.Order;
import com.ticketflow.order_service.Repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private UserClient userClient;

    public List<Order> buscarTodos() {
        return repository.findAll();
    }

    public Order buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Order crear(Order entity) {
        log.info("Validando existencia del usuario ID: {} para la orden", entity.getUserId());
        
        try {
            Object user = userClient.buscarPorId(entity.getUserId());
            if (user == null) {
                log.error("Usuario ID: {} no encontrado. Abortando orden.", entity.getUserId());
                throw new RuntimeException("Usuario no existe");
            }
            log.info("Usuario validado. Guardando orden.");
            return repository.save(entity);
        } catch (Exception e) {
            log.error("Error al validar usuario con user-service: {}", e.getMessage());
            throw new RuntimeException("Error en validación de usuario: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
