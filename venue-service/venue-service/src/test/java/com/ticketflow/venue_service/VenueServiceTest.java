package com.ticketflow.venue_service;

import com.ticketflow.venue_service.Model.Venue;
import com.ticketflow.venue_service.Repository.VenueRepository;
import com.ticketflow.venue_service.Service.VenueService;
import com.ticketflow.venue_service.Exception.ResourceNotFoundException;
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
public class VenueServiceTest {

    @Mock
    private VenueRepository repository;

    @InjectMocks
    private VenueService service;

    private Venue mockEntity;

    @BeforeEach
    void setUp() {
        mockEntity = new Venue();
        mockEntity.setId(1L);
        mockEntity.setName("Estadio Nacional");
        mockEntity.setAddress("Avenida Grecia");
        mockEntity.setCapacity(50000);
    }

    @Test
    void givenList_whenBuscarTodos_thenReturnList() {
        // Given
        when(repository.findAll()).thenReturn(Arrays.asList(mockEntity));

        // When
        List<Venue> result = service.buscarTodos();

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
        Venue result = service.buscarPorId(1L);

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
    void givenEntity_whenCrear_thenReturnSavedEntity() {
        // Given
        when(repository.save(any(Venue.class))).thenReturn(mockEntity);

        // When
        Venue result = service.crear(mockEntity);

        // Then
        assertNotNull(result);
        assertEquals("Estadio Nacional", result.getName());
        verify(repository, times(1)).save(any(Venue.class));
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
        when(repository.save(any(Venue.class))).thenReturn(mockEntity);
        
        Venue updateData = new Venue();
        updateData.setName("Movistar Arena");

        // When
        Venue result = service.actualizar(1L, updateData);

        // Then
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Venue.class));
    }
}
