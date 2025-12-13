package com.zentry.sigea.module_actividad.core.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class TipoActividadDomainEntityTest {

    @Test
    void testSettersAndGetters() {
        TipoActividadDomainEntity entity = new TipoActividadDomainEntity();

        entity.setTipoActividadId("T1");
        entity.setNombreActividad("Charla");
        entity.setDescripcion("Desc X");

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertEquals("T1", entity.getTipoActividadId());
        assertEquals("Charla", entity.getNombreActividad());
        assertEquals("Desc X", entity.getDescripcion());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testCreateValid() {
        TipoActividadDomainEntity entity =
                TipoActividadDomainEntity.create("Seminario", "Evento académico");

        assertEquals("Seminario", entity.getNombreActividad());
        assertEquals("Evento académico", entity.getDescripcion());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());
    }

    @Test
    void testCreateAllowsNullDescription() {
        TipoActividadDomainEntity entity =
                TipoActividadDomainEntity.create("Seminario", null);

        assertNull(entity.getDescripcion());
        assertNotNull(entity.getCreatedAt());
    }
}
