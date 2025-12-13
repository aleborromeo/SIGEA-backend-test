package com.zentry.sigea.module_inscripciones.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class InscripcionRequestTest {

    @Test
    void constructorAndGetters_workCorrectly() {
        LocalDate fecha = LocalDate.of(2025, 2, 1);

        InscripcionRequest req =
            new InscripcionRequest(fecha, "estado-1", "user-1", "act-1");

        assertEquals(fecha, req.getFechaInscripcion());
        assertEquals("estado-1", req.getEstadoId());
        assertEquals("user-1", req.getUsuarioId());
        assertEquals("act-1", req.getActividadId());
    }

    @Test
    void emptyConstructor_allowsJacksonCreation() {
        InscripcionRequest req = new InscripcionRequest();
        assertNotNull(req);
    }
}
