package com.zentry.sigea.module_inscripciones.infrastructure.database.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.EstadoInscripcionEntity;

class EstadoInscripcionMapperTest {

    // ---------- toEntity ----------

    @Test
    void toEntity_mapsDomainToEntityCorrectly() {
        EstadoInscripcionDomainEntity domain = EstadoInscripcionDomainEntity.create(
                "CONFIRMADA",
                "Confirmada"
        );
        domain.setId(UUID.randomUUID().toString());

        EstadoInscripcionEntity entity = EstadoInscripcionMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getCodigo(), entity.getCodigo());
        assertEquals(domain.getEtiqueta(), entity.getEtiqueta());
        // El ID NO SE MAPEÓ → lo asigna JPA
        assertNull(entity.getId());
    }

    @Test
    void toEntity_returnsNullWhenDomainIsNull() {
        EstadoInscripcionEntity entity = EstadoInscripcionMapper.toEntity(null);
        assertNull(entity);
    }

    // ---------- toDomain ----------

    @Test
    void toDomain_mapsEntityToDomainCorrectly() {
        EstadoInscripcionEntity entity = new EstadoInscripcionEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setCodigo("PENDIENTE");
        entity.setEtiqueta("Pendiente");

        EstadoInscripcionDomainEntity domain = EstadoInscripcionMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id.toString(), domain.getId());
        assertEquals("PENDIENTE", domain.getCodigo());
        assertEquals("Pendiente", domain.getEtiqueta());
    }

    @Test
    void toDomain_returnsNullWhenEntityIsNull() {
        EstadoInscripcionDomainEntity domain = EstadoInscripcionMapper.toDomain(null);
        assertNull(domain);
    }

    @Test
    void toDomain_handlesNullIdSafely() {
        EstadoInscripcionEntity entity = new EstadoInscripcionEntity();
        entity.setId(null);
        entity.setCodigo("CANCELADA");
        entity.setEtiqueta("Cancelada");

        EstadoInscripcionDomainEntity domain = EstadoInscripcionMapper.toDomain(entity);

        assertNotNull(domain);
        assertNull(domain.getId()); // porque entity.getId() es null
        assertEquals("CANCELADA", domain.getCodigo());
        assertEquals("Cancelada", domain.getEtiqueta());
    }
}
