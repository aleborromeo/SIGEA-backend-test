package com.zentry.sigea.module_notificaciones.infrastructure.database.mappers;

import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.TipoNotificacionEntity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TipoNotificacionMapperTest {

    @Test
    void toEntity_ok() {
        TipoNotificacionDomainEntity domain = new TipoNotificacionDomainEntity();
        domain.setId(UUID.randomUUID().toString());
        domain.setCodigo("CERTIFICADO");
        domain.setEtiqueta("Certificado");

        TipoNotificacionEntity entity =
            TipoNotificacionMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals("CERTIFICADO", entity.getCodigo());
        assertEquals("Certificado", entity.getEtiqueta());
        assertEquals(domain.getId(), entity.getId().toString());
    }

    @Test
    void toDomain_ok() {
        TipoNotificacionEntity entity = new TipoNotificacionEntity();
        entity.setId(UUID.randomUUID());
        entity.setCodigo("PAGO");
        entity.setEtiqueta("Pago");

        TipoNotificacionDomainEntity domain =
            TipoNotificacionMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("PAGO", domain.getCodigo());
        assertEquals("Pago", domain.getEtiqueta());
        assertEquals(entity.getId().toString(), domain.getId());
    }

    @Test
    void toEntity_null() {
        assertNull(TipoNotificacionMapper.toEntity(null));
    }

    @Test
    void toDomain_null() {
        assertNull(TipoNotificacionMapper.toDomain(null));
    }
}
