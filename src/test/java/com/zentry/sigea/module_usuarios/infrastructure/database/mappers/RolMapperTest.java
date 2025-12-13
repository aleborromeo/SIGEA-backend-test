package com.zentry.sigea.module_usuarios.infrastructure.database.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.RolEntity;

class RolMapperTest {

    @Test
    void toEntity_mapeaCorrectamente() {
        // Arrange
        RolDomainEntity domain =
            RolDomainEntity.create("ADMIN", "Administrador");

        // Act
        RolEntity entity = RolMapper.toEntity(domain);

        // Assert
        assertEquals("ADMIN", entity.getNombreRol());
        assertEquals("Administrador", entity.getDescripcion());
    }

    @Test
    void toDomain_mapeaCorrectamente() {
        // Arrange
        RolEntity entity = new RolEntity();
        entity.setNombreRol("USER");
        entity.setDescripcion("Usuario");

        // Act
        RolDomainEntity domain = RolMapper.toDomain(entity);

        // Assert
        assertEquals("USER", domain.getNombreRol());
        assertEquals("Usuario", domain.getDescripcion());
    }
}
