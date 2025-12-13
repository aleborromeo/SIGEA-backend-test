package com.zentry.sigea.module_actividad.services.usecases.tipo_actividad;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.ITipoActividadRepository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.TipoActividadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizarTipoActividadUseCaseTest {

    @Mock
    private ITipoActividadRepository tipoActividadRepository;

    @InjectMocks
    private ActualizarTipoActividadUseCase useCase;

    @Test
    void execute_actualizaYGuardaCuandoExiste() {
        String id = "TIP-1";

        TipoActividadRequest request =
                new TipoActividadRequest("Nuevo nombre", "Nueva descripción");

        TipoActividadDomainEntity existente = new TipoActividadDomainEntity();
        existente.setTipoActividadId(id);
        existente.setNombreActividad("Viejo nombre");
        existente.setDescripcion("Vieja desc");
        existente.setUpdatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));

        when(tipoActividadRepository.findById(id))
                .thenReturn(Optional.of(existente));

        TipoActividadDomainEntity result = useCase.execute(id, request);

        assertEquals("Nuevo nombre", existente.getNombreActividad());
        assertEquals("Nueva descripción", existente.getDescripcion());
        assertNotNull(existente.getUpdatedAt());
        assertTrue(existente.getUpdatedAt().isAfter(LocalDateTime.of(2020, 1, 1, 0, 0)));

        verify(tipoActividadRepository).save(existente);
        assertSame(existente, result);
    }

    @Test
    void execute_lanzaExcepcionSiNoExiste() {
        String id = "NO-EXISTE";
        TipoActividadRequest request =
                new TipoActividadRequest("Nombre", "Desc");

        when(tipoActividadRepository.findById(id))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(id, request)
        );

        assertTrue(ex.getMessage().contains("Tipo de actividad no encontrado"));
        verify(tipoActividadRepository, never()).save(any());
    }
}
