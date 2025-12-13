package com.zentry.sigea.module_usuarios.services.usecases.organizador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_usuarios.services.serviceDTO.RegistrarAsistenciaItemServiceDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.RegistrarAsistenciaServiceDTO;

@ExtendWith(MockitoExtension.class)
class RegistrarAsistenciaUseCaseTest {

    @Mock
    private IAsistenciaRepository asistenciaRepository;

    @InjectMocks
    private RegistrarAsistenciaUseCase registrarAsistenciaUseCase;

    @Test
    void execute_registers_attendance_and_returns_message() {
        // Arrange
        RegistrarAsistenciaItemServiceDTO item =
            new RegistrarAsistenciaItemServiceDTO(
                "insc-2",
                false,
                Optional.of(LocalDateTime.now())
            );

        RegistrarAsistenciaServiceDTO dto =
            new RegistrarAsistenciaServiceDTO();
        dto.setSesionId("sesion-2");
        dto.setRegistrarAsistenciaItemServiceDTOs(List.of(item));

        // Act
        String result =
            registrarAsistenciaUseCase.execute(dto);

        // Assert
        assertEquals("Asistencia registrada con exito", result);

        verify(asistenciaRepository, times(1))
            .saveAll(anyList());
    }
}
