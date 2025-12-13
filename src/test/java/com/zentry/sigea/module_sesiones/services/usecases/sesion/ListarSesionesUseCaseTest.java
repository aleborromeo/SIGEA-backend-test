package com.zentry.sigea.module_sesiones.services.usecases.sesion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_sesiones.core.entities.SesionDomainEntity;
import com.zentry.sigea.module_sesiones.core.repositories.ISesionRepository;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity.Modalidad;

@ExtendWith(MockitoExtension.class)
class ListarSesionesUseCaseTest {

    @Mock
    private ISesionRepository sesionRepository;

    @InjectMocks
    private ListarSesionesUseCase useCase;

    @Test
    void listarTodas() {
        when(sesionRepository.findAll())
            .thenReturn(List.of(mock(SesionDomainEntity.class)));

        List<SesionDomainEntity> result = useCase.execute();

        assertEquals(1, result.size());
        verify(sesionRepository).findAll();
    }

    @Test
    void listarPorActividad() {
        String actividadId = UUID.randomUUID().toString();

        when(sesionRepository.findByActividadId(actividadId))
            .thenReturn(List.of(mock(SesionDomainEntity.class)));

        List<SesionDomainEntity> result =
            useCase.executeByActividad(actividadId);

        assertEquals(1, result.size());
        verify(sesionRepository).findByActividadId(actividadId);
    }

    @Test
    void listarPorActividadYModalidad() {
        UUID actividadId = UUID.randomUUID();

        when(sesionRepository.findByActividadIdAndModalidad(
                eq(actividadId),
                eq(Modalidad.VIRTUAL)
        )).thenReturn(List.of(mock(SesionDomainEntity.class)));

        List<SesionDomainEntity> result =
            useCase.executeByActividadAndModalidad(
                actividadId.toString(),
                Modalidad.VIRTUAL
            );

        assertEquals(1, result.size());
        verify(sesionRepository)
            .findByActividadIdAndModalidad(actividadId, Modalidad.VIRTUAL);
    }
}
