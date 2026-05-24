package com.ticketflow.venue_service.Service;

import com.ticketflow.venue_service.Exception.ResourceNotFoundException;
import com.ticketflow.venue_service.Exception.BusinessValidationException;

import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.ticketflow.venue_service.Model.Venue;
import com.ticketflow.venue_service.Repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class VenueService {

    @Autowired
    private VenueRepository repository;

    public List<Venue> buscarTodos() {
        log.info("Buscando todos los registros de Venue");
        return repository.findAll();
    }

    public Venue buscarPorId(Long id) {
        log.info("Buscando Venue con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Venue con ID " + id + " no existe."));
    }

    @Transactional
    public Venue crear(Venue entity) {
        log.info("Creando nuevo registro de Venue");
        return repository.save(entity);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando Venue con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El registro de Venue con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public Venue actualizar(Long id, Venue entity) {
        log.info("Actualizando Venue con ID: {}", id);
        Venue existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Venue con ID " + id + " no existe."));
        
        existente.setName(entity.getName());
        existente.setAddress(entity.getAddress());
        existente.setCapacity(entity.getCapacity());
        
        return repository.save(existente);
    }
}
