package com.zentry.sigea.module_inscripciones.services.usecases.estado_inscripcion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IEstadoInscripcionRepository;

@ExtendWith(MockitoExtension.class)
class EliminarEstadoInscripcionUseCaseTest {

    @Mock
    private IEstadoInscripcionRepository estadoInscripcionRepository;

    @InjectMocks
    private EliminarEstadoInscripcionUseCase eliminarEstadoInscripcionUseCase;

    private String estadoId;

    @BeforeEach
    void setUp() {
        estadoId = "estado-123";
    }

    @Test
    void execute_estadoExiste_eliminaCorrectamente() {
        // Arrange
        EstadoInscripcionDomainEntity estado =
            mock(EstadoInscripcionDomainEntity.class);

        when(estadoInscripcionRepository.findById(estadoId))
            .thenReturn(Optional.of(estado));

        // Act
        eliminarEstadoInscripcionUseCase.execute(estadoId);

        // Assert
        verify(estadoInscripcionRepository).deleteById(estadoId);
    }


    @Test
    void execute_estadoNoExiste_lanzaExcepcion() {
        // Arrange
        when(estadoInscripcionRepository.findById(estadoId))
            .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> eliminarEstadoInscripcionUseCase.execute(estadoId)
        );

        verify(estadoInscripcionRepository).findById(estadoId);
        verify(estadoInscripcionRepository, never()).deleteById(any());

        // (Opcional) validar mensaje
        assert exception.getMessage().contains(estadoId);
    }
}
