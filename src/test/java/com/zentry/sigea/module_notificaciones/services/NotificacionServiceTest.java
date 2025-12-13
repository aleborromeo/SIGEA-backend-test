package com.zentry.sigea.module_notificaciones.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_notificaciones.core.entities.CanalNotificacion;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.ports.IEmailService;
import com.zentry.sigea.module_notificaciones.core.ports.IUsuarioGateway;
import com.zentry.sigea.module_notificaciones.core.ports.IWhatsAppService;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;
import com.zentry.sigea.module_notificaciones.presentation.models.responseDTO.NotificacionResponse;
import com.zentry.sigea.module_notificaciones.services.usecases.notificacion.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private CrearNotificacionUseCase crearNotificacionUseCase;
    @Mock
    private ObtenerNotificacionPorIdUseCase obtenerNotificacionPorIdUseCase;
    @Mock
    private ListarNotificacionesUseCase listarNotificacionesUseCase;
    @Mock
    private ActualizarNotificacionUseCase actualizarNotificacionUseCase;
    @Mock
    private ActualizarEstadoNotificacionUseCase actualizarEstadoNotificacionUseCase;
    @Mock
    private EliminarNotificacionUseCase eliminarNotificacionUseCase;

    @Mock
    private IEmailService emailService;
    @Mock
    private IWhatsAppService whatsAppService;
    @Mock
    private IUsuarioGateway usuarioGateway;

    @InjectMocks
    private NotificacionService service;

    private NotificacionDomainEntity notificacion;

    @BeforeEach
    void setup() {
        TipoNotificacionDomainEntity tipo = new TipoNotificacionDomainEntity();
        tipo.setCodigo("INSCRIPCION");

        notificacion = new NotificacionDomainEntity();
        notificacion.setId("notif-1");
        notificacion.setUsuarioId("user-1");
        notificacion.setMensaje("Mensaje test");
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setTipoNotificacion(tipo);
        notificacion.setCanal(CanalNotificacion.SISTEMA);
    }

    // ========================= CREAR =========================

    @Test
    void crearNotificacion_ok_emailEnviado() {
        CrearNotificacionRequest request = new CrearNotificacionRequest(
            "user-1", null, "INSCRIPCION", "Mensaje", null, "SISTEMA"
        );

        when(crearNotificacionUseCase.execute(request)).thenReturn(notificacion);
        when(usuarioGateway.obtenerCorreoUsuario("user-1")).thenReturn(Optional.of("test@mail.com"));
        when(usuarioGateway.obtenerNombreUsuario("user-1")).thenReturn(Optional.of("Juan"));
        when(emailService.enviar(any(), anyString(), anyString())).thenReturn(true);

        String result = service.crearNotificacion(request);

        assertNotNull(result);
        verify(crearNotificacionUseCase).execute(request);
        verify(emailService).enviar(any(), eq("test@mail.com"), eq("Juan"));
    }

    @Test
    void crearNotificacion_emailFalla_noExplota() {
        CrearNotificacionRequest request = new CrearNotificacionRequest(
            "user-1", null, "INSCRIPCION", "Mensaje", null, "SISTEMA"
        );

        when(crearNotificacionUseCase.execute(request)).thenReturn(notificacion);
        when(usuarioGateway.obtenerCorreoUsuario("user-1")).thenReturn(Optional.of("test@mail.com"));
        when(usuarioGateway.obtenerNombreUsuario("user-1")).thenReturn(Optional.of("Juan"));
        when(emailService.enviar(any(), anyString(), anyString())).thenReturn(false);

        String result = service.crearNotificacion(request);

        assertNotNull(result);
        verify(emailService).enviar(any(), any(), any());
    }

    // ========================= OBTENER =========================

    @Test
    void obtenerNotificacionPorId_ok() {
        when(obtenerNotificacionPorIdUseCase.execute("notif-1")).thenReturn(notificacion);

        NotificacionResponse response = service.obtenerNotificacionPorId("notif-1");

        assertNotNull(response);
        assertEquals("notif-1", response.getId());
    }

    // ========================= LISTAR =========================

    @Test
    void listarNotificaciones_ok() {
        when(listarNotificacionesUseCase.execute()).thenReturn(List.of(notificacion));

        List<NotificacionResponse> result = service.listarNotificaciones();

        assertEquals(1, result.size());
    }

    // ========================= MARCAR LEÍDA =========================

    @Test
    void marcarComoLeida_ok() {
        when(actualizarEstadoNotificacionUseCase.marcarComoLeida("notif-1"))
            .thenReturn(notificacion);

        NotificacionResponse response = service.marcarComoLeida("notif-1");

        assertNotNull(response);
        verify(actualizarEstadoNotificacionUseCase).marcarComoLeida("notif-1");
    }

    // ========================= ELIMINAR =========================

    @Test
    void eliminarNotificacion_ok() {
        doNothing().when(eliminarNotificacionUseCase).execute("notif-1");

        service.eliminarNotificacion("notif-1");

        verify(eliminarNotificacionUseCase).execute("notif-1");
    }

    // ========================= USUARIO =========================

    @Test
    void obtenerNombreUsuarioPorId_default() {
        when(usuarioGateway.obtenerNombreUsuario("user-x")).thenReturn(Optional.empty());

        String nombre = service.obtenerNombreUsuarioPorId("user-x");

        assertEquals("Usuario", nombre);
    }
}
