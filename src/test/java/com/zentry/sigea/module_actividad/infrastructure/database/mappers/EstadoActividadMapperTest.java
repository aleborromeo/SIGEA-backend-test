package com.zentry.sigea.module_actividad.infrastructure.database.mappers;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.EstadoActividadEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase EstadoActividadMapper
 */
class EstadoActividadMapperTest { // No necesita @SpringBootTest

    @Test
    void toEntity_debeMapearDomainAEntityCorrectamente() {
        // ARRANGE
        EstadoActividadDomainEntity domain = EstadoActividadDomainEntity.create("BORRADOR", "Borrador de actividad");
        domain.setEstadoActividadId(UUID.randomUUID().toString()); // Simulamos un ID

        // ACT
        EstadoActividadEntity entity = EstadoActividadMapper.toEntity(domain);

        // ASSERT
        assertNotNull(entity);
        assertEquals("BORRADOR", entity.getCodigo());
        assertEquals(domain.getEstadoActividadId(), entity.getId().toString());
    }

    @Test
    void toDomain_debeMapearEntityADomainCorrectamente() {
        // ARRANGE
        EstadoActividadEntity entity = new EstadoActividadEntity();
        entity.setId(UUID.randomUUID());
        entity.setCodigo("FINALIZADA");
        entity.setEtiqueta("Actividad concluida");

        // ACT
        EstadoActividadDomainEntity domain = EstadoActividadMapper.toDomain(entity);

        // ASSERT
        assertNotNull(domain);
        assertEquals("FINALIZADA", domain.getCodigo());
        assertEquals(entity.getId().toString(), domain.getEstadoActividadId());
    }

    @Test
    void toEntity_manejaEntidadNula() {
        assertNull(EstadoActividadMapper.toEntity(null));
    }
    
    @Test
    void toDomain_manejaEntidadJPANula() {
        assertNull(EstadoActividadMapper.toDomain(null));
    }
}