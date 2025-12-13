package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarParticipanteRequestDTO;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;

class RegistrarParticipanteMapperTest {

    @Test
    void requestToDomain_ok() {
        // Arrange
        RegistrarParticipanteRequestDTO dto =
            mock(RegistrarParticipanteRequestDTO.class);

        when(dto.getCorreo()).thenReturn("participante@zentry.com");
        when(dto.getNombres()).thenReturn("Juan");
        when(dto.getApellidos()).thenReturn("Perez");

        // Act
        UsuarioDomainEntity usuario =
            RegistrarParticipanteMapper.requestToDomain(dto);

        // Assert
        assertNotNull(usuario);
    }
}
