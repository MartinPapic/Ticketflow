package com.ticketflow.order_service;

import com.ticketflow.order_service.Model.Order;
import com.ticketflow.order_service.Repository.OrderRepository;
import com.ticketflow.order_service.Service.OrderService;
import com.ticketflow.order_service.Exception.ResourceNotFoundException;
import com.ticketflow.order_service.Exception.BusinessValidationException;
import com.ticketflow.order_service.Client.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private OrderService service;

    private Order mockEntity;

    @BeforeEach
    void setUp() {
        mockEntity = new Order();
        mockEntity.setId(1L);
        mockEntity.setUserId(5L);
        mockEntity.setTotalAmount(150.00);
        mockEntity.setOrderDate(LocalDateTime.now().toString());
        mockEntity.setStatus("COMPLETED");
        mockEntity.setItems(new ArrayList<>());
    }

    @Test
    void givenList_whenBuscarTodos_thenReturnList() {
        // Given
        when(repository.findAll()).thenReturn(Arrays.asList(mockEntity));

        // When
        List<Order> result = service.buscarTodos();

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
        Order result = service.buscarPorId(1L);

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
    void givenValidUserId_whenCrear_thenReturnSavedEntity() {
        // Given
        when(userClient.buscarPorId(5L)).thenReturn(new Object());
        when(repository.save(any(Order.class))).thenReturn(mockEntity);

        // When
        Order result = service.crear(mockEntity);

        // Then
        assertNotNull(result);
        assertEquals(150.00, result.getTotalAmount());
        verify(userClient, times(1)).buscarPorId(5L);
        verify(repository, times(1)).save(any(Order.class));
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
        verify(repository, never()).save(any(Order.class));
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
        when(userClient.buscarPorId(5L)).thenReturn(new Object());
        when(repository.save(any(Order.class))).thenReturn(mockEntity);
        
        Order updateData = new Order();
        updateData.setUserId(5L);
        updateData.setTotalAmount(200.00);

        // When
        Order result = service.actualizar(1L, updateData);

        // Then
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(userClient, times(1)).buscarPorId(5L);
        verify(repository, times(1)).save(any(Order.class));
    }
}
