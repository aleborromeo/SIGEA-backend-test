package com.zentry.sigea.module_notificaciones.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EstadoNotificacionDomainEntity")
class EstadoNotificacionDomainEntityTest {

    @Test
    @DisplayName("create() debe crear estado correctamente")
    void create_estadoValido_ok() {
        EstadoNotificacionDomainEntity estado =
            EstadoNotificacionDomainEntity.create("ENVIADA", "Enviada");

        assertNotNull(estado);
        assertEquals("ENVIADA", estado.getCodigo());
        assertEquals("Enviada", estado.getEtiqueta());
    }

    @Test
    @DisplayName("create() convierte código a mayúsculas")
    void create_codigoMinuscula_seConvierte() {
        EstadoNotificacionDomainEntity estado =
            EstadoNotificacionDomainEntity.create("leida", "Leída");

        assertEquals("LEIDA", estado.getCodigo());
    }

    @Test
    @DisplayName("create() con código null lanza excepción")
    void create_codigoNull_lanzaExcepcion() {
        assertThrows(
            IllegalArgumentException.class,
            () -> EstadoNotificacionDomainEntity.create(null, "Etiqueta")
        );
    }

    @Test
    @DisplayName("create() con código largo lanza excepción")
    void create_codigoMuyLargo_lanzaExcepcion() {
        String codigo = "A".repeat(31);

        assertThrows(
            IllegalArgumentException.class,
            () -> EstadoNotificacionDomainEntity.create(codigo, "Etiqueta")
        );
    }

    @Test
    @DisplayName("create() con etiqueta muy larga lanza excepción")
    void create_etiquetaMuyLarga_lanzaExcepcion() {
        String etiqueta = "B".repeat(61);

        assertThrows(
            IllegalArgumentException.class,
            () -> EstadoNotificacionDomainEntity.create("VALIDO", etiqueta)
        );
    }
}
