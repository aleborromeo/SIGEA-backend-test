package com.zentry.sigea.module_notificaciones.events.listeners;

import com.zentry.sigea.module_notificaciones.events.domain.InscripcionCreadaEvent;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionEventListenerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private InscripcionEventListener inscripcionEventListener;

    @Test
    void debeEnviarNotificacionDeInscripcion() {
        when(notificacionService.crearNotificacion(any()))
            .thenReturn("OK");

        InscripcionCreadaEvent event =
            new InscripcionCreadaEvent(
                "user-1",
                "actividad-1",
                "ins-1",
                LocalDateTime.now()
            );

        inscripcionEventListener.onInscripcionCreada(event);

        verify(notificacionService, times(1))
            .crearNotificacion(any());
    }
}
