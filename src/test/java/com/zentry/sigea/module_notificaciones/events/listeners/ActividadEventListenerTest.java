package com.zentry.sigea.module_notificaciones.events.listeners;

import com.zentry.sigea.module_notificaciones.events.domain.ActividadCreadaEvent;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;
import com.zentry.sigea.module_usuarios.services.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActividadEventListenerTest {

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ActividadEventListener actividadEventListener;

    @Test
    void debeEnviarNotificacionesACadaUsuario() {
        when(notificacionService.obtenerNombreUsuarioPorId(anyString()))
            .thenReturn("Usuario Test");

        when(notificacionService.crearNotificacion(any()))
            .thenReturn("OK");

        ActividadCreadaEvent event = new ActividadCreadaEvent(
            "actividad-1",
            "Actividad de prueba",
            "Descripción",
            List.of("u1", "u2"),
            LocalDateTime.now()
        );

        actividadEventListener.onActividadCreada(event);

        verify(notificacionService, times(2))
            .crearNotificacion(any());
    }
}
