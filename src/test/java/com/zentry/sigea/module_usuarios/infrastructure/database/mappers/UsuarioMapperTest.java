package com.zentry.sigea.module_usuarios.infrastructure.database.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

class UsuarioMapperTest {

    @Test
    void toEntity_mapeaCorrectamente() {
        // Arrange
        UsuarioDomainEntity domain = UsuarioDomainEntity.create(
            "Juan",
            "Perez",
            "juan@mail.com",
            "hash",
            "12345678",
            true,
            "999999999",
            "+51"
        );
        domain.setId(UUID.randomUUID().toString());

        // Act
        UsuarioEntity entity = UsuarioMapper.toEntity(domain);

        // Assert
        assertEquals("Juan", entity.getNombres());
        assertEquals("Perez", entity.getApellidos());
        assertEquals("juan@mail.com", entity.getCorreo());
        assertEquals("hash", entity.getPasswordHash());
        assertNotNull(entity.getId());
    }

    @Test
    void toDomain_mapeaCorrectamente() {
        // Arrange
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(UUID.randomUUID());
        entity.setNombres("Ana");
        entity.setApellidos("Lopez");
        entity.setCorreo("ana@mail.com");
        entity.setPasswordHash("hash2");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // Act
        UsuarioDomainEntity domain = UsuarioMapper.toDomain(entity);

        // Assert
        assertEquals("Ana", domain.getNombres());
        assertEquals("Lopez", domain.getApellidos());
        assertEquals("ana@mail.com", domain.getCorreo());
        assertEquals(entity.getId().toString(), domain.getId());
    }
}
