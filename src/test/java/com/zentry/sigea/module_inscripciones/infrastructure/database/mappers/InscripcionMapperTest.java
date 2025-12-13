package com.zentry.sigea.module_inscripciones.infrastructure.database.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.EstadoInscripcionEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

class InscripcionMapperTest {

    // ---------- toEntity ----------

    @Test
    void toEntity_mapsAllFieldsCorrectly() {
        // Domain
        InscripcionDomainEntity domain = new InscripcionDomainEntity();
        LocalDate fecha = LocalDate.of(2025, 1, 10);
        domain.setFechaInscripcion(fecha);

        EstadoInscripcionDomainEntity estadoDomain = EstadoInscripcionDomainEntity.create(
                "PENDIENTE", "Pendiente"
        );
        estadoDomain.setId(UUID.randomUUID().toString());
        domain.setEstadoInscripcionDomainEntity(estadoDomain);

        // Relaciones JPA
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(UUID.randomUUID());

        ActividadEntity actividadEntity = new ActividadEntity();
        actividadEntity.setId(UUID.randomUUID());

        // Act
        InscripcionEntity entity = InscripcionMapper.toEntity(
                domain,
                usuarioEntity,
                actividadEntity
        );

        // Assert
        assertNotNull(entity);
        assertEquals(fecha, entity.getFechaInscripcion());
        assertSame(usuarioEntity, entity.getUsuario());
        assertSame(actividadEntity, entity.getActividad());
        assertNotNull(entity.getEstadoInscripcion());
        assertEquals("PENDIENTE", entity.getEstadoInscripcion().getCodigo());
        assertEquals("Pendiente", entity.getEstadoInscripcion().getEtiqueta());
    }

    @Test
    void toEntity_returnsNullWhenDomainIsNull() {
        InscripcionEntity entity = InscripcionMapper.toEntity(null, null, null);
        assertNull(entity);
    }

    // ---------- toDomain ----------

    @Test
    void toDomain_mapsAllFieldsCorrectly() {
        // JPA
        InscripcionEntity entity = new InscripcionEntity();
        UUID inscId = UUID.randomUUID();
        entity.setId(inscId);
        LocalDate fecha = LocalDate.of(2025, 2, 1);
        entity.setFechaInscripcion(fecha);

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        UUID usuarioId = UUID.randomUUID();
        usuarioEntity.setId(usuarioId);
        entity.setUsuario(usuarioEntity);

        ActividadEntity actividadEntity = new ActividadEntity();
        UUID actividadId = UUID.randomUUID();
        actividadEntity.setId(actividadId);
        entity.setActividad(actividadEntity);

        EstadoInscripcionEntity estadoEntity = new EstadoInscripcionEntity();
        UUID estadoId = UUID.randomUUID();
        estadoEntity.setId(estadoId);
        estadoEntity.setCodigo("CONFIRMADA");
        estadoEntity.setEtiqueta("Confirmada");
        entity.setEstadoInscripcion(estadoEntity);

        // Act
        InscripcionDomainEntity domain = InscripcionMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(inscId.toString(), domain.getId());
        assertEquals(fecha, domain.getFechaInscripcion());
        assertEquals(usuarioId.toString(), domain.getUsuarioId());
        assertEquals(actividadId.toString(), domain.getActividadId());
        assertNotNull(domain.getEstadoInscripcionDomainEntity());
        assertEquals("CONFIRMADA", domain.getEstadoInscripcionDomainEntity().getCodigo());
        assertEquals("Confirmada", domain.getEstadoInscripcionDomainEntity().getEtiqueta());
    }

    @Test
    void toDomain_handlesNullRelationsSafely() {
        InscripcionEntity entity = new InscripcionEntity();
        entity.setId(null);
        entity.setFechaInscripcion(LocalDate.now());
        entity.setUsuario(null);
        entity.setActividad(null);
        entity.setEstadoInscripcion(null);

        InscripcionDomainEntity domain = InscripcionMapper.toDomain(entity);

        assertNotNull(domain);
        assertNull(domain.getId());
        assertNull(domain.getUsuarioId());
        assertNull(domain.getActividadId());
        assertNull(domain.getEstadoInscripcionDomainEntity());
    }

    @Test
    void toDomain_returnsNullWhenEntityIsNull() {
        InscripcionDomainEntity domain = InscripcionMapper.toDomain(null);
        assertNull(domain);
    }
}
