package com.zentry.sigea.module_notificaciones.events.listeners;

import com.zentry.sigea.module_notificaciones.events.domain.SesionCreadaEvent;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SesionEventListenerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private SesionEventListener sesionEventListener;

    @Test
    void debeNotificarATodosLosUsuarios() {
        when(notificacionService.crearNotificacion(any()))
            .thenReturn("OK");

        SesionCreadaEvent event = new SesionCreadaEvent(
            "ses-1",
            "actividad-1",
            "Sesión Java",
            LocalDateTime.now(),
            List.of("u1", "u2"),
            LocalDateTime.now(),
            "Aula",
            "Profesor",
            "Presencial",
            null,
            LocalTime.NOON,
            LocalTime.MIDNIGHT,
            "Descripción"
        );

        sesionEventListener.onSesionCreada(event);

        verify(notificacionService, times(2))
            .crearNotificacion(any());
    }
}
