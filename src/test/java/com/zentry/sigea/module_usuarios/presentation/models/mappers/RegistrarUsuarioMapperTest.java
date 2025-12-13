package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarUsuarioRequestDTO;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;

class RegistrarUsuarioMapperTest {

    @Test
    void requestToDomain_ok() {
        // Arrange
        RegistrarUsuarioRequestDTO dto = mock(RegistrarUsuarioRequestDTO.class);
        when(dto.getCorreo()).thenReturn("test@zentry.com");
        when(dto.getNombres()).thenReturn("Juan");
        when(dto.getApellidos()).thenReturn("Perez");

        // Act
        UsuarioDomainEntity usuario =
            RegistrarUsuarioMapper.requestToDomain(dto);

        // Assert
        assertNotNull(usuario);
        assertEquals("test@zentry.com", usuario.getCorreo());
        assertEquals("Juan", usuario.getNombres());
        assertEquals("Perez", usuario.getApellidos());
    }
}
