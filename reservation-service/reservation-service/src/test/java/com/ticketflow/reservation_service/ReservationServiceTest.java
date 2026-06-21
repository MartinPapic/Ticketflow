package com.ticketflow.reservation_service;

import com.ticketflow.reservation_service.Model.Reservation;
import com.ticketflow.reservation_service.Repository.ReservationRepository;
import com.ticketflow.reservation_service.Service.ReservationService;
import com.ticketflow.reservation_service.Exception.ResourceNotFoundException;
import com.ticketflow.reservation_service.Exception.BusinessValidationException;
import com.ticketflow.reservation_service.Client.EventClient;
import com.ticketflow.reservation_service.Client.UserClient;
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
public class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private ReservationService service;

    private Reservation mockEntity;

    @BeforeEach
    void setUp() {
        mockEntity = new Reservation();
        mockEntity.setId(1L);
        mockEntity.setUserId(5L);
        mockEntity.setEventId(10L);
        mockEntity.setSeatId(12L);
        mockEntity.setStatus("RESERVED");
        mockEntity.setExpirationTime(LocalDateTime.now().plusMinutes(15).toString());
    }

    @Test
    void givenList_whenBuscarTodos_thenReturnList() {
        // Given
        when(repository.findAll()).thenReturn(Arrays.asList(mockEntity));

        // When
        List<Reservation> result = service.buscarTodos();

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
        Reservation result = service.buscarPorId(1L);

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
    void givenValidData_whenCrear_thenReturnSavedEntity() {
        // Given
        when(userClient.buscarPorId(5L)).thenReturn(new Object());
        when(eventClient.buscarPorId(10L)).thenReturn(new Object());
        when(repository.save(any(Reservation.class))).thenReturn(mockEntity);

        // When
        Reservation result = service.crear(mockEntity);

        // Then
        assertNotNull(result);
        assertEquals("RESERVED", result.getStatus());
        verify(userClient, times(1)).buscarPorId(5L);
        verify(eventClient, times(1)).buscarPorId(10L);
        verify(repository, times(1)).save(any(Reservation.class));
    }

    @Test
    void givenInvalidUserId_whenCrear_thenThrowResourceNotFoundException() {
        // Given
        when(userClient.buscarPorId(5L)).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.crear(mockEntity);
        });
        verify(userClient, times(1)).buscarPorId(5L);
        verify(eventClient, never()).buscarPorId(any());
        verify(repository, never()).save(any(Reservation.class));
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
    void givenValidEntity_whenActualizar_thenReturnUpdatedEntity() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));
        when(userClient.buscarPorId(5L)).thenReturn(new Object());
        when(eventClient.buscarPorId(10L)).thenReturn(new Object());
        when(repository.save(any(Reservation.class))).thenReturn(mockEntity);
        
        Reservation updateData = new Reservation();
        updateData.setUserId(5L);
        updateData.setEventId(10L);
        updateData.setStatus("CONFIRMED");

        // When
        Reservation result = service.actualizar(1L, updateData);

        // Then
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(userClient, times(1)).buscarPorId(5L);
        verify(eventClient, times(1)).buscarPorId(10L);
        verify(repository, times(1)).save(any(Reservation.class));
    }
}
