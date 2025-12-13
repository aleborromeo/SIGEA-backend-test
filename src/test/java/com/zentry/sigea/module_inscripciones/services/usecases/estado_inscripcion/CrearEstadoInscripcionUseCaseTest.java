package com.zentry.sigea.module_inscripciones.services.usecases.estado_inscripcion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IEstadoInscripcionRepository;
import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.EstadoInscripcionRequest;

class CrearEstadoInscripcionUseCaseTest {

    @Test
    void execute_shouldThrowException_whenCodigoAlreadyExists() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase useCase = new CrearEstadoInscripcionUseCase(repository);

        EstadoInscripcionRequest request = new EstadoInscripcionRequest("CONFIRMADA", "Confirmada");

        when(repository.findByCodigo("CONFIRMADA"))
                .thenReturn(Optional.of(new EstadoInscripcionDomainEntity()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(request)
        );

        assertEquals("Ya existe un estado de inscripción con el código: CONFIRMADA", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void execute_shouldSaveAndReturnSuccessMessage_whenValid() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase useCase = new CrearEstadoInscripcionUseCase(repository);

        EstadoInscripcionRequest request = new EstadoInscripcionRequest("PENDIENTE", "Pendiente");

        when(repository.findByCodigo("PENDIENTE")).thenReturn(Optional.empty());
        when(repository.save(any(EstadoInscripcionDomainEntity.class))).thenReturn(true);

        String result = useCase.execute(request);

        assertEquals("Estado de inscripción registrado con éxito", result);

        ArgumentCaptor<EstadoInscripcionDomainEntity> captor =
                ArgumentCaptor.forClass(EstadoInscripcionDomainEntity.class);

        verify(repository).save(captor.capture());

        EstadoInscripcionDomainEntity saved = captor.getValue();
        assertEquals("PENDIENTE", saved.getCodigo());
        assertEquals("Pendiente", saved.getEtiqueta());
    }

    @Test
    void execute_shouldReturnErrorMessage_whenSaveFails() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase useCase = new CrearEstadoInscripcionUseCase(repository);

        EstadoInscripcionRequest request = new EstadoInscripcionRequest("CANCELADA", "Cancelada");

        when(repository.findByCodigo("CANCELADA")).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(false);

        String result = useCase.execute(request);

        assertEquals("Algo salió mal al guardar el estado de inscripción", result);
    }
}
