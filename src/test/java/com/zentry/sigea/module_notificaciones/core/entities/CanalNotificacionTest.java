package com.zentry.sigea.module_notificaciones.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CanalNotificacion")
class CanalNotificacionTest {

    @Test
    @DisplayName("fromString() debe convertir correctamente")
    void fromString_valido_ok() {
        assertEquals(CanalNotificacion.SISTEMA, CanalNotificacion.fromString("SISTEMA"));
        assertEquals(CanalNotificacion.CORREO, CanalNotificacion.fromString("correo"));
        assertEquals(CanalNotificacion.WHATSAPP, CanalNotificacion.fromString("WhAtSaPp"));
    }

    @Test
    @DisplayName("fromString() con null debe lanzar excepción")
    void fromString_null_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> CanalNotificacion.fromString(null)
        );

        assertEquals("El canal no puede ser nulo", ex.getMessage());
    }

    @Test
    @DisplayName("fromString() con valor inválido debe lanzar excepción")
    void fromString_invalido_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> CanalNotificacion.fromString("FAX")
        );

        assertTrue(ex.getMessage().contains("Canal inválido"));
    }

    @Test
    @DisplayName("requiereContacto() funciona correctamente")
    void requiereContacto_funciona() {
        assertFalse(CanalNotificacion.SISTEMA.requiereContacto());
        assertTrue(CanalNotificacion.CORREO.requiereContacto());
        assertTrue(CanalNotificacion.WHATSAPP.requiereContacto());
    }
}
