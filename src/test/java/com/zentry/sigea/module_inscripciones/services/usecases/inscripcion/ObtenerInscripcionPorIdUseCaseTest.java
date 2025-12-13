package com.zentry.sigea.module_inscripciones.services.usecases.inscripcion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;

class ObtenerInscripcionPorIdUseCaseTest {

    @Test
    void execute_devuelveInscripcionCuandoExiste() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        ObtenerInscripcionPorIdUseCase useCase =
                new ObtenerInscripcionPorIdUseCase(inscripcionRepository);

        InscripcionDomainEntity inscripcion = new InscripcionDomainEntity();
        inscripcion.setId("ins-1");

        when(inscripcionRepository.findById("ins-1"))
                .thenReturn(Optional.of(inscripcion));

        Optional<InscripcionDomainEntity> result = useCase.execute("ins-1");

        assertTrue(result.isPresent());
        assertEquals("ins-1", result.get().getId());
        verify(inscripcionRepository).findById("ins-1");
    }

    @Test
    void execute_devuelveEmptyCuandoNoExiste() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        ObtenerInscripcionPorIdUseCase useCase =
                new ObtenerInscripcionPorIdUseCase(inscripcionRepository);

        when(inscripcionRepository.findById("ins-x"))
                .thenReturn(Optional.empty());

        Optional<InscripcionDomainEntity> result = useCase.execute("ins-x");

        assertTrue(result.isEmpty());
        verify(inscripcionRepository).findById("ins-x");
    }
}
