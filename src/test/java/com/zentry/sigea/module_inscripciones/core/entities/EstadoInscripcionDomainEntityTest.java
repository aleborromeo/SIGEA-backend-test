package com.zentry.sigea.module_inscripciones.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EstadoInscripcionDomainEntity")
class EstadoInscripcionDomainEntityTest {

    @Test
    @DisplayName("create() debe crear estado correctamente")
    void create_estadoValido_ok() {
        EstadoInscripcionDomainEntity estado =
            EstadoInscripcionDomainEntity.create("CONFIRMADA", "Confirmada");

        assertNotNull(estado);
        assertEquals("CONFIRMADA", estado.getCodigo());
        assertEquals("Confirmada", estado.getEtiqueta());
    }

    @Test
    @DisplayName("create() con código null debe lanzar excepción")
    void create_codigoNull_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> EstadoInscripcionDomainEntity.create(null, "Etiqueta")
        );

        assertEquals("El código no puede ser nulo o vacío", ex.getMessage());
    }

    @Test
    @DisplayName("create() con código vacío debe lanzar excepción")
    void create_codigoVacio_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> EstadoInscripcionDomainEntity.create("   ", "Etiqueta")
        );

        assertEquals("El código no puede ser nulo o vacío", ex.getMessage());
    }
}
