package com.zentry.sigea.module_actividad.core.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstadoActividadDomainEntityTest {

    @Test
    void testSettersAndGetters() {
        EstadoActividadDomainEntity entity = new EstadoActividadDomainEntity();

        entity.setEstadoActividadId("E1");
        entity.setCodigo("ACT");
        entity.setEtiqueta("Activo");

        assertEquals("E1", entity.getEstadoActividadId());
        assertEquals("ACT", entity.getCodigo());
        assertEquals("Activo", entity.getEtiqueta());
    }

    @Test
    void testCreateValid() {
        EstadoActividadDomainEntity entity = EstadoActividadDomainEntity.create("INA", "Inactivo");

        assertEquals("INA", entity.getCodigo());
        assertEquals("Inactivo", entity.getEtiqueta());
        assertNull(entity.getEstadoActividadId());
    }

    @Test
    void testCreateFailsWhenCodigoNull() {
        assertThrows(IllegalArgumentException.class,
                () -> EstadoActividadDomainEntity.create(null, "Etiqueta"));
    }

    @Test
    void testCreateFailsWhenCodigoBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> EstadoActividadDomainEntity.create(" ", "Etiqueta"));
    }
}
