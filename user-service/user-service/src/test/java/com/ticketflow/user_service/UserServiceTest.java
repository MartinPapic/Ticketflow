package com.ticketflow.user_service;

import com.ticketflow.user_service.Model.User;
import com.ticketflow.user_service.Repository.UserRepository;
import com.ticketflow.user_service.Service.UserService;
import com.ticketflow.user_service.Exception.ResourceNotFoundException;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@test.com");
        mockUser.setPassword("password");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        mockUser.setPhoneNumber("123456789");
        mockUser.setRole("CLIENT");
        mockUser.setActive(true);
        mockUser.setCreatedAt(LocalDateTime.now().toString());
        mockUser.setUpdatedAt(LocalDateTime.now().toString());
    }

    @Test
    void givenUserList_whenBuscarTodos_thenReturnUserList() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList(mockUser));

        // When
        List<User> result = userService.buscarTodos();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void givenValidId_whenBuscarPorId_thenReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // When
        User result = userService.buscarPorId(1L);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void givenInvalidId_whenBuscarPorId_thenThrowResourceNotFoundException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.buscarPorId(99L);
        });
        
        assertTrue(exception.getMessage().contains("no existe"));
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void givenUser_whenCrear_thenReturnSavedUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // When
        User result = userService.crear(mockUser);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenValidId_whenEliminar_thenDeleteUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.eliminar(1L);

        // Then
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void givenInvalidId_whenEliminar_thenThrowException() {
        // Given
        when(userRepository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.eliminar(99L);
        });
        
        verify(userRepository, times(1)).existsById(99L);
        verify(userRepository, never()).deleteById(99L);
    }

    @Test
    void givenValidUser_whenActualizar_thenReturnUpdatedUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        
        User updateData = new User();
        updateData.setUsername("updated_username");
        updateData.setEmail("updated@test.com");

        // When
        User result = userService.actualizar(1L, updateData);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
