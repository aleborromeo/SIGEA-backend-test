package com.zentry.sigea.module_sesiones.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_notificaciones.events.domain.SesionCreadaEvent;
import com.zentry.sigea.module_sesiones.core.entities.SesionDomainEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity.Modalidad;
import com.zentry.sigea.module_sesiones.presentacion.models.CrearSesionRequest;
import com.zentry.sigea.module_sesiones.presentacion.models.SesionResponse;
import com.zentry.sigea.module_sesiones.services.SesionService;
import com.zentry.sigea.module_sesiones.services.usecases.sesion.ActualizarSesionUseCase;
import com.zentry.sigea.module_sesiones.services.usecases.sesion.CrearSesionUseCase;
import com.zentry.sigea.module_sesiones.services.usecases.sesion.EliminarSesionUseCase;
import com.zentry.sigea.module_sesiones.services.usecases.sesion.ListarSesionesUseCase;

@ExtendWith(MockitoExtension.class)
class SesionServiceTest {

    @Mock private CrearSesionUseCase crearSesionUseCase;
    @Mock private ListarSesionesUseCase listarSesionesUseCase;
    @Mock private ActualizarSesionUseCase actualizarSesionUseCase;
    @Mock private EliminarSesionUseCase eliminarSesionUseCase;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private IInscripcionRepository inscripcionRepository;

    @InjectMocks
    private SesionService sesionService;

    @Test
    void crearSesion_conInscritos_publicaEvento() {
        // Arrange
        CrearSesionRequest request = mock(CrearSesionRequest.class);
        when(request.getActividadId()).thenReturn("act-1");

        SesionDomainEntity sesion = SesionDomainEntity.reconstruct(
            "ses-1",
            "act-1",
            "Titulo",
            "Desc",
            LocalDateTime.now().plusDays(2),
            LocalTime.of(10, 0),
            LocalTime.of(12, 0),
            "Ponente",
            Modalidad.VIRTUAL,
            "Lugar",
            "https://meet.test",
            "1",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(crearSesionUseCase.execute(request)).thenReturn(sesion);

        InscripcionDomainEntity inscripcion = mock(InscripcionDomainEntity.class);
        when(inscripcion.getUsuarioId()).thenReturn("usr-1");

        when(inscripcionRepository.findByActividadId("act-1"))
            .thenReturn(List.of(inscripcion)); // ✅ LISTA TIPADA CORRECTAMENTE

        // Act
        SesionResponse response = sesionService.crearSesion(request);

        // Assert
        assertNotNull(response);
        verify(eventPublisher, times(1)).publishEvent(any(SesionCreadaEvent.class));
    }

    @Test
    void crearSesion_sinInscritos_noPublicaEvento() {
        CrearSesionRequest request = mock(CrearSesionRequest.class);
        when(request.getActividadId()).thenReturn("act-1");

        SesionDomainEntity sesion = SesionDomainEntity.reconstruct(
            "ses-1","act-1","Titulo","Desc",
            LocalDateTime.now().plusDays(2),
            LocalTime.of(10, 0),
            LocalTime.of(12, 0),
            "Ponente",
            Modalidad.PRESENCIAL,
            "Lugar",
            null,
            "1",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(crearSesionUseCase.execute(request)).thenReturn(sesion);
        when(inscripcionRepository.findByActividadId("act-1")).thenReturn(List.of()); // ✅ lista vacía tipada

        SesionResponse response = sesionService.crearSesion(request);

        assertNotNull(response);
        verify(eventPublisher, never()).publishEvent(any());
    }
}
