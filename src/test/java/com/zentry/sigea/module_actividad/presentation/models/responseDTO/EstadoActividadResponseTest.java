package com.zentry.sigea.module_actividad.presentation.models.responseDTO;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoActividadResponseTest {

    @Test
    void testEmptyConstructor() {
        EstadoActividadResponse response = new EstadoActividadResponse();
        assertNotNull(response);
    }

    @Test
    void testFullConstructor() {
        EstadoActividadResponse response =
                new EstadoActividadResponse("ID1", "ACT", "Activo");

        assertEquals("ID1", response.getId());
        assertEquals("ACT", response.getCodigo());
        assertEquals("Activo", response.getEtiqueta());
    }

    @Test
    void testSettersAndGetters() {
        EstadoActividadResponse response = new EstadoActividadResponse();

        response.setId("E10");
        response.setCodigo("INA");
        response.setEtiqueta("Inactivo");

        assertEquals("E10", response.getId());
        assertEquals("INA", response.getCodigo());
        assertEquals("Inactivo", response.getEtiqueta());
    }

    @Test
    void testNullValues() {
        EstadoActividadResponse response = new EstadoActividadResponse();

        response.setId(null);
        response.setCodigo(null);
        response.setEtiqueta(null);

        assertNull(response.getId());
        assertNull(response.getCodigo());
        assertNull(response.getEtiqueta());
    }

    @Test
    void testEmptyStrings() {
        EstadoActividadResponse response = new EstadoActividadResponse();

        response.setId("");
        response.setCodigo("");
        response.setEtiqueta("");

        assertEquals("", response.getId());
        assertEquals("", response.getCodigo());
        assertEquals("", response.getEtiqueta());
    }

    @Test
    void testFromEntity() {
        EstadoActividadDomainEntity domain = new EstadoActividadDomainEntity();
        domain.setEstadoActividadId("EST1");
        domain.setCodigo("ACT");
        domain.setEtiqueta("Activo");

        EstadoActividadResponse response = EstadoActividadResponse.fromEntity(domain);

        assertEquals("EST1", response.getId());
        assertEquals("ACT", response.getCodigo());
        assertEquals("Activo", response.getEtiqueta());
    }

    @Test
    void testFromEntityWithNulls() {
        EstadoActividadDomainEntity domain = new EstadoActividadDomainEntity();

        EstadoActividadResponse response = EstadoActividadResponse.fromEntity(domain);

        assertNull(response.getId());
        assertNull(response.getCodigo());
        assertNull(response.getEtiqueta());
    }
}
