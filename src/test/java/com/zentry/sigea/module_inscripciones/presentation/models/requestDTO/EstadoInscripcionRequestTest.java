package com.zentry.sigea.module_inscripciones.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EstadoInscripcionRequestTest {

    @Test
    void constructorAndGetters_workCorrectly() {
        EstadoInscripcionRequest req =
            new EstadoInscripcionRequest("PEN", "Pendiente");

        assertEquals("PEN", req.getCodigo());
        assertEquals("Pendiente", req.getEtiqueta());
    }

    @Test
    void emptyConstructor_allowsJacksonCreation() {
        EstadoInscripcionRequest req = new EstadoInscripcionRequest();
        assertNotNull(req);
    }
}
