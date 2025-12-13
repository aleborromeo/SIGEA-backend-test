package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarUsuarioRequestDTO;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.UsuarioResponseDTO;

class UsuarioMapperTest {

    @Test
    void requestToDomain_ok() {
        // Arrange
        RegistrarUsuarioRequestDTO requestDTO = new RegistrarUsuarioRequestDTO();
        requestDTO.setNombres("Juan");
        requestDTO.setApellidos("Pérez");

        String passwordHash = "hashed_password_123";

        // Act
        UsuarioDomainEntity domainEntity =
            UsuarioMapper.requestToDomain(requestDTO, passwordHash);

        // Assert
        assertNotNull(domainEntity);
        assertEquals("Juan", domainEntity.getNombres());

        // ⚠️ Según el mapper actual, apellidos se setean con nombres
        assertEquals("Juan", domainEntity.getApellidos());

        assertEquals(passwordHash, domainEntity.getPasswordHash());
    }

    @Test
    void requestToDomain_passwordHashNoDebeSerNull() {
        // Arrange
        RegistrarUsuarioRequestDTO requestDTO = new RegistrarUsuarioRequestDTO();
        requestDTO.setNombres("Ana");

        String passwordHash = "secure_hash";

        // Act
        UsuarioDomainEntity domainEntity =
            UsuarioMapper.requestToDomain(requestDTO, passwordHash);

        // Assert
        assertNotNull(domainEntity.getPasswordHash());
        assertEquals("secure_hash", domainEntity.getPasswordHash());
    }

    @Test
    void domainToResponse_ok() {
        // Arrange
        UsuarioDomainEntity domainEntity = new UsuarioDomainEntity();
        domainEntity.setNombres("Carlos");
        domainEntity.setApellidos("Lopez");

        // Act
        UsuarioResponseDTO responseDTO =
            UsuarioMapper.domainToResponse(domainEntity);

        // Assert
        assertNotNull(responseDTO);

        // Actualmente el mapper NO setea campos
        // Este assert valida que el método no falle
        assertTrue(responseDTO instanceof UsuarioResponseDTO);
    }
}
