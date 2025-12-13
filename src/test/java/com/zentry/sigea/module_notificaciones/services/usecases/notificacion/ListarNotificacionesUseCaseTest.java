package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;

@ExtendWith(MockitoExtension.class)
class ListarNotificacionesUseCaseTest {

    @Mock
    private INotificacionRepository repository;

    @InjectMocks
    private ListarNotificacionesUseCase useCase;

    @Test
    void execute_ok() {
        when(repository.findAll()).thenReturn(List.of());

        assertNotNull(useCase.execute());
    }

    @Test
    void executeByUsuarioId_invalido() {
        assertThrows(IllegalArgumentException.class,
            () -> useCase.executeByUsuarioId(""));
    }
}
