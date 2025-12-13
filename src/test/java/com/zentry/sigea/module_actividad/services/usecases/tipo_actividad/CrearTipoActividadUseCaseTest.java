package com.zentry.sigea.module_actividad.services.usecases.tipo_actividad;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.ITipoActividadRepository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.TipoActividadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearTipoActividadUseCaseTest {

    @Mock
    private ITipoActividadRepository tipoActividadRepository;

    @InjectMocks
    private CrearTipoActividadUseCase useCase;

    @Test
    void execute_creaYDevuelveMensajeExitoCuandoSaveTrue() {
        TipoActividadRequest request =
                new TipoActividadRequest("Seminario", "Descripción X");

        when(tipoActividadRepository.save(any(TipoActividadDomainEntity.class)))
                .thenReturn(true);

        ArgumentCaptor<TipoActividadDomainEntity> captor =
                ArgumentCaptor.forClass(TipoActividadDomainEntity.class);

        String result = useCase.execute(request);

        verify(tipoActividadRepository).save(captor.capture());
        TipoActividadDomainEntity saved = captor.getValue();

        assertEquals("Seminario", saved.getNombreActividad());
        assertEquals("Descripción X", saved.getDescripcion());

        assertEquals(
                "El tipo de actividad Seminario se registro correctamente.",
                result
        );
    }

    @Test
    void execute_devuelveMensajeErrorCuandoSaveFalse() {
        TipoActividadRequest request =
                new TipoActividadRequest("Charla", "Desc");

        when(tipoActividadRepository.save(any(TipoActividadDomainEntity.class)))
                .thenReturn(false);

        String result = useCase.execute(request);

        assertEquals(
                "Ocurrio un error al registrar el tipo de actividad.",
                result
        );
    }

    @Test
    void execute_lanzaErrorCuandoNombreEsNulo() {
        TipoActividadRequest request =
                new TipoActividadRequest(null, "Desc");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(request)
        );
        assertTrue(ex.getMessage().contains("El nombre de la actividad es obligatorio"));
        verify(tipoActividadRepository, never()).save(any());
    }

    @Test
    void execute_lanzaErrorCuandoNombreEsBlanco() {
        TipoActividadRequest request =
                new TipoActividadRequest("   ", "Desc");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(request)
        );
        assertTrue(ex.getMessage().contains("El nombre de la actividad es obligatorio"));
        verify(tipoActividadRepository, never()).save(any());
    }
}
