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

    @org.springframework.transaction.annotation.Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @org.springframework.transaction.annotation.Transactional
    public Ticket actualizar(Long id, Ticket entity) {
        log.info("Actualizando ticket con ID: {}", id);
        Ticket existente = repository.findById(id)
                .orElseThrow(() -> new com.ticketflow.ticket_service.Exception.ResourceNotFoundException("El ticket con ID " + id + " no existe."));
        
        try {
            Object event = eventClient.buscarPorId(entity.getEventId());
            if (event == null) {
                log.error("El evento con ID: {} no existe. No se puede actualizar el ticket.", entity.getEventId());
                throw new com.ticketflow.ticket_service.Exception.ResourceNotFoundException("El evento con ID " + entity.getEventId() + " no existe.");
            }
        } catch (com.ticketflow.ticket_service.Exception.ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al comunicarse con el servicio de eventos: {}", e.getMessage());
            throw new com.ticketflow.ticket_service.Exception.BusinessValidationException("Fallo al validar evento de forma remota: " + e.getMessage());
        }

        existente.setOrderId(entity.getOrderId());
        existente.setEventId(entity.getEventId());
        existente.setSeatId(entity.getSeatId());
        existente.setPrice(entity.getPrice());
        existente.setStatus(entity.getStatus());
        
        return repository.save(existente);
    }
}
