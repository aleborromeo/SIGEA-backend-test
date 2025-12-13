package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.TokenUsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.TokenUsuarioJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

@ExtendWith(MockitoExtension.class)
class TokenUsuarioRepositoryAdapterTest {

    @Mock
    private TokenUsuarioJPARepository tokenRepo;

    @Mock
    private UsuarioJPARepository usuarioRepo;

    @InjectMocks
    private TokenUsuarioRepositoryAdapter adapter;

    @Test
    void save_retornaDomain() {
        UUID userId = UUID.randomUUID();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(userId);

        TokenUsuarioEntity entity = new TokenUsuarioEntity();
        entity.setId(UUID.randomUUID());

        when(usuarioRepo.findById(userId)).thenReturn(Optional.of(usuario));
        when(tokenRepo.saveAndFlush(any())).thenReturn(entity);

        TokenUsuarioDomainEntity domain = new TokenUsuarioDomainEntity();
        domain.setUsuarioId(userId.toString());

        TokenUsuarioDomainEntity saved = adapter.save(domain);

        assertNotNull(saved);
    }

    @Test
    void deleteExpiredTokens_ejecuta() {
        adapter.deleteExpiredTokens(Instant.now());
        verify(tokenRepo).deleteExpiredTokens(any());
    }

    @Test
    void findAll_retornaLista() {
        when(tokenRepo.findAll()).thenReturn(List.of(new TokenUsuarioEntity()));

        assertEquals(1, adapter.findAll().size());
    }
}
