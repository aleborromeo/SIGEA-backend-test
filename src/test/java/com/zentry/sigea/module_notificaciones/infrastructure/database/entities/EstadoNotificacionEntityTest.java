package com.zentry.sigea.module_notificaciones.infrastructure.database.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EstadoNotificacionEntityTest {

    @Test
    void crearEstadoNotificacion_ok() {
        EstadoNotificacionEntity entity = new EstadoNotificacionEntity();

        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setCodigo("ENVIADA");
        entity.setEtiqueta("Enviada");

        assertEquals(id, entity.getId());
        assertEquals("ENVIADA", entity.getCodigo());
        assertEquals("Enviada", entity.getEtiqueta());
    }

    @Test
    void entidadInicializaSinErrores() {
        EstadoNotificacionEntity entity = new EstadoNotificacionEntity();
        assertNotNull(entity);
    }
}
