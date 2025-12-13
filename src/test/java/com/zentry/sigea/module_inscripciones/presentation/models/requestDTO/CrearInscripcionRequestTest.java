package com.zentry.sigea.module_inscripciones.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CrearInscripcionRequestTest {

    @Test
    void constructorAndGetters_workCorrectly() {
        LocalDate fecha = LocalDate.of(2025, 1, 10);
        CrearInscripcionRequest req =
            new CrearInscripcionRequest(fecha, "user-1", "act-1", "estado-1");

        assertEquals(fecha, req.getFechaInscripcion());
        assertEquals("user-1", req.getUsuarioId());
        assertEquals("act-1", req.getActividadId());
        assertEquals("estado-1", req.getEstadoId());
    }

    @Test
    void emptyConstructor_allowsJacksonCreation() {
        CrearInscripcionRequest req = new CrearInscripcionRequest();
        assertNotNull(req);
    }
}
