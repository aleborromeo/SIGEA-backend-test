package com.zentry.sigea.module_actividad.presentation.models.responseDTO;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoActividadResponseTest {

    @Test
    void testEmptyConstructor() {
        TipoActividadResponse response = new TipoActividadResponse();
        assertNotNull(response);
    }

    @Test
    void testFullConstructor() {
        TipoActividadResponse response =
                new TipoActividadResponse("T1", "Taller", "Descripción X");

        assertEquals("T1", response.getId());
        assertEquals("Taller", response.getNombreActividad());
        assertEquals("Descripción X", response.getDescripcion());
    }

    @Test
    void testSetters() {
        TipoActividadResponse response = new TipoActividadResponse();

        response.setId("T50");

        assertEquals("T50", response.getId());
    }

    @Test
    void testNullValues() {
        TipoActividadResponse response = new TipoActividadResponse();

        response.setId(null);

        assertNull(response.getId());
    }

    @Test
    void testEmptyString() {
        TipoActividadResponse response = new TipoActividadResponse();

        response.setId("");

        assertEquals("", response.getId());
    }

    @Test
    void testFromEntity() {
        TipoActividadDomainEntity domain = new TipoActividadDomainEntity();
        domain.setTipoActividadId("T9");
        domain.setNombreActividad("Seminario");
        domain.setDescripcion("Académico");

        TipoActividadResponse response = TipoActividadResponse.fromEntity(domain);

        assertEquals("T9", response.getId());
        assertEquals("Seminario", response.getNombreActividad());
        assertEquals("Académico", response.getDescripcion());
    }

    @Test
    void testFromEntityWithNulls() {
        TipoActividadDomainEntity domain = new TipoActividadDomainEntity();

        TipoActividadResponse response = TipoActividadResponse.fromEntity(domain);

        assertNull(response.getId());
        assertNull(response.getNombreActividad());
        assertNull(response.getDescripcion());
    }
}
