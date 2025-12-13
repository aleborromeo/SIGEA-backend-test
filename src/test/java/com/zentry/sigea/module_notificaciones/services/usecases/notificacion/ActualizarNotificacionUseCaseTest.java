package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.ActualizarNotificacionRequest;

@ExtendWith(MockitoExtension.class)
class ActualizarNotificacionUseCaseTest {

    @Mock
    private INotificacionRepository notificacionRepository;
    @Mock
    private IEstadoNotificacionRepository estadoRepository;

    @InjectMocks
    private ActualizarNotificacionUseCase useCase;

    @Test
    void execute_actualizaMensajeYEstado() {
        NotificacionDomainEntity notif = new NotificacionDomainEntity();
        EstadoNotificacionDomainEntity estado = new EstadoNotificacionDomainEntity();

        when(notificacionRepository.findById("id")).thenReturn(Optional.of(notif));
        when(estadoRepository.findById("estado")).thenReturn(Optional.of(estado));
        when(notificacionRepository.save(any())).thenReturn(true);

        ActualizarNotificacionRequest req =
            new ActualizarNotificacionRequest("Nuevo mensaje", "estado");

        NotificacionDomainEntity result = useCase.execute("id", req);

        assertEquals("Nuevo mensaje", result.getMensaje());
    }

    @Test
    void execute_idInvalido() {
        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute("", new ActualizarNotificacionRequest()));
    }
}
