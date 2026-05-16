package com.ticketflow.ticket_service.Controller;

import com.ticketflow.ticket_service.DTO.TicketDTO;
import com.ticketflow.ticket_service.Model.Ticket;
import com.ticketflow.ticket_service.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {

    @Autowired
    private TicketService service;

    @GetMapping
    public ResponseEntity<List<Ticket>> buscarTodos() {
        List<Ticket> lista = service.buscarTodos();
        if(lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> buscarPorId(@PathVariable Long id) {
        Ticket entity = service.buscarPorId(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Ticket> crear(@RequestBody TicketDTO dto) {
        Ticket entity = new Ticket();
        // Here we'd map DTO to Entity. For simplicity, just saving new entity or manual map
        // assuming standard mapper logic
        entity.setOrderId(dto.getOrderId());
        entity.setEventId(dto.getEventId());
        entity.setSeatId(dto.getSeatId());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus());
        return ResponseEntity.ok(service.crear(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
