package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_usuarios.infrastructure.database.entities.RolEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.RolJPARepository;

@ExtendWith(MockitoExtension.class)
class RolRepositoryAdapterTest {

    @Mock
    private RolJPARepository rolJPARepository;

    @InjectMocks
    private RolRepositoryAdapter adapter;

    @Test
    void findAll_retornaLista() {
        when(rolJPARepository.findAll()).thenReturn(List.of(new RolEntity()));

        assertEquals(1, adapter.findAll().size());
    }

    @Test
    void findByNombreRol_retornaOptional() {
        when(rolJPARepository.findByNombreRol("ADMIN"))
            .thenReturn(Optional.of(new RolEntity()));

        assertTrue(adapter.findByNombreRol("ADMIN").isPresent());
    }

    @Test
    void findIdByNombreRol_retornaId() {
        UUID id = UUID.randomUUID();
        when(rolJPARepository.findIdByNombreRol("ADMIN"))
            .thenReturn(Optional.of(id));

        assertEquals(id.toString(), adapter.findIdByNombreRol("ADMIN"));
    }
}
