package com.zentry.sigea.module_notificaciones.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NotificacionDomainEntity")
class NotificacionDomainEntityTest {

    private EstadoNotificacionDomainEntity estado(String codigo) {
        return EstadoNotificacionDomainEntity.create(codigo, codigo);
    }

    private TipoNotificacionDomainEntity tipo(String codigo) {
        return TipoNotificacionDomainEntity.create(codigo, codigo);
    }

    @Test
    @DisplayName("create() debe crear notificación correctamente")
    void create_notificacionValida_ok() {
        NotificacionDomainEntity notificacion =
            NotificacionDomainEntity.create(
                "usuario-1",
                "actividad-1",
                tipo("CERTIFICADO_GENERADO"),
                "Mensaje de prueba",
                estado("PENDIENTE"),
                CanalNotificacion.SISTEMA
            );

        assertNotNull(notificacion);
        assertEquals("usuario-1", notificacion.getUsuarioId());
        assertEquals("Mensaje de prueba", notificacion.getMensaje());
        assertTrue(notificacion.estaPendiente());
        assertNotNull(notificacion.getFechaEnvio());
    }

    @Test
    @DisplayName("cambiarEstado() debe actualizar estado")
    void cambiarEstado_ok() {
        NotificacionDomainEntity notificacion =
            NotificacionDomainEntity.create(
                "usuario",
                null,
                tipo("INSCRIPCION_CONFIRMADA"),
                "Mensaje",
                estado("PENDIENTE"),
                CanalNotificacion.SISTEMA
            );

        notificacion.cambiarEstado(estado("ENVIADA"));

        assertTrue(notificacion.estaEnviada());
    }

    @Test
    @DisplayName("marcarComoLeida() funciona solo con estado LEIDA")
    void marcarComoLeida_ok() {
        NotificacionDomainEntity notificacion =
            NotificacionDomainEntity.create(
                "usuario",
                null,
                tipo("IMPORTANTE"),
                "Mensaje",
                estado("PENDIENTE"),
                CanalNotificacion.SISTEMA
            );

        notificacion.marcarComoLeida(estado("LEIDA"));

        assertTrue(notificacion.estaLeida());
    }

    @Test
    @DisplayName("marcarComoLeida() con estado inválido lanza excepción")
    void marcarComoLeida_estadoInvalido_lanzaExcepcion() {
        NotificacionDomainEntity notificacion =
            NotificacionDomainEntity.create(
                "usuario",
                null,
                tipo("IMPORTANTE"),
                "Mensaje",
                estado("PENDIENTE"),
                CanalNotificacion.SISTEMA
            );

        assertThrows(
            IllegalArgumentException.class,
            () -> notificacion.marcarComoLeida(estado("ENVIADA"))
        );
    }

    @Test
    @DisplayName("esImportante() detecta correctamente")
    void esImportante_funciona() {
        NotificacionDomainEntity notificacion =
            NotificacionDomainEntity.create(
                "usuario",
                null,
                tipo("CERTIFICADO_EMITIDO"),
                "Mensaje",
                estado("ENVIADA"),
                CanalNotificacion.SISTEMA
            );

        assertTrue(notificacion.esImportante());
    }
}
