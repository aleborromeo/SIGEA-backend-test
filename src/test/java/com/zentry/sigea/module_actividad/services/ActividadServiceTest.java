package com.zentry.sigea.module_actividad.services;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IActividadRespository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.ActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.CrearActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.responseDTO.ActividadResponse;
import com.zentry.sigea.module_actividad.services.usecases.actividad.ActualizarActividadUseCase;
import com.zentry.sigea.module_actividad.services.usecases.actividad.CrearActividadUseCase;
import com.zentry.sigea.module_actividad.services.usecases.actividad.EliminarActividadUseCase;
import com.zentry.sigea.module_notificaciones.events.domain.ActividadCreadaEvent;
import com.zentry.sigea.module_notificaciones.events.domain.ComunicacionPublicadaEvent;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActividadServiceTest {

    @Mock
    private IActividadRespository actividadRespository;

    @Mock
    private CrearActividadUseCase crearActividadUseCase;

    @Mock
    private ActualizarActividadUseCase actualizarActividadUseCase;

    @Mock
    private EliminarActividadUseCase eliminarActividadUseCase;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private UsuarioJPARepository usuarioJPARepository;

    @InjectMocks
    private ActividadService actividadService;

    // ---------- crearActividad ----------

    @Test
    void crearActividad_publicaEventoCuandoHayUsuarios() {
        CrearActividadRequest request = new CrearActividadRequest();
        when(crearActividadUseCase.execute(request)).thenReturn("ACT-1");

        ActividadDomainEntity domain = new ActividadDomainEntity();
        domain.setTitulo("Titulo");
        domain.setDescripcion("Desc");
        when(actividadRespository.findById("ACT-1"))
                .thenReturn(Optional.of(domain));

        UsuarioEntity usuario = mock(UsuarioEntity.class);
        when(usuario.getId()).thenReturn(java.util.UUID.randomUUID());
        when(usuarioJPARepository.findAll()).thenReturn(List.of(usuario));

        String result = actividadService.crearActividad(request);

        assertEquals("ACT-1", result);
        verify(crearActividadUseCase).execute(request);
        verify(actividadRespository).findById("ACT-1");
        verify(usuarioJPARepository).findAll();
        verify(eventPublisher).publishEvent(any(ActividadCreadaEvent.class));
    }

    @Test
    void crearActividad_noPublicaEventoSiNoHayUsuarios() {
        CrearActividadRequest request = new CrearActividadRequest();
        when(crearActividadUseCase.execute(request)).thenReturn("ACT-1");

        ActividadDomainEntity domain = new ActividadDomainEntity();
        when(actividadRespository.findById("ACT-1"))
                .thenReturn(Optional.of(domain));

        when(usuarioJPARepository.findAll()).thenReturn(List.of());

        String result = actividadService.crearActividad(request);

        assertEquals("ACT-1", result);
        verify(eventPublisher, never()).publishEvent(any(ActividadCreadaEvent.class));
    }

    @Test
    void crearActividad_noPublicaEventoSiNoEncuentraActividad() {
        CrearActividadRequest request = new CrearActividadRequest();
        when(crearActividadUseCase.execute(request)).thenReturn("ACT-1");
        when(actividadRespository.findById("ACT-1"))
                .thenReturn(Optional.empty());

        String result = actividadService.crearActividad(request);

        assertEquals("ACT-1", result);
        verify(usuarioJPARepository, never()).findAll();
        verify(eventPublisher, never()).publishEvent(any());
    }

    // ---------- listarActividades ----------

    @Test
    void listarActividades_mapeaConActividadResponse() {
        ActividadDomainEntity a1 = new ActividadDomainEntity();
        ActividadDomainEntity a2 = new ActividadDomainEntity();
        when(actividadRespository.findAll()).thenReturn(List.of(a1, a2));

        try (MockedStatic<ActividadResponse> mock = Mockito.mockStatic(ActividadResponse.class)) {
            mock.when(() -> ActividadResponse.fromEntity(any()))
                    .thenReturn(new ActividadResponse());

            List<ActividadResponse> responses = actividadService.listarActividades();

            assertEquals(2, responses.size());
            mock.verify(() -> ActividadResponse.fromEntity(a1));
            mock.verify(() -> ActividadResponse.fromEntity(a2));
        }
    }

    // ---------- actualizarActividad ----------

    @Test
    void actualizarActividad_devuelveIdCuandoExiste() {
        ActividadRequest request = new ActividadRequest();

        ActividadDomainEntity domain = new ActividadDomainEntity();
        domain.setActividadId("ID-1");

        when(actualizarActividadUseCase.execute("ID-1", request))
                .thenReturn(domain);

        String result = actividadService.actualizarActividad("ID-1", request);

        assertEquals("ID-1", result);
    }

    @Test
    void actualizarActividad_lanzaExcepcionSiNoEncuentraActividad() {
        ActividadRequest request = new ActividadRequest();

        when(actualizarActividadUseCase.execute("ID-1", request))
                .thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> actividadService.actualizarActividad("ID-1", request)
        );
        assertTrue(ex.getMessage().contains("Actividad no encontrada"));
    }

    // ---------- obtenerActividadPorId ----------

    @Test
    void obtenerActividadPorId_ok() {
        ActividadDomainEntity domain = new ActividadDomainEntity();
        when(actividadRespository.findById("ID-1"))
                .thenReturn(Optional.of(domain));

        ActividadResponse mapped = new ActividadResponse();

        try (MockedStatic<ActividadResponse> mock = Mockito.mockStatic(ActividadResponse.class)) {
            mock.when(() -> ActividadResponse.fromEntity(domain))
                    .thenReturn(mapped);

            ActividadResponse result = actividadService.obtenerActividadPorId("ID-1");

            assertEquals(mapped, result);
        }
    }

    @Test
    void obtenerActividadPorId_lanzaExcepcionSiNoExiste() {
        when(actividadRespository.findById("ID-1"))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> actividadService.obtenerActividadPorId("ID-1")
        );
        assertTrue(ex.getMessage().contains("Actividad no encontrada"));
    }

    // ---------- obtenerActividadesPorTipo ----------

    @Test
    void obtenerActividadesPorTipo_mapeaCorrectamente() {
        ActividadDomainEntity a1 = new ActividadDomainEntity();
        when(actividadRespository.findByTipoActividadId("T1"))
                .thenReturn(List.of(a1));

        ActividadResponse mapped = new ActividadResponse();

        try (MockedStatic<ActividadResponse> mock = Mockito.mockStatic(ActividadResponse.class)) {
            mock.when(() -> ActividadResponse.fromEntity(a1))
                    .thenReturn(mapped);

            List<ActividadResponse> result =
                    actividadService.obtenerActividadesPorTipo("T1");

            assertEquals(1, result.size());
            assertEquals(mapped, result.get(0));
        }
    }

    // ---------- eliminarActividad ----------

    @Test
    void eliminarActividad_delegaEnUseCase() {
        actividadService.eliminarActividad("ID-1");

        verify(eliminarActividadUseCase).execute("ID-1");
    }

    // ---------- enviarComunicacion ----------

    @Test
    void enviarComunicacion_publicaEvento() {
        actividadService.enviarComunicacion(
                "ACT-1",
                List.of("U1", "U2"),
                "Titulo",
                "Mensaje",
                ComunicacionPublicadaEvent.TipoComunicacion.ANUNCIO_ACTIVIDAD
        );

        verify(eventPublisher).publishEvent(any(ComunicacionPublicadaEvent.class));
    }
}
