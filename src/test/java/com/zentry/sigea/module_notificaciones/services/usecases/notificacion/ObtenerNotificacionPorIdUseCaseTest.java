package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;

@ExtendWith(MockitoExtension.class)
class ObtenerNotificacionPorIdUseCaseTest {

    @Mock
    private INotificacionRepository repository;

    @InjectMocks
    private ObtenerNotificacionPorIdUseCase useCase;

    @Test
    void execute_ok() {
        when(repository.findById("id"))
            .thenReturn(Optional.of(new NotificacionDomainEntity()));

        assertNotNull(useCase.execute("id"));
    }

    @Test
    void execute_noExiste() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute("id"));
    }
}
