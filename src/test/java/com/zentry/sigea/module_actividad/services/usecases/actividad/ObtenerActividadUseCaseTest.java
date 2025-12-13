package com.zentry.sigea.module_actividad.services.usecases.actividad;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IActividadRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObtenerActividadUseCaseTest {

    @Mock
    IActividadRespository repo;

    @InjectMocks
    ObtenerActividadUseCase useCase;

    @Test
    void execute_ok() {
        ActividadDomainEntity act = new ActividadDomainEntity();
        when(repo.findById("ACT")).thenReturn(Optional.of(act));

        Optional<ActividadDomainEntity> result = useCase.execute("ACT");

        assertTrue(result.isPresent());
        assertEquals(act, result.get());
    }

    @Test
    void execute_lanzaSiIdEsNulo() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));

        assertTrue(ex.getMessage().contains("no puede ser nulo"));
    }

    @Test
    void execute_devuelveEmptySiNoExiste() {
        when(repo.findById("ACT")).thenReturn(Optional.empty());

        Optional<ActividadDomainEntity> result = useCase.execute("ACT");

        assertTrue(result.isEmpty());
    }
}
