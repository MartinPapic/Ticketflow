package com.ticketflow.venue_service.Controller;

import com.ticketflow.venue_service.DTO.VenueDTO;
import com.ticketflow.venue_service.Model.Venue;
import com.ticketflow.venue_service.Service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/venue")
public class VenueController {

    @Autowired
    private VenueService service;

    @GetMapping
    public ResponseEntity<List<Venue>> buscarTodos() {
        List<Venue> lista = service.buscarTodos();
        if(lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> buscarPorId(@PathVariable Long id) {
        Venue entity = service.buscarPorId(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Venue> crear(@RequestBody VenueDTO dto) {
        Venue entity = new Venue();
        // Here we'd map DTO to Entity. For simplicity, just saving new entity or manual map
        // assuming standard mapper logic
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setCapacity(dto.getCapacity());
        return ResponseEntity.ok(service.crear(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
