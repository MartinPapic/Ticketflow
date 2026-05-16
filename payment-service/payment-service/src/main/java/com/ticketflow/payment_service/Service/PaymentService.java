package com.ticketflow.payment_service.Service;

import com.ticketflow.payment_service.Model.Payment;
import com.ticketflow.payment_service.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    public List<Payment> buscarTodos() {
        return repository.findAll();
    }

    public Payment buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Payment crear(Payment entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
