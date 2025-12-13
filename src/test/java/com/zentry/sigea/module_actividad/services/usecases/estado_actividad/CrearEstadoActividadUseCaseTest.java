package com.zentry.sigea.module_actividad.services.usecases.estado_actividad;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IEstadoActividadRepository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.EstadoActividadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearEstadoActividadUseCaseTest {

    @Mock
    private IEstadoActividadRepository estadoActividadRepository;

    @InjectMocks
    private CrearEstadoActividadUseCase useCase;

    @Test
    void execute_retornaMensajeExitoCuandoSaveDevuelveTrue() {
        EstadoActividadRequest request =
                new EstadoActividadRequest("ACT", "Activo");

        // Capturar la entidad que se guarda
        ArgumentCaptor<EstadoActividadDomainEntity> captor =
                ArgumentCaptor.forClass(EstadoActividadDomainEntity.class);

        when(estadoActividadRepository.save(any(EstadoActividadDomainEntity.class)))
                .thenReturn(true);

        String result = useCase.execute(request);

        verify(estadoActividadRepository).save(captor.capture());
        EstadoActividadDomainEntity saved = captor.getValue();

        assertEquals("ACT", saved.getCodigo());
        assertEquals("Activo", saved.getEtiqueta());
        assertEquals(
                "El estado de actividad Activo se registro correctamente",
                result
        );
    }

    @Test
    void execute_retornaMensajeErrorCuandoSaveDevuelveFalse() {
        EstadoActividadRequest request =
                new EstadoActividadRequest("INA", "Inactivo");

        when(estadoActividadRepository.save(any(EstadoActividadDomainEntity.class)))
                .thenReturn(false);

        String result = useCase.execute(request);

        assertEquals(
                "Ocurrio un problema al registrar el estado de actividad",
                result
        );
    }
}
