package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @Mock
    private UsuarioJPARepository usuarioJPARepository;

    @InjectMocks
    private UsuarioRepositoryAdapter adapter;

    @Test
    void findById_devuelveOptionalVacio() {
        UUID id = UUID.randomUUID();
        when(usuarioJPARepository.findById(id)).thenReturn(Optional.empty());

        Optional<?> result = adapter.findById(id.toString());

        assertTrue(result.isEmpty());
    }
}
