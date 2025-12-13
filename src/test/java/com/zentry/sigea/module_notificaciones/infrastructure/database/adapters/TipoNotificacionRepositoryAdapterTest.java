package com.zentry.sigea.module_notificaciones.infrastructure.database.adapters;

import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.TipoNotificacionEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.repository.TipoNotificacionJPARepository;

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
class TipoNotificacionRepositoryAdapterTest {

    @Mock
    private TipoNotificacionJPARepository jpaRepository;

    @InjectMocks
    private TipoNotificacionRepositoryAdapter adapter;

    private TipoNotificacionEntity entity;

    @BeforeEach
    void setup() {
        entity = new TipoNotificacionEntity();
        entity.setId(UUID.randomUUID());
        entity.setCodigo("CERTIFICADO");
        entity.setEtiqueta("Certificado");
    }

    @Test
    void findByCodigo_ok() {
        when(jpaRepository.findByCodigo("CERTIFICADO"))
            .thenReturn(Optional.of(entity));

        Optional<TipoNotificacionDomainEntity> result =
            adapter.findByCodigo("CERTIFICADO");

        assertTrue(result.isPresent());
        assertEquals("CERTIFICADO", result.get().getCodigo());
    }

    @Test
    void existsByCodigo_false() {
        when(jpaRepository.findByCodigo("OTRO"))
            .thenReturn(Optional.empty());

        assertFalse(adapter.existsByCodigo("OTRO"));
    }
}
