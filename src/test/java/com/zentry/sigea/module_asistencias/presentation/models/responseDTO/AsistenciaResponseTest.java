package com.zentry.sigea.module_asistencias.presentation.models.responseDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;

class AsistenciaResponseTest {

    @Test
    void constructorVacio_inicializaCamposNull() {
        AsistenciaResponse response = new AsistenciaResponse();

        assertNull(response.getId());
        assertNull(response.getSesionId());
        assertNull(response.getInscripcionId());
        assertNull(response.getPresente());
        assertNull(response.getRegistradoEn());
    }

    @Test
    void constructorConArgs_seteaCorrectamente() {
        LocalDateTime now = LocalDateTime.now();

        AsistenciaResponse response = new AsistenciaResponse(
            "as-1",
            "ses-1",
            "ins-1",
            true,
            now
        );

        assertEquals("as-1", response.getId());
        assertEquals("ses-1", response.getSesionId());
        assertEquals("ins-1", response.getInscripcionId());
        assertTrue(response.getPresente());
        assertEquals(now, response.getRegistradoEn());
    }

    @Test
    void setters_y_getters_funcionan() {
        LocalDateTime now = LocalDateTime.now();

        AsistenciaResponse response = new AsistenciaResponse();

        response.setId("as-9");
        response.setSesionId("ses-9");
        response.setInscripcionId("ins-9");
        response.setPresente(false);
        response.setRegistradoEn(now);

        assertEquals("as-9", response.getId());
        assertEquals("ses-9", response.getSesionId());
        assertEquals("ins-9", response.getInscripcionId());
        assertFalse(response.getPresente());
        assertEquals(now, response.getRegistradoEn());
    }

    @Test
    void fromDomain_creaResponseCorrectamente() {
        LocalDateTime now = LocalDateTime.now();

        AsistenciaDomainEntity domain = AsistenciaDomainEntity.reconstruct(
            "domain-id",
            "sesion-id",
            "inscripcion-id",
            true,
            now
        );

        AsistenciaResponse response =
            AsistenciaResponse.fromDomain(domain, "response-id");

        assertEquals("response-id", response.getId());
        assertEquals("sesion-id", response.getSesionId());
        assertEquals("inscripcion-id", response.getInscripcionId());
        assertTrue(response.getPresente());
        assertEquals(now, response.getRegistradoEn());
    }
}
