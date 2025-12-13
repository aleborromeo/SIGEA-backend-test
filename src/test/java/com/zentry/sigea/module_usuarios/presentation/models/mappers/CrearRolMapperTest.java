package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.CrearRolRequestDTO;
import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;

class CrearRolMapperTest {

    @Test
    void requestToDomain_ok() {
        // Arrange
        CrearRolRequestDTO dto = mock(CrearRolRequestDTO.class);
        when(dto.getNombreRol()).thenReturn("ADMIN");
        when(dto.getDescripcion()).thenReturn("Rol administrador");

        // Act
        RolDomainEntity rol = CrearRolMapper.requestToDomain(dto);

        // Assert
        assertNotNull(rol);
    }
}
