package com.zentry.sigea.module_usuarios.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;

class SesionEntityTest {

    @Test
    void alCrearEntidad_fechasInicialmenteSonNull() {
        SesionEntity entity = new SesionEntity();

        // Antes de persistir JPA NO ejecuta callbacks
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void settersYGetters_funcionanCorrectamente() {
        SesionEntity entity = new SesionEntity();
        LocalDateTime now = LocalDateTime.now();

        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }
}
