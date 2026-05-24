package com.ticketflow.event_service.Service;

import com.ticketflow.event_service.Exception.ResourceNotFoundException;
import com.ticketflow.event_service.Exception.BusinessValidationException;

import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.ticketflow.event_service.Model.Event;
import com.ticketflow.event_service.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class EventService {

    @Autowired
    private EventRepository repository;

    public List<Event> buscarTodos() {
        log.info("Buscando todos los registros de Event");
        return repository.findAll();
    }

    public Event buscarPorId(Long id) {
        log.info("Buscando Event con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Event con ID " + id + " no existe."));
    }

    @Transactional
    public Event crear(Event entity) {
        log.info("Creando nuevo registro de Event");
        return repository.save(entity);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando Event con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El registro de Event con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public Event actualizar(Long id, Event entity) {
        log.info("Actualizando Event con ID: {}", id);
        Event existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Event con ID " + id + " no existe."));
        
        existente.setName(entity.getName());
        existente.setDescription(entity.getDescription());
        existente.setVenueId(entity.getVenueId());
        existente.setDate(entity.getDate());
        existente.setStatus(entity.getStatus());
        
        return repository.save(existente);
    }
}
