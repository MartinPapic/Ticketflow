package com.ticketflow.access_service;

import com.ticketflow.access_service.Model.Access;
import com.ticketflow.access_service.Repository.AccessRepository;
import com.ticketflow.access_service.Service.AccessService;
import com.ticketflow.access_service.Exception.ResourceNotFoundException;
import com.ticketflow.access_service.Exception.BusinessValidationException;
import com.ticketflow.access_service.Client.TicketClient;
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
public class AccessServiceTest {

    @Mock
    private AccessRepository repository;

    @Mock
    private TicketClient ticketClient;

    @InjectMocks
    private AccessService service;

    private Access mockEntity;

    @BeforeEach
    void setUp() {
        mockEntity = new Access();
        mockEntity.setId(1L);
        mockEntity.setTicketId(100L);
        mockEntity.setGate("A1");
        mockEntity.setAccessTime(LocalDateTime.now().toString());
        mockEntity.setStatus("GRANTED");
    }

    @Test
    void givenList_whenBuscarTodos_thenReturnList() {
        // Given
        when(repository.findAll()).thenReturn(Arrays.asList(mockEntity));

        // When
        List<Access> result = service.buscarTodos();

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
        Access result = service.buscarPorId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void givenInvalidId_whenBuscarPorId_thenThrowResourceNotFoundException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.buscarPorId(99L);
        });
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void givenValidTicketId_whenCrear_thenReturnSavedEntity() {
        // Given
        when(ticketClient.buscarPorId(100L)).thenReturn(new Object());
        when(repository.save(any(Access.class))).thenReturn(mockEntity);

        // When
        Access result = service.crear(mockEntity);

        // Then
        assertNotNull(result);
        assertEquals(100L, result.getTicketId());
        verify(ticketClient, times(1)).buscarPorId(100L);
        verify(repository, times(1)).save(any(Access.class));
    }

    @Test
    void givenInvalidTicketId_whenCrear_thenThrowResourceNotFoundException() {
        // Given
        when(ticketClient.buscarPorId(100L)).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.crear(mockEntity);
        });
        verify(ticketClient, times(1)).buscarPorId(100L);
        verify(repository, never()).save(any(Access.class));
    }

    @Test
    void givenValidId_whenEliminar_thenDeleteEntity() {
        // Given
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // When
        service.eliminar(1L);

        // Then
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenInvalidId_whenEliminar_thenThrowException() {
        // Given
        when(repository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.eliminar(99L);
        });
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }

    @Test
    void givenValidEntity_whenActualizar_thenReturnUpdatedEntity() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));
        when(ticketClient.buscarPorId(100L)).thenReturn(new Object());
        when(repository.save(any(Access.class))).thenReturn(mockEntity);
        
        Access updateData = new Access();
        updateData.setTicketId(100L);
        updateData.setGate("B2");

        // When
        Access result = service.actualizar(1L, updateData);

        // Then
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(ticketClient, times(1)).buscarPorId(100L);
        verify(repository, times(1)).save(any(Access.class));
    }
}
