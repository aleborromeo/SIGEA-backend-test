package com.zentry.sigea.module_actividad.services.usecases.tipo_actividad;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.ITipoActividadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EliminarTipoActividadUseCaseTest {

    @Mock
    private ITipoActividadRepository tipoActividadRepository;

    @InjectMocks
    private EliminarTipoActividadUseCase useCase;

    @Test
    void execute_eliminaCuandoExiste() {
        String id = "TIP-1";

        when(tipoActividadRepository.findById(id))
                .thenReturn(Optional.of(new TipoActividadDomainEntity()));

        useCase.execute(id);

        verify(tipoActividadRepository).deleteById(id);
    }

    @Test
    void execute_lanzaExcepcionCuandoNoExiste() {
        String id = "NO-EXISTE";

        when(tipoActividadRepository.findById(id))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(id)
        );

        assertTrue(ex.getMessage().contains("Tipo de actividad no encontrado con ID: " + id));
        verify(tipoActividadRepository, never()).deleteById(any());
    }
}
