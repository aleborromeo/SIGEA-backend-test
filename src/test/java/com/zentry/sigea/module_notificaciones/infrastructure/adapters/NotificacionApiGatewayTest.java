package com.zentry.sigea.module_notificaciones.infrastructure.adapters;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionApiGatewayTest {

    private final NotificacionApiGateway gateway = new NotificacionApiGateway();

    @Test
    void enviarCorreo_debeRetornarFalse() {
        NotificacionDomainEntity notificacion = new NotificacionDomainEntity();
        notificacion.setMensaje("Mensaje de prueba");

        boolean resultado = gateway.enviarCorreo(notificacion, "test@mail.com");

        assertFalse(resultado);
    }

    @Test
    void enviarWhatsApp_debeRetornarFalse() {
        NotificacionDomainEntity notificacion = new NotificacionDomainEntity();
        notificacion.setMensaje("Mensaje WhatsApp");

        boolean resultado = gateway.enviarWhatsApp(notificacion, "+51999999999");

        assertFalse(resultado);
    }

    @Test
    void emailServiceDisponible_debeRetornarFalse() {
        assertFalse(gateway.emailServiceDisponible());
    }

    @Test
    void whatsAppServiceDisponible_debeRetornarFalse() {
        assertFalse(gateway.whatsAppServiceDisponible());
    }
}
