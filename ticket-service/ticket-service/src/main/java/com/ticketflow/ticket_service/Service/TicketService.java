package com.ticketflow.ticket_service.Service;

import com.ticketflow.ticket_service.Client.EventClient;
import com.ticketflow.ticket_service.Model.Ticket;
import com.ticketflow.ticket_service.Repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TicketService {

    @Autowired
    private TicketRepository repository;

    @Autowired
    private EventClient eventClient;

    public List<Ticket> buscarTodos() {
        return repository.findAll();
    }

    public Ticket buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Ticket crear(Ticket entity) {
        log.info("Validando evento ID: {} antes de crear ticket", entity.getEventId());
        
        try {
            Object event = eventClient.buscarPorId(entity.getEventId());
            if (event == null) {
                log.error("El evento con ID: {} no existe. No se puede crear el ticket.", entity.getEventId());
                throw new com.ticketflow.ticket_service.Exception.ResourceNotFoundException("El evento con ID " + entity.getEventId() + " no existe.");
            }
            log.info("Evento validado correctamente. Procediendo a guardar el ticket.");
            return repository.save(entity);
        } catch (com.ticketflow.ticket_service.Exception.ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al comunicarse con el servicio de eventos: {}", e.getMessage());
            throw new com.ticketflow.ticket_service.Exception.BusinessValidationException("Fallo al validar evento de forma remota: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
