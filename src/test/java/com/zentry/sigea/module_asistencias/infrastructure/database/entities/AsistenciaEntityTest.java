package com.zentry.sigea.module_asistencias.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;

class AsistenciaEntityTest {

    @Test
    void defaults_alCrearInstancia_presenteFalse_y_registradoEnNoNulo() {
        AsistenciaEntity entity = new AsistenciaEntity();

        assertNotNull(entity.getRegistradoEn(), "registradoEn debe inicializarse por defecto");
        assertFalse(entity.getPresente(), "presente debe ser false por defecto");
    }

    @Test
    void gettersSetters_funcionanCorrectamente() {
        AsistenciaEntity entity = new AsistenciaEntity();

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);

        SesionEntity sesion = new SesionEntity();
        InscripcionEntity inscripcion = new InscripcionEntity();

        entity.setId(id);
        entity.setRegistradoEn(now);
        entity.setPresente(true);
        entity.setSesion(sesion);
        entity.setInscripcion(inscripcion);

        assertEquals(id, entity.getId());
        assertEquals(now, entity.getRegistradoEn());
        assertTrue(entity.getPresente());
        assertSame(sesion, entity.getSesion());
        assertSame(inscripcion, entity.getInscripcion());
    }
}
