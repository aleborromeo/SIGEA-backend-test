package com.zentry.sigea.module_actividad.services.usecases.actividad;

import com.zentry.sigea.module_actividad.core.repositories.IActividadRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EliminarActividadUseCaseTest {

    @Mock
    IActividadRespository repo;

    @InjectMocks
    EliminarActividadUseCase useCase;

    @Test
    void execute_eliminaCuandoExiste() {
        when(repo.existsById("A1")).thenReturn(true);

        useCase.execute("A1");

        verify(repo).deleteById("A1");
    }

    @Test
    void execute_lanzaErrorCuandoNoExiste() {
        when(repo.existsById("A1")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> useCase.execute("A1"));

        assertTrue(ex.getMessage().contains("No existe actividad"));
        verify(repo, never()).deleteById(any());
    }
}
