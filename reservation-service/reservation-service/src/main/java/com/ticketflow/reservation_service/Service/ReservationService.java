package com.ticketflow.reservation_service.Service;

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
public class ReservationService {

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private EventClient eventClient;

    public List<Reservation> buscarTodos() {
        return repository.findAll();
    }

    public Reservation buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Reservation crear(Reservation entity) {
        log.info("Iniciando validaciones para reserva: Usuario {} - Evento {}", entity.getUserId(), entity.getEventId());
        
        try {
            // Validar usuario
            Object user = userClient.buscarPorId(entity.getUserId());
            if (user == null) {
                log.error("Usuario ID: {} no existe", entity.getUserId());
                throw new RuntimeException("Usuario no encontrado");
            }

            // Validar evento
            Object event = eventClient.buscarPorId(entity.getEventId());
            if (event == null) {
                log.error("Evento ID: {} no existe", entity.getEventId());
                throw new RuntimeException("Evento no encontrado");
            }

            log.info("Validaciones exitosas. Guardando reserva.");
            return repository.save(entity);
        } catch (Exception e) {
            log.error("Fallo en la comunicación inter-servicio: {}", e.getMessage());
            throw new RuntimeException("Error de validación: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
