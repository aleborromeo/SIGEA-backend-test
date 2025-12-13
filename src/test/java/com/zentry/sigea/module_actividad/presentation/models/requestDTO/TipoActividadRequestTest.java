package com.zentry.sigea.module_actividad.presentation.models.requestDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoActividadRequestTest {

    @Test
    void testConstructorAndGetters() {
        TipoActividadRequest request = new TipoActividadRequest("Seminario", "Evento académico");

        assertEquals("Seminario", request.getNombreActividad());
        assertEquals("Evento académico", request.getDescripcion());
    }

    @Test
    void testConstructorAllowsNullValues() {
        TipoActividadRequest request = new TipoActividadRequest(null, null);

        assertNull(request.getNombreActividad());
        assertNull(request.getDescripcion());
    }

    @Test
    void testConstructorAllowsEmptyStrings() {
        TipoActividadRequest request = new TipoActividadRequest("", "");

        assertEquals("", request.getNombreActividad());
        assertEquals("", request.getDescripcion());
    }

    @Test
    void testImmutability() {
        TipoActividadRequest request =
                new TipoActividadRequest("Workshop", "Taller técnico");

        // Verifica que no existan setters
        assertThrows(NoSuchMethodException.class,
                () -> request.getClass().getMethod("setNombreActividad", String.class));

        assertThrows(NoSuchMethodException.class,
                () -> request.getClass().getMethod("setDescripcion", String.class));
    }
}
