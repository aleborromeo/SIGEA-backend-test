package com.zentry.sigea.module_notificaciones.infrastructure.external;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    private EmailServiceImpl emailService;
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() throws Exception {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailServiceImpl(mailSender);

        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Inyectar flags privados
        setField("emailEnabled", false);
    }

    @Test
    void enviar_emailDeshabilitado_devuelveFalse() {
        NotificacionDomainEntity notificacion = crearNotificacion();

        boolean result = emailService.enviar(
            notificacion,
            "test@email.com",
            "Juan"
        );

        assertFalse(result);
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void enviarCodigoVerificacion_emailDeshabilitado_devuelveFalse() {
        boolean result = emailService.enviarCodigoVerificacion(
            "test@email.com",
            "Juan",
            123456
        );

        assertFalse(result);
    }

    /* ---------- helpers ---------- */

    private NotificacionDomainEntity crearNotificacion() {
        TipoNotificacionDomainEntity tipo = new TipoNotificacionDomainEntity();
        tipo.setCodigo("INSCRIPCION");

        NotificacionDomainEntity n = new NotificacionDomainEntity();
        n.setMensaje("Mensaje prueba");
        n.setFechaEnvio(LocalDateTime.now());
        n.setTipoNotificacion(tipo);
        return n;
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = EmailServiceImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(emailService, value);
    }
}
