package com.zentry.sigea.module_inscripciones.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class EstadoInscripcionEntityTest {

    @Test
    void testGettersAndSetters() {
        EstadoInscripcionEntity entity = new EstadoInscripcionEntity();

        UUID id = UUID.randomUUID();
        String codigo = "PENDIENTE";
        String etiqueta = "Pendiente";

        entity.setId(id);
        entity.setCodigo(codigo);
        entity.setEtiqueta(etiqueta);

        assertEquals(id, entity.getId());
        assertEquals(codigo, entity.getCodigo());
        assertEquals(etiqueta, entity.getEtiqueta());
    }

    @Test
    void testNoArgsConstructorAndDefaultValues() {
        EstadoInscripcionEntity entity = new EstadoInscripcionEntity();

        // Por defecto no debería tener datos
        assertNull(entity.getId());
        assertNull(entity.getCodigo());
        assertNull(entity.getEtiqueta());
    }
}
