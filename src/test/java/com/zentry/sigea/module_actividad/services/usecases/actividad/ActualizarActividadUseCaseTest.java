package com.zentry.sigea.module_actividad.services.usecases.actividad;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IActividadRespository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.ActividadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizarActividadUseCaseTest {

    @Mock
    private IActividadRespository actividadRepository;

    @InjectMocks
    private ActualizarActividadUseCase useCase;

    @Test
    void execute_actualizaYGuardaCuandoExiste_conEstadoYTipo() {
        String id = "ACT-1";

        // Request con todos los campos
        ActividadRequest request = new ActividadRequest();
        request.setTitulo("Nuevo título");
        request.setDescripcion("Nueva descripción");
        request.setFechaInicio(LocalDate.of(2024, 1, 10));
        request.setFechaFin(LocalDate.of(2024, 1, 20));
        request.setUbicacion("Auditorio");
        EstadoActividadDomainEntity estado = new EstadoActividadDomainEntity();
        TipoActividadDomainEntity tipo = new TipoActividadDomainEntity();
        request.setEstado(estado);
        request.setTipoActividad(tipo);

        // Entidad de dominio mockeada
        ActividadDomainEntity actividad = mock(ActividadDomainEntity.class);
        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad));
        when(actividadRepository.save(actividad)).thenReturn(actividad);

        ActividadDomainEntity result = useCase.execute(id, request);

        // Verifica que se haya actualizado la info básica
        verify(actividad).updateInfo(
                request.getTitulo(),
                request.getDescripcion(),
                request.getFechaInicio(),
                request.getFechaFin(),
                request.getUbicacion()
        );

        // Verifica que se haya cambiado el estado y tipo
        verify(actividad).changeStatus(estado);
        verify(actividad).setTipoActividadDomainEntity(tipo);

        // Verifica que se haya guardado
        verify(actividadRepository).save(actividad);

        // Retorna la misma instancia
        assertSame(actividad, result);
    }

    @Test
    void execute_noLlamaAChangeStatusNiSetTipoSiSonNull() {
        String id = "ACT-2";

        ActividadRequest request = new ActividadRequest();
        request.setTitulo("Titulo");
        request.setDescripcion("Desc");
        request.setFechaInicio(LocalDate.of(2024, 2, 1));
        request.setFechaFin(LocalDate.of(2024, 2, 5));
        request.setUbicacion("Sala 1");
        // estado y tipoActividad en null por defecto

        ActividadDomainEntity actividad = mock(ActividadDomainEntity.class);
        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad));
        when(actividadRepository.save(actividad)).thenReturn(actividad);

        useCase.execute(id, request);

        verify(actividad).updateInfo(
                request.getTitulo(),
                request.getDescripcion(),
                request.getFechaInicio(),
                request.getFechaFin(),
                request.getUbicacion()
        );

        // Como en el request están en null, no deben llamarse
        verify(actividad, never()).changeStatus(any());
        verify(actividad, never()).setTipoActividadDomainEntity(any());

        verify(actividadRepository).save(actividad);
    }

    @Test
    void execute_lanzaExcepcionSiNoExisteActividad() {
        String id = "NO-EXISTE";
        ActividadRequest request = new ActividadRequest();

        when(actividadRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(id, request)
        );

        assertTrue(ex.getMessage().contains("Actividad no encontrada con ID: " + id));
        verify(actividadRepository, never()).save(any());
    }
}
