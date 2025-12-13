package com.zentry.sigea.module_notificaciones.events.listeners;

import com.zentry.sigea.module_notificaciones.events.domain.ComunicacionPublicadaEvent;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComunicacionEventListenerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private ComunicacionEventListener comunicacionEventListener;

    @Test
    void debeEnviarNotificacionesABroadcast() {
        when(notificacionService.crearNotificacion(any()))
            .thenReturn("OK");

        ComunicacionPublicadaEvent event =
            new ComunicacionPublicadaEvent(
                List.of("u1", "u2", "u3"),
                "actividad-1",
                "Anuncio",
                "Mensaje importante",
                ComunicacionPublicadaEvent.TipoComunicacion.ANUNCIO_ACTIVIDAD,
                LocalDateTime.now()
            );

        comunicacionEventListener.onComunicacionPublicada(event);

        verify(notificacionService, times(3))
            .crearNotificacion(any());
    }
}
