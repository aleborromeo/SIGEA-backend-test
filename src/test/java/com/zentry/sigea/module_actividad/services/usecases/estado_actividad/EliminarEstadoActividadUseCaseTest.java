package com.zentry.sigea.module_actividad.services.usecases.estado_actividad;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IEstadoActividadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EliminarEstadoActividadUseCaseTest {

    @Mock
    private IEstadoActividadRepository estadoActividadRepository;

    @InjectMocks
    private EliminarEstadoActividadUseCase useCase;

    @Test
    void execute_eliminaCuandoExiste() {
        String id = "EST-1";

        when(estadoActividadRepository.findById(id))
                .thenReturn(Optional.of(new EstadoActividadDomainEntity()));

        useCase.execute(id);

        verify(estadoActividadRepository).deleteById(id);
    }

    @Test
    void execute_lanzaExcepcionCuandoNoExiste() {
        String id = "NO-EXISTE";

        when(estadoActividadRepository.findById(id))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(id)
        );

        assertTrue(ex.getMessage().contains("Estado de actividad no encontrado con ID: " + id));
        verify(estadoActividadRepository, never()).deleteById(any());
    }
}
