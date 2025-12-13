package com.zentry.sigea.module_notificaciones.infrastructure.database.adapters;

import com.zentry.sigea.module_notificaciones.core.entities.NotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.repository.NotificacionJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;
import com.zentry.sigea.module_actividad.infrastructure.repository.ActividadJPARepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionRepositoryAdapterTest {

    @Mock
    private NotificacionJPARepository notificacionJPARepository;

    @Mock
    private UsuarioJPARepository usuarioJPARepository;

    @Mock
    private ActividadJPARepository actividadJPARepository;

    @InjectMocks
    private NotificacionRepositoryAdapter adapter;

    private NotificacionDomainEntity domain;
    private UsuarioEntity usuario;

    @BeforeEach
    void setup() {
        usuario = new UsuarioEntity();
        usuario.setId(UUID.randomUUID());

        domain = new NotificacionDomainEntity();
        domain.setUsuarioId(usuario.getId().toString());
        domain.setMensaje("Mensaje test");
    }

    @Test
    void save_usuarioExiste_ok() {
        when(usuarioJPARepository.findById(any()))
            .thenReturn(Optional.of(usuario));

        boolean result = adapter.save(domain);

        assertTrue(result);
        verify(notificacionJPARepository).save(any());
    }

    @Test
    void save_usuarioNoExiste_false() {
        when(usuarioJPARepository.findById(any()))
            .thenReturn(Optional.empty());

        boolean result = adapter.save(domain);

        assertFalse(result);
        verify(notificacionJPARepository, never()).save(any());
    }
}
