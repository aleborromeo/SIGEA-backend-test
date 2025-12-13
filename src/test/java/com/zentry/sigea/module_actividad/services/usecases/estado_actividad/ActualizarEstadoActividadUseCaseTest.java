package com.zentry.sigea.module_actividad.services.usecases.estado_actividad;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IEstadoActividadRepository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.EstadoActividadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizarEstadoActividadUseCaseTest {

    @Mock
    private IEstadoActividadRepository estadoActividadRepository;

    @InjectMocks
    private ActualizarEstadoActividadUseCase useCase;

    @Test
    void execute_actualizaYGuardaCuandoExiste() {
        String id = "EST-1";

        EstadoActividadRequest request =
                new EstadoActividadRequest("ACT", "Activo");

        EstadoActividadDomainEntity existente = new EstadoActividadDomainEntity();
        existente.setEstadoActividadId(id);
        existente.setCodigo("OLD");
        existente.setEtiqueta("Viejo");

        when(estadoActividadRepository.findById(id))
                .thenReturn(Optional.of(existente));

        EstadoActividadDomainEntity result = useCase.execute(id, request);

        // Se actualizan campos
        assertEquals("ACT", existente.getCodigo());
        assertEquals("Activo", existente.getEtiqueta());
        // Se guarda
        verify(estadoActividadRepository).save(existente);
        // Devuelve la misma instancia
        assertSame(existente, result);
    }

    @Test
    void execute_lanzaExcepcionSiNoExiste() {
        String id = "NO-EXISTE";
        EstadoActividadRequest request =
                new EstadoActividadRequest("ACT", "Activo");

        when(estadoActividadRepository.findById(id))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(id, request)
        );

        assertTrue(ex.getMessage().contains("Actividad no encontrada con ID: " + id));
        verify(estadoActividadRepository, never()).save(any());
    }
}
