package com.zentry.sigea.module_inscripciones.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.InscripcionRequest;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.InscripcionResponse;
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;
import com.zentry.sigea.module_inscripciones.services.usecases.inscripcion.ActualizarInscripcionUseCase;
import com.zentry.sigea.module_inscripciones.services.usecases.inscripcion.CrearInscripcionUseCase;
import com.zentry.sigea.module_inscripciones.services.usecases.inscripcion.EliminarInscripcionUseCase;
import com.zentry.sigea.module_inscripciones.services.usecases.inscripcion.ObtenerInscripcionPorIdUseCase;
import com.zentry.sigea.module_notificaciones.events.domain.InscripcionCreadaEvent;

class InscripcionServiceTest {

    private InscripcionService buildService(
            IInscripcionRepository repo,
            CrearInscripcionUseCase crear,
            ActualizarInscripcionUseCase actualizar,
            EliminarInscripcionUseCase eliminar,
            ObtenerInscripcionPorIdUseCase obtener,
            ApplicationEventPublisher publisher
    ) {
        return new InscripcionService(
                repo,
                crear,
                actualizar,
                eliminar,
                obtener,
                publisher
        );
    }

    @Test
    void crearInscripcion_ejecutaUseCaseYPublicaEvento() {
        IInscripcionRepository repo = mock(IInscripcionRepository.class);
        CrearInscripcionUseCase crearUseCase = mock(CrearInscripcionUseCase.class);
        ActualizarInscripcionUseCase actualizarUseCase = mock(ActualizarInscripcionUseCase.class);
        EliminarInscripcionUseCase eliminarUseCase = mock(EliminarInscripcionUseCase.class);
        ObtenerInscripcionPorIdUseCase obtenerUseCase = mock(ObtenerInscripcionPorIdUseCase.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        InscripcionService service = buildService(
                repo,
                crearUseCase,
                actualizarUseCase,
                eliminarUseCase,
                obtenerUseCase,
                publisher
        );

        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();
        dto.setUsuarioId("user-1");
        dto.setActividadId("act-1");
        dto.setEstadoId("estado-1");
        dto.setFechaInscripcion(LocalDate.now().plusDays(1));

        when(crearUseCase.execute(dto)).thenReturn("ins-123");

        String result = service.crearInscripcion(dto);

        assertEquals("ins-123", result);
        verify(crearUseCase).execute(dto);

        ArgumentCaptor<InscripcionCreadaEvent> captor =
                ArgumentCaptor.forClass(InscripcionCreadaEvent.class);
        verify(publisher).publishEvent(captor.capture());

        InscripcionCreadaEvent event = captor.getValue();
        assertEquals("user-1", event.getUsuarioId());
        assertEquals("act-1", event.getActividadId());
        assertEquals("ins-123", event.getInscripcionId());
        assertNotNull(event.getFechaInscripcion());

    }

    @Test
    void listarInscripciones_mapeaCorrectamenteLasRespuestas() {
        IInscripcionRepository repo = mock(IInscripcionRepository.class);
        CrearInscripcionUseCase crearUseCase = mock(CrearInscripcionUseCase.class);
        ActualizarInscripcionUseCase actualizarUseCase = mock(ActualizarInscripcionUseCase.class);
        EliminarInscripcionUseCase eliminarUseCase = mock(EliminarInscripcionUseCase.class);
        ObtenerInscripcionPorIdUseCase obtenerUseCase = mock(ObtenerInscripcionPorIdUseCase.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        InscripcionService service = buildService(
                repo,
                crearUseCase,
                actualizarUseCase,
                eliminarUseCase,
                obtenerUseCase,
                publisher
        );

        EstadoInscripcionDomainEntity estado = new EstadoInscripcionDomainEntity();
        estado.setCodigo("CONFIRMADA");
        estado.setEtiqueta("Confirmada");

        InscripcionDomainEntity ins = new InscripcionDomainEntity();
        ins.setId("ins-1");
        ins.setFechaInscripcion(LocalDate.of(2025, 1, 10));
        ins.setUsuarioId("user-1");
        ins.setActividadId("act-1");
        ins.setEstadoInscripcionDomainEntity(estado);
        ins.setCreatedAt(LocalDateTime.now().minusDays(1));
        ins.setUpdatedAt(LocalDateTime.now());

        when(repo.findAll()).thenReturn(List.of(ins));

        List<InscripcionResponse> result = service.listarInscripciones();

        assertEquals(1, result.size());
        InscripcionResponse r = result.get(0);
        assertEquals("ins-1", r.getId());
        assertEquals("user-1", r.getUsuarioId());
        assertEquals("act-1", r.getActividadId());
        assertNotNull(r.getEstado());
        assertEquals("CONFIRMADA", r.getEstado().getCodigo());
        assertTrue(r.isConfirmada());
    }

    @Test
    void actualizarInscripcion_delegaAlUseCaseYRetornaRespuesta() {
        IInscripcionRepository repo = mock(IInscripcionRepository.class);
        CrearInscripcionUseCase crearUseCase = mock(CrearInscripcionUseCase.class);
        ActualizarInscripcionUseCase actualizarUseCase = mock(ActualizarInscripcionUseCase.class);
        EliminarInscripcionUseCase eliminarUseCase = mock(EliminarInscripcionUseCase.class);
        ObtenerInscripcionPorIdUseCase obtenerUseCase = mock(ObtenerInscripcionPorIdUseCase.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        InscripcionService service = buildService(
                repo,
                crearUseCase,
                actualizarUseCase,
                eliminarUseCase,
                obtenerUseCase,
                publisher
        );

        InscripcionRequest request = new InscripcionRequest(
                LocalDate.now(),
                "estado-1",
                "user-1",
                "act-1"
        );

        InscripcionResponse expected = new InscripcionResponse();
        expected.setId("ins-1");

        when(actualizarUseCase.execute(request, "ins-1"))
                .thenReturn(expected);

        InscripcionResponse result = service.actualizarInscripcion("ins-1", request);

        assertEquals("ins-1", result.getId());
        verify(actualizarUseCase).execute(request, "ins-1");
    }

    @Test
    void obtenerInscripcionPorId_devuelveRespuestaCuandoExiste() {
        IInscripcionRepository repo = mock(IInscripcionRepository.class);
        CrearInscripcionUseCase crearUseCase = mock(CrearInscripcionUseCase.class);
        ActualizarInscripcionUseCase actualizarUseCase = mock(ActualizarInscripcionUseCase.class);
        EliminarInscripcionUseCase eliminarUseCase = mock(EliminarInscripcionUseCase.class);
        ObtenerInscripcionPorIdUseCase obtenerUseCase = mock(ObtenerInscripcionPorIdUseCase.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        InscripcionService service = buildService(
                repo,
                crearUseCase,
                actualizarUseCase,
                eliminarUseCase,
                obtenerUseCase,
                publisher
        );

        InscripcionDomainEntity ins = new InscripcionDomainEntity();
        ins.setId("ins-1");
        ins.setFechaInscripcion(LocalDate.now());
        ins.setUsuarioId("user-1");
        ins.setActividadId("act-1");

        when(obtenerUseCase.execute("ins-1"))
                .thenReturn(Optional.of(ins));

        InscripcionResponse result = service.obtenerInscripcionPorId("ins-1");

        assertEquals("ins-1", result.getId());
        assertEquals("user-1", result.getUsuarioId());
        assertEquals("act-1", result.getActividadId());
    }

    @Test
    void obtenerInscripcionPorId_lanzaExcepcionSiNoExiste() {
        IInscripcionRepository repo = mock(IInscripcionRepository.class);
        CrearInscripcionUseCase crearUseCase = mock(CrearInscripcionUseCase.class);
        ActualizarInscripcionUseCase actualizarUseCase = mock(ActualizarInscripcionUseCase.class);
        EliminarInscripcionUseCase eliminarUseCase = mock(EliminarInscripcionUseCase.class);
        ObtenerInscripcionPorIdUseCase obtenerUseCase = mock(ObtenerInscripcionPorIdUseCase.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        InscripcionService service = buildService(
                repo,
                crearUseCase,
                actualizarUseCase,
                eliminarUseCase,
                obtenerUseCase,
                publisher
        );

        when(obtenerUseCase.execute("no-id"))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.obtenerInscripcionPorId("no-id")
        );

        assertEquals("Inscripción no encontrada con ID: no-id", ex.getMessage());
    }

    @Test
    void obtenerInscripcionesPorUsuario_mapeaCorrectamente() {
        IInscripcionRepository repo = mock(IInscripcionRepository.class);
        CrearInscripcionUseCase crearUseCase = mock(CrearInscripcionUseCase.class);
        ActualizarInscripcionUseCase actualizarUseCase = mock(ActualizarInscripcionUseCase.class);
        EliminarInscripcionUseCase eliminarUseCase = mock(EliminarInscripcionUseCase.class);
        ObtenerInscripcionPorIdUseCase obtenerUseCase = mock(ObtenerInscripcionPorIdUseCase.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        InscripcionService service = buildService(
                repo,
                crearUseCase,
                actualizarUseCase,
                eliminarUseCase,
                obtenerUseCase,
                publisher
        );

        InscripcionDomainEntity ins = new InscripcionDomainEntity();
        ins.setId("ins-1");
        ins.setUsuarioId("user-1");
        ins.setActividadId("act-1");
        ins.setFechaInscripcion(LocalDate.now());

        when(repo.findByUsuarioId("user-1"))
                .thenReturn(List.of(ins));

        List<InscripcionResponse> result = service.obtenerInscripcionesPorUsuario("user-1");

        assertEquals(1, result.size());
        assertEquals("ins-1", result.get(0).getId());
        assertEquals("user-1", result.get(0).getUsuarioId());
    }

    @Test
    void obtenerInscripcionesPorActividad_mapeaCorrectamente() {
        IInscripcionRepository repo = mock(IInscripcionRepository.class);
        CrearInscripcionUseCase crearUseCase = mock(CrearInscripcionUseCase.class);
        ActualizarInscripcionUseCase actualizarUseCase = mock(ActualizarInscripcionUseCase.class);
        EliminarInscripcionUseCase eliminarUseCase = mock(EliminarInscripcionUseCase.class);
        ObtenerInscripcionPorIdUseCase obtenerUseCase = mock(ObtenerInscripcionPorIdUseCase.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        InscripcionService service = buildService(
                repo,
                crearUseCase,
                actualizarUseCase,
                eliminarUseCase,
                obtenerUseCase,
                publisher
        );

        InscripcionDomainEntity ins = new InscripcionDomainEntity();
        ins.setId("ins-1");
        ins.setUsuarioId("user-1");
        ins.setActividadId("act-1");
        ins.setFechaInscripcion(LocalDate.now());

        when(repo.findByActividadId("act-1"))
                .thenReturn(List.of(ins));

        List<InscripcionResponse> result = service.obtenerInscripcionesPorActividad("act-1");

        assertEquals(1, result.size());
        assertEquals("ins-1", result.get(0).getId());
        assertEquals("act-1", result.get(0).getActividadId());
    }

    @Test
    void eliminarInscripcion_delegaEnUseCase() {
        IInscripcionRepository repo = mock(IInscripcionRepository.class);
        CrearInscripcionUseCase crearUseCase = mock(CrearInscripcionUseCase.class);
        ActualizarInscripcionUseCase actualizarUseCase = mock(ActualizarInscripcionUseCase.class);
        EliminarInscripcionUseCase eliminarUseCase = mock(EliminarInscripcionUseCase.class);
        ObtenerInscripcionPorIdUseCase obtenerUseCase = mock(ObtenerInscripcionPorIdUseCase.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        InscripcionService service = buildService(
                repo,
                crearUseCase,
                actualizarUseCase,
                eliminarUseCase,
                obtenerUseCase,
                publisher
        );

        service.eliminarInscripcion("ins-1");

        verify(eliminarUseCase).execute("ins-1");
    }
}
