package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;

@ExtendWith(MockitoExtension.class)
class ActualizarEstadoNotificacionUseCaseTest {

    @Mock
    private INotificacionRepository notificacionRepository;
    @Mock
    private IEstadoNotificacionRepository estadoRepository;

    @InjectMocks
    private ActualizarEstadoNotificacionUseCase useCase;

    private NotificacionDomainEntity notificacion;
    private EstadoNotificacionDomainEntity estadoLeida;

    @BeforeEach
    void setup() {
        notificacion = new NotificacionDomainEntity();
        notificacion.setId("notif-1");

        estadoLeida = new EstadoNotificacionDomainEntity();
        estadoLeida.setCodigo("LEIDA");
    }

    @Test
    void execute_ok() {
        when(notificacionRepository.findById("notif-1")).thenReturn(Optional.of(notificacion));
        when(estadoRepository.findByCodigo("LEIDA")).thenReturn(Optional.of(estadoLeida));
        when(notificacionRepository.save(any())).thenReturn(true);

        boolean result = useCase.execute("notif-1", "LEIDA");

        assertTrue(result);
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void execute_notificacionNoExiste() {
        when(notificacionRepository.findById("x")).thenReturn(Optional.empty());

        boolean result = useCase.execute("x", "LEIDA");

        assertFalse(result);
    }

    @Test
    void marcarComoLeida_ok() {
        when(notificacionRepository.findById("notif-1")).thenReturn(Optional.of(notificacion));
        when(estadoRepository.findByCodigo("LEIDA")).thenReturn(Optional.of(estadoLeida));
        when(notificacionRepository.save(any())).thenReturn(true);

        NotificacionDomainEntity result = useCase.marcarComoLeida("notif-1");

        assertNotNull(result);
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void marcarTodasComoLeidas_ok() {
        when(estadoRepository.findByCodigo("LEIDA")).thenReturn(Optional.of(estadoLeida));
        when(notificacionRepository.findByUsuarioId("user-1"))
            .thenReturn(List.of(notificacion));

        useCase.marcarTodasComoLeidas("user-1");

        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void marcarTodasComoLeidas_usuarioInvalido() {
        assertThrows(IllegalArgumentException.class,
            () -> useCase.marcarTodasComoLeidas(""));
    }
}
