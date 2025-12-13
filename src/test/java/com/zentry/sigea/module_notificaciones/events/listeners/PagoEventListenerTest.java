package com.zentry.sigea.module_notificaciones.events.listeners;

import com.zentry.sigea.module_notificaciones.events.domain.PagoCompletadoEvent;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoEventListenerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private PagoEventListener pagoEventListener;

    @Test
    void debeEnviarNotificacionDePago() {
        when(notificacionService.crearNotificacion(any()))
            .thenReturn("OK");

        PagoCompletadoEvent event = new PagoCompletadoEvent(
            "user-1",
            "pago-1",
            BigDecimal.TEN,
            "USD",
            "actividad-1",
            "Curso Java",
            PagoCompletadoEvent.ConceptoPago.INSCRIPCION,
            null,
            "CMP-01",
            "TARJETA",
            LocalDateTime.now()
        );

        pagoEventListener.onPagoCompletado(event);

        verify(notificacionService, times(1))
            .crearNotificacion(any());
    }
}
