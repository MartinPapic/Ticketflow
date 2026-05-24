package com.ticketflow.reservation_service.Service;

import com.ticketflow.reservation_service.Exception.ResourceNotFoundException;
import com.ticketflow.reservation_service.Exception.BusinessValidationException;

import org.springframework.transaction.annotation.Transactional;

import com.ticketflow.reservation_service.Client.EventClient;
import com.ticketflow.reservation_service.Client.UserClient;
import com.ticketflow.reservation_service.Model.Reservation;
import com.ticketflow.reservation_service.Repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ReservationService {

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private EventClient eventClient;

    public List<Reservation> buscarTodos() {
        log.info("Buscando todos los registros de Reservation");
        return repository.findAll();
    }

    public Reservation buscarPorId(Long id) {
        log.info("Buscando Reservation con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Reservation con ID " + id + " no existe."));
    }

    @Transactional
    public Reservation crear(Reservation entity) {
        log.info("Iniciando validaciones para reserva: Usuario {} - Evento {}", entity.getUserId(), entity.getEventId());
        
        try {
            // Validar usuario
            Object user = userClient.buscarPorId(entity.getUserId());
            if (user == null) {
                log.error("Usuario ID: {} no existe", entity.getUserId());
                throw new ResourceNotFoundException("El usuario asociado ID " + entity.getUserId() + " no existe.");
            }

            // Validar evento
            Object event = eventClient.buscarPorId(entity.getEventId());
            if (event == null) {
                log.error("Evento ID: {} no existe", entity.getEventId());
                throw new ResourceNotFoundException("El evento asociado ID " + entity.getEventId() + " no existe.");
            }

            log.info("Validaciones exitosas. Guardando reserva.");
            return repository.save(entity);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Fallo en la comunicación inter-servicio: {}", e.getMessage());
            throw new BusinessValidationException("Error de validación: " + e.getMessage());
        }
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando Reservation con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El registro de Reservation con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}
