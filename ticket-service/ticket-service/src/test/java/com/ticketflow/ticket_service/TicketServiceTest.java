package com.ticketflow.ticket_service;

import com.ticketflow.ticket_service.Model.Ticket;
import com.ticketflow.ticket_service.Repository.TicketRepository;
import com.ticketflow.ticket_service.Service.TicketService;
import com.ticketflow.ticket_service.Exception.ResourceNotFoundException;
import com.ticketflow.ticket_service.Exception.BusinessValidationException;
import com.ticketflow.ticket_service.Client.EventClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private TicketService service;

    private Ticket mockEntity;

    @BeforeEach
    void setUp() {
        mockEntity = new Ticket();
        mockEntity.setId(1L);
        mockEntity.setOrderId(100L);
        mockEntity.setEventId(10L);
        mockEntity.setSeatId(12L);
        mockEntity.setPrice(50.0);
        mockEntity.setStatus("ISSUED");
    }

    @Test
    void givenList_whenBuscarTodos_thenReturnList() {
        // Given
        when(repository.findAll()).thenReturn(Arrays.asList(mockEntity));

        // When
        List<Ticket> result = service.buscarTodos();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void givenValidId_whenBuscarPorId_thenReturnEntity() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));

        // When
        Ticket result = service.buscarPorId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void givenValidEventId_whenCrear_thenReturnSavedEntity() {
        // Given
        when(eventClient.buscarPorId(10L)).thenReturn(new Object());
        when(repository.save(any(Ticket.class))).thenReturn(mockEntity);

        // When
        Ticket result = service.crear(mockEntity);

        // Then
        assertNotNull(result);
        assertEquals(50.0, result.getPrice());
        verify(eventClient, times(1)).buscarPorId(10L);
        verify(repository, times(1)).save(any(Ticket.class));
    }

    @Test
    void givenInvalidEventId_whenCrear_thenThrowResourceNotFoundException() {
        // Given
        when(eventClient.buscarPorId(10L)).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.crear(mockEntity);
        });
        verify(eventClient, times(1)).buscarPorId(10L);
        verify(repository, never()).save(any(Ticket.class));
    }

    @Test
    void givenValidId_whenEliminar_thenDeleteEntity() {
        // Given
        doNothing().when(repository).deleteById(1L);

        // When
        service.eliminar(1L);

        // Then
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenValidEntity_whenActualizar_thenReturnUpdatedEntity() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));
        when(eventClient.buscarPorId(10L)).thenReturn(new Object());
        when(repository.save(any(Ticket.class))).thenReturn(mockEntity);
        
        Ticket updateData = new Ticket();
        updateData.setEventId(10L);
        updateData.setPrice(100.0);

        // When
        Ticket result = service.actualizar(1L, updateData);

        // Then
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(eventClient, times(1)).buscarPorId(10L);
        verify(repository, times(1)).save(any(Ticket.class));
    }
}
