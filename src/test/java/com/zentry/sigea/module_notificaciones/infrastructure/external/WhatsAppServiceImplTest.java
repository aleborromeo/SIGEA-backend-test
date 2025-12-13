package com.zentry.sigea.module_notificaciones.infrastructure.external;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WhatsAppServiceImplTest {

    private WhatsAppServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new WhatsAppServiceImpl();

        // Simular configuración sin Spring
        ReflectionTestUtils.setField(service, "whatsappEnabled", false);
    }

    @Test
    void enviar_whatsappDeshabilitado_devuelveFalse() {
        NotificacionDomainEntity notificacion = new NotificacionDomainEntity();
        notificacion.setId("1");
        notificacion.setMensaje("Mensaje prueba");
        notificacion.setFechaEnvio(LocalDateTime.now());

        boolean result = service.enviar(
            notificacion,
            "+51999999999",
            "Juan"
        );

        assertFalse(result);
    }

    @Test
    void enviar_telefonoNulo_devuelveFalse() {
        NotificacionDomainEntity notificacion = new NotificacionDomainEntity();
        notificacion.setId("2");
        notificacion.setMensaje("Mensaje");
        notificacion.setFechaEnvio(LocalDateTime.now());

        boolean result = service.enviar(
            notificacion,
            null,
            "Usuario"
        );

        assertFalse(result);
    }
}
