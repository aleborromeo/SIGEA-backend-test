package com.zentry.sigea.module_asistencias.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RegistrarAsistenciaRequestTest {

    @Test
    void constructorVacio_iniciaCamposNull() {
        RegistrarAsistenciaRequest req = new RegistrarAsistenciaRequest();

        assertNull(req.getSesionId());
        assertNull(req.getInscripcionId());
        assertNull(req.getPresente());
    }

    @Test
    void constructorConArgs_seteaCampos() {
        RegistrarAsistenciaRequest req =
            new RegistrarAsistenciaRequest("ses-1", "ins-1", true);

        assertEquals("ses-1", req.getSesionId());
        assertEquals("ins-1", req.getInscripcionId());
        assertTrue(req.getPresente());
    }

    @Test
    void setters_getters_funcionan() {
        RegistrarAsistenciaRequest req = new RegistrarAsistenciaRequest();

        req.setSesionId("ses-9");
        req.setInscripcionId("ins-9");
        req.setPresente(false);

        assertEquals("ses-9", req.getSesionId());
        assertEquals("ins-9", req.getInscripcionId());
        assertFalse(req.getPresente());
    }
}
