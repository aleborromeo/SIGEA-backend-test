package com.zentry.sigea.module_usuarios.services;

import com.zentry.sigea.module_usuarios.services.serviceDTO.RegistrarAsistenciaServiceDTO;
import com.zentry.sigea.module_usuarios.services.usecases.organizador.RegistrarAsistenciaMasivaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.UUID;

// ↓↓↓ CORRECCIÓN APLICADA ↓↓↓
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
// ↑↑↑ CORRECCIÓN APLICADA ↑↑↑
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para OrganizadorService (Delegación de registro de asistencia)
 */
@DisplayName("OrganizadorService Tests")
class OrganizadorServiceTest {

    @Mock private RegistrarAsistenciaMasivaUseCase registrarAsistenciaUseCase;

    @InjectMocks
    private OrganizadorService organizadorService;

    private RegistrarAsistenciaServiceDTO mockRequest;
    private final String MESSAGE_SUCCESS = "Asistencia registrada con exito";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        mockRequest = new RegistrarAsistenciaServiceDTO();
        mockRequest.setSesionId(UUID.randomUUID().toString());
        mockRequest.setRegistrarAsistenciaItemServiceDTOs(Collections.emptyList());

        when(registrarAsistenciaUseCase.execute(any(RegistrarAsistenciaServiceDTO.class))).thenReturn(MESSAGE_SUCCESS);
    }

    @Test
    @DisplayName("registrarAsistencia - Debe delegar la ejecución al RegistrarAsistenciaMasivaUseCase")
    void registrarAsistencia_debeDelegarAUsecase() {
        // ACT
        String resultado = organizadorService.registrarAsistencia(mockRequest);

        // ASSERT
        assertEquals(MESSAGE_SUCCESS, resultado);
        verify(registrarAsistenciaUseCase, times(1)).execute(mockRequest);
    }
    
    @Test
    @DisplayName("registrarAsistencia - Debe propagar excepciones del Caso de Uso")
    void registrarAsistencia_debePropagarExcepciones() {
        // ARRANGE
        when(registrarAsistenciaUseCase.execute(any(RegistrarAsistenciaServiceDTO.class)))
            .thenThrow(new IllegalArgumentException("ID de sesión inválido"));

        // ACT & ASSERT
        // Ahora assertThrows está disponible
        assertThrows(IllegalArgumentException.class, () -> {
            organizadorService.registrarAsistencia(mockRequest);
        });

        verify(registrarAsistenciaUseCase, times(1)).execute(mockRequest);
    }
}