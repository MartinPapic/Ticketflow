package com.ticketflow.ticket_service.Service;

import com.ticketflow.ticket_service.Model.Ticket;
import com.ticketflow.ticket_service.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repository;

    public List<Ticket> buscarTodos() {
        return repository.findAll();
    }

    public Ticket buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Ticket crear(Ticket entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
