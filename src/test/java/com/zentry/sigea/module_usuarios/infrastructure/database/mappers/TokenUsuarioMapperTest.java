package com.zentry.sigea.module_usuarios.infrastructure.database.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.TokenUsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

class TokenUsuarioMapperTest {

    @Test
    void toEntity_mapeaCorrectamente() {
        // Arrange
        TokenUsuarioDomainEntity domain = new TokenUsuarioDomainEntity();
        domain.setToken("token-123");
        domain.setExpiryDate(Instant.now().plusSeconds(600));

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(UUID.randomUUID());

        // Act
        TokenUsuarioEntity entity =
            TokenUsuarioMapper.toEntity(domain, usuario);

        // Assert
        assertEquals("token-123", entity.getToken());
        assertEquals(usuario, entity.getUsuario());
        assertNotNull(entity.getExpiryDate());
    }

    @Test
    void toDomain_mapeaCorrectamente() {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(UUID.randomUUID());

        TokenUsuarioEntity entity = new TokenUsuarioEntity();
        entity.setId(UUID.randomUUID());
        entity.setToken("token-456");
        entity.setUsuario(usuario);
        entity.setExpiryDate(Instant.now().plusSeconds(300));

        // Act
        TokenUsuarioDomainEntity domain =
            TokenUsuarioMapper.toDomain(entity);

        // Assert
        assertEquals(entity.getId().toString(), domain.getId());
        assertEquals("token-456", domain.getToken());
        assertEquals(usuario.getId().toString(), domain.getUsuarioId());
        assertNotNull(domain.getExpiryDate());
    }
}
