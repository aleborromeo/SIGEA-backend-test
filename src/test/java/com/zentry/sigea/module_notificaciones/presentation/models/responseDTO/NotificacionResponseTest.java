package com.zentry.sigea.module_notificaciones.presentation.models.responseDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_notificaciones.core.entities.CanalNotificacion;
import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;

class NotificacionResponseTest {

    @Test
    void constructorVacio_noLanzaErrores() {
        NotificacionResponse response = new NotificacionResponse();
        assertNotNull(response);
    }

    @Test
    void constructorCompleto_asignaCorrectamenteCampos() {
        LocalDateTime fecha = LocalDateTime.now();

        TipoNotificacionDomainEntity tipo = TipoNotificacionDomainEntity.create(
            "PAGO", "Pago realizado"
        );

        EstadoNotificacionDomainEntity estado = EstadoNotificacionDomainEntity.create(
            "ENVIADA", "Enviada"
        );

        NotificacionResponse response = new NotificacionResponse(
            "1",
            "usuario-1",
            "actividad-1",
            tipo,
            "Mensaje de prueba",
            fecha,
            estado,
            "SISTEMA"
        );

        assertEquals("1", response.getId());
        assertEquals("usuario-1", response.getUsuarioId());
        assertEquals("actividad-1", response.getActividadId());
        assertEquals(tipo, response.getTipoNotificacion());
        assertEquals("Mensaje de prueba", response.getMensaje());
        assertEquals(fecha, response.getFechaEnvio());
        assertEquals(estado, response.getEstadoNotificacion());
        assertEquals("SISTEMA", response.getCanal());
    }

    @Test
    void fromEntity_convierteCorrectamente() {
        LocalDateTime fecha = LocalDateTime.now();

        TipoNotificacionDomainEntity tipo = TipoNotificacionDomainEntity.create(
            "CERTIFICADO", "Certificado generado"
        );

        EstadoNotificacionDomainEntity estado = EstadoNotificacionDomainEntity.create(
            "LEIDA", "Leída"
        );

        NotificacionDomainEntity domain = new NotificacionDomainEntity();
        domain.setId("10");
        domain.setUsuarioId("usuario-10");
        domain.setActividadId("actividad-10");
        domain.setTipoNotificacion(tipo);
        domain.setMensaje("Tu certificado está disponible");
        domain.setFechaEnvio(fecha);
        domain.setEstadoNotificacion(estado);
        domain.setCanal(CanalNotificacion.CORREO);

        NotificacionResponse response = NotificacionResponse.fromEntity(domain);

        assertEquals("10", response.getId());
        assertEquals("usuario-10", response.getUsuarioId());
        assertEquals("actividad-10", response.getActividadId());
        assertEquals(tipo, response.getTipoNotificacion());
        assertEquals("Tu certificado está disponible", response.getMensaje());
        assertEquals(fecha, response.getFechaEnvio());
        assertEquals(estado, response.getEstadoNotificacion());
        assertEquals("CORREO", response.getCanal());
    }

    @Test
    void settersYGetters_funcionanCorrectamente() {
        NotificacionResponse response = new NotificacionResponse();

        response.setId("99");
        response.setUsuarioId("usuario-99");
        response.setActividadId("actividad-99");
        response.setMensaje("Mensaje");
        response.setCanal("WHATSAPP");

        assertEquals("99", response.getId());
        assertEquals("usuario-99", response.getUsuarioId());
        assertEquals("actividad-99", response.getActividadId());
        assertEquals("Mensaje", response.getMensaje());
        assertEquals("WHATSAPP", response.getCanal());
    }
}
