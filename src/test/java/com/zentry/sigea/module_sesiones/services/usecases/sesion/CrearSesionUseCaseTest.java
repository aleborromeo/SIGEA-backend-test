package com.zentry.sigea.module_sesiones.services.usecases.sesion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_sesiones.core.entities.SesionDomainEntity;
import com.zentry.sigea.module_sesiones.core.repositories.ISesionRepository;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity.Modalidad;
import com.zentry.sigea.module_sesiones.presentacion.models.CrearSesionRequest;

@ExtendWith(MockitoExtension.class)
class CrearSesionUseCaseTest {

    @Mock
    private ISesionRepository sesionRepository;

    @InjectMocks
    private CrearSesionUseCase useCase;

    @Test
    void crearSesion_ok() {
        CrearSesionRequest request = mock(CrearSesionRequest.class);

        when(request.getActividadId()).thenReturn("act-1");
        when(request.getTitulo()).thenReturn("Sesión Test");
        when(request.getDescripcion()).thenReturn("Desc");
        when(request.getFechaSesion()).thenReturn(LocalDateTime.now().plusDays(1));
        when(request.getHoraInicio()).thenReturn(LocalTime.of(10, 0));
        when(request.getHoraFin()).thenReturn(LocalTime.of(12, 0));
        when(request.getPonente()).thenReturn("Ponente");
        when(request.getModalidad()).thenReturn(Modalidad.VIRTUAL);
        when(request.getLugarSesion()).thenReturn("Virtual");
        when(request.getLinkVirtual()).thenReturn("https://meet.test");
        when(request.getOrden()).thenReturn("1");

        when(sesionRepository.save(any()))
            .thenAnswer(inv -> inv.getArgument(0));

        SesionDomainEntity result = useCase.execute(request);

        assertNotNull(result);
        assertEquals("Sesión Test", result.getTitulo());
        verify(sesionRepository).save(any());
    }

    @Test
    void crearSesion_fechaPasada_lanzaExcepcion() {
        CrearSesionRequest request = mock(CrearSesionRequest.class);
        when(request.getFechaSesion()).thenReturn(LocalDateTime.now().minusDays(1));

        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(request));
    }
}
