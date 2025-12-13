package com.zentry.sigea.module_notificaciones.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TipoNotificacionDomainEntity")
class TipoNotificacionDomainEntityTest {

    @Test
    @DisplayName("create() debe crear tipo correctamente")
    void create_tipoValido_ok() {
        TipoNotificacionDomainEntity tipo =
            TipoNotificacionDomainEntity.create("CERTIFICADO_GENERADO", "Certificado generado");

        assertEquals("CERTIFICADO_GENERADO", tipo.getCodigo());
        assertEquals("Certificado generado", tipo.getEtiqueta());
    }

    @Test
    @DisplayName("create() convierte código a mayúsculas")
    void create_codigoMinuscula_seConvierte() {
        TipoNotificacionDomainEntity tipo =
            TipoNotificacionDomainEntity.create("inscripcion_confirmada", null);

        assertEquals("INSCRIPCION_CONFIRMADA", tipo.getCodigo());
    }

    @Test
    @DisplayName("create() con código vacío lanza excepción")
    void create_codigoVacio_lanzaExcepcion() {
        assertThrows(
            IllegalArgumentException.class,
            () -> TipoNotificacionDomainEntity.create("   ", "Etiqueta")
        );
    }
}
