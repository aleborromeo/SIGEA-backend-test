package com.zentry.sigea.module_actividad.presentation.models.requestDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoActividadRequestTest {

    @Test
    void testEmptyConstructor() {
        EstadoActividadRequest request = new EstadoActividadRequest();
        assertNotNull(request);
    }

    @Test
    void testFullConstructor() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");

        assertEquals("ACT", request.getCodigo());
        assertEquals("Activo", request.getEtiqueta());
    }

    @Test
    void testSettersAndGetters() {
        EstadoActividadRequest request = new EstadoActividadRequest();

        request.setCodigo("INA");
        request.setEtiqueta("Inactivo");

        assertEquals("INA", request.getCodigo());
        assertEquals("Inactivo", request.getEtiqueta());
    }

    @Test
    void testNullValues() {
        EstadoActividadRequest request = new EstadoActividadRequest();

        request.setCodigo(null);
        request.setEtiqueta(null);

        assertNull(request.getCodigo());
        assertNull(request.getEtiqueta());
    }

    @Test
    void testEmptyStrings() {
        EstadoActividadRequest request = new EstadoActividadRequest();

        request.setCodigo("");
        request.setEtiqueta("");

        assertEquals("", request.getCodigo());
        assertEquals("", request.getEtiqueta());
    }
}
