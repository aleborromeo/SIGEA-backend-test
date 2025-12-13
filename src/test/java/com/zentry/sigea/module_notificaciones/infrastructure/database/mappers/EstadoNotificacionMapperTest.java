package com.zentry.sigea.module_notificaciones.infrastructure.database.mappers;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.EstadoNotificacionEntity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EstadoNotificacionMapperTest {

    @Test
    void toEntity_ok() {
        EstadoNotificacionDomainEntity domain = new EstadoNotificacionDomainEntity();
        domain.setId(UUID.randomUUID().toString());
        domain.setCodigo("PENDIENTE");
        domain.setEtiqueta("Pendiente");

        EstadoNotificacionEntity entity =
            EstadoNotificacionMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getCodigo(), entity.getCodigo());
        assertEquals(domain.getEtiqueta(), entity.getEtiqueta());
        assertEquals(domain.getId(), entity.getId().toString());
    }

    @Test
    void toDomain_ok() {
        EstadoNotificacionEntity entity = new EstadoNotificacionEntity();
        entity.setId(UUID.randomUUID());
        entity.setCodigo("ENVIADA");
        entity.setEtiqueta("Enviada");

        EstadoNotificacionDomainEntity domain =
            EstadoNotificacionMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("ENVIADA", domain.getCodigo());
        assertEquals("Enviada", domain.getEtiqueta());
        assertEquals(entity.getId().toString(), domain.getId());
    }

    @Test
    void toEntity_null() {
        assertNull(EstadoNotificacionMapper.toEntity(null));
    }

    @Test
    void toDomain_null() {
        assertNull(EstadoNotificacionMapper.toDomain(null));
    }
}
