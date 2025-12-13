package com.zentry.sigea.module_usuarios.infrastructure.database.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.core.entities.CodigoVerificacionDomainEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.CodigoVerificacionEntity;

class CodigoVerificacionMapperTest {

    @Test
    void toEntity_mapeaCorrectamente() {
        // Arrange
        CodigoVerificacionDomainEntity domain =
            CodigoVerificacionDomainEntity.create("123456", "test@mail.com");

        // Act
        CodigoVerificacionEntity entity =
            CodigoVerificacionMapper.toEntity(domain);

        // Assert
        assertEquals("123456", entity.getCodigo());
        assertEquals("test@mail.com", entity.getCorreo());
        assertNotNull(entity.getExpiresAt());
    }

    @Test
    void toDomain_mapeaCorrectamente() {
        // Arrange
        CodigoVerificacionEntity entity = new CodigoVerificacionEntity();
        entity.setCodigo("654321");
        entity.setCorreo("otro@mail.com");
        entity.setExpiresAt(Instant.now().plusSeconds(300));

        // Act
        CodigoVerificacionDomainEntity domain =
            CodigoVerificacionMapper.toDomain(entity);

        // Assert
        assertEquals("654321", domain.getCodigo());
        assertEquals("otro@mail.com", domain.getCorreo());
        assertNotNull(domain.getExpiresAt());
    }
}
