package com.ticketflow.venue_service.Service;

import com.ticketflow.venue_service.Model.Venue;
import com.ticketflow.venue_service.Repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {

    @Autowired
    private VenueRepository repository;

    public List<Venue> buscarTodos() {
        return repository.findAll();
    }

    public Venue buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Venue crear(Venue entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
