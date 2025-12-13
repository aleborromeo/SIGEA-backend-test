package com.zentry.sigea.module_actividad.infrastructure.database.mappers;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.TipoActividadEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TipoActividadMapperTest {

    @Test
    void toDomain_debeConvertirEntityADomain() {
        // ARRANGE
        TipoActividadEntity entity = new TipoActividadEntity();
        entity.setId(UUID.randomUUID());
        entity.setNombreActividad("Taller");
        entity.setDescripcion("Descripción del taller");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // ACT
        TipoActividadDomainEntity domain = TipoActividadMapper.toDomain(entity);

        // ASSERT
        assertNotNull(domain);
        assertEquals(entity.getId().toString(), domain.getTipoActividadId());
        assertEquals(entity.getNombreActividad(), domain.getNombreActividad());
        assertEquals(entity.getDescripcion(), domain.getDescripcion());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), domain.getUpdatedAt());
    }

    @Test
    void toDomain_debeRetornarNull_cuandoEntityEsNull() {
        // ACT
        TipoActividadDomainEntity domain = TipoActividadMapper.toDomain(null);

        // ASSERT
        assertNull(domain);
    }

    @Test
    void toEntity_debeConvertirDomainAEntity() {
        // ARRANGE
        TipoActividadDomainEntity domain = TipoActividadDomainEntity.create("Taller", "Descripción");
        domain.setTipoActividadId(UUID.randomUUID().toString());

        // ACT
        TipoActividadEntity entity = TipoActividadMapper.toEntity(domain);

        // ASSERT
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(domain.getNombreActividad(), entity.getNombreActividad());
        assertEquals(domain.getDescripcion(), entity.getDescripcion());
        assertEquals(domain.getCreatedAt(), entity.getCreatedAt());
        assertEquals(domain.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    void toEntity_debeRetornarNull_cuandoDomainEsNull() {
        // ACT
        TipoActividadEntity entity = TipoActividadMapper.toEntity(null);

        // ASSERT
        assertNull(entity);
    }

    @Test
    void toEntity_debeAsignarIdSiExiste() {
        // ARRANGE
        String uuidString = UUID.randomUUID().toString();
        TipoActividadDomainEntity domain = TipoActividadDomainEntity.create("Taller", "Desc");
        domain.setTipoActividadId(uuidString);

        // ACT
        TipoActividadEntity entity = TipoActividadMapper.toEntity(domain);

        // ASSERT
        assertEquals(uuidString, entity.getId().toString());
    }
}