package com.zentry.sigea.module_asistencias.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AsistenciaRequestTest {

    @Test
    void constructorVacio_iniciaConPresenteNull() {
        AsistenciaRequest req = new AsistenciaRequest();
        assertNull(req.getPresente());
    }

    @Test
    void constructorConArgs_seteaPresente() {
        AsistenciaRequest req = new AsistenciaRequest(true);
        assertTrue(req.getPresente());
    }

    @Test
    void setter_getter_funcionan() {
        AsistenciaRequest req = new AsistenciaRequest();
        req.setPresente(false);

        assertFalse(req.getPresente());
    }
}
