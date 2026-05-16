package com.ticketflow.event_service.Service;

import com.ticketflow.event_service.Model.Event;
import com.ticketflow.event_service.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    public List<Event> buscarTodos() {
        return repository.findAll();
    }

    public Event buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Event crear(Event entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
