package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioRolEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.RolJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioRolJPARepository;

@ExtendWith(MockitoExtension.class)
class UsuarioRolRepositoryAdapterTest {

    @Mock
    private UsuarioRolJPARepository usuarioRolRepo;

    @Mock
    private UsuarioJPARepository usuarioRepo;

    @Mock
    private RolJPARepository rolRepo;

    @InjectMocks
    private UsuarioRolRepositoryAdapter adapter;

    @Test
    void findRolesByUsuarioId_retornaLista() {
        when(usuarioRolRepo.findById_IdUsuario(any()))
            .thenReturn(List.of(new UsuarioRolEntity()));

        assertEquals(1, adapter.findRolesByUsuarioId(UUID.randomUUID().toString()).size());
    }
}
