package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.presentation.models.requestDTO.RegistrarAsistenciaRequestDTO;

class RegistrarAsistenciaMapperTest {

    @Test
    void requestToService_ok() {
        // Arrange
        RegistrarAsistenciaRequestDTO dto =
            mock(RegistrarAsistenciaRequestDTO.class);

        // Act
        var serviceDTO =
            RegistrarAsistenciaMapper.requestToService(dto);

        // Assert
        assertNotNull(serviceDTO);
    }
}
