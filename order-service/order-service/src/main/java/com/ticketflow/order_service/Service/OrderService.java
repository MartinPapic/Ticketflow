package com.ticketflow.order_service.Service;

import com.ticketflow.order_service.Model.Order;
import com.ticketflow.order_service.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public List<Order> buscarTodos() {
        return repository.findAll();
    }

    public Order buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Order crear(Order entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
