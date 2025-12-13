package com.zentry.sigea.module_notificaciones.events.listeners;

import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificadoEventListenerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private CertificadoEventListener certificadoEventListener;

    @Test
    void debeEnviarNotificacionDeCertificado() {
        when(notificacionService.crearNotificacion(any()))
            .thenReturn("OK");

        CertificadoGeneradoEvent event = new CertificadoGeneradoEvent(
            "user-1",
            "cert-1",
            "actividad-1",
            "Curso Java",
            "CERT-1234",
            LocalDate.now(),
            CertificadoGeneradoEvent.EstadoCertificado.EMITIDO,
            "http://pdf",
            "asistencia-1",
            LocalDateTime.now()
        );

        certificadoEventListener.onCertificadoGenerado(event);

        verify(notificacionService, times(1))
            .crearNotificacion(any());
    }
}
