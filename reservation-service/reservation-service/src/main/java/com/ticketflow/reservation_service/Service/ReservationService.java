package com.ticketflow.reservation_service.Service;

import com.ticketflow.reservation_service.Model.Reservation;
import com.ticketflow.reservation_service.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repository;

    public List<Reservation> buscarTodos() {
        return repository.findAll();
    }

    public Reservation buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Reservation crear(Reservation entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
