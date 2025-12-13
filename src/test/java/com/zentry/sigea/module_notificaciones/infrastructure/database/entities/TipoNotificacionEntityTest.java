package com.zentry.sigea.module_notificaciones.infrastructure.database.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TipoNotificacionEntityTest {

    @Test
    void crearTipoNotificacion_ok() {
        TipoNotificacionEntity entity = new TipoNotificacionEntity();

        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setCodigo("CERTIFICADO");
        entity.setEtiqueta("Certificado");

        assertEquals(id, entity.getId());
        assertEquals("CERTIFICADO", entity.getCodigo());
        assertEquals("Certificado", entity.getEtiqueta());
    }

    @Test
    void entidadInicializaSinErrores() {
        TipoNotificacionEntity entity = new TipoNotificacionEntity();
        assertNotNull(entity);
    }
}
