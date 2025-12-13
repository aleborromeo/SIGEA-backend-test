package com.zentry.sigea.module_notificaciones.infrastructure.database.adapters;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.EstadoNotificacionEntity;
import com.zentry.sigea.module_notificaciones.infrastructure.repository.EstadoNotificacionJPARepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoNotificacionRepositoryAdapterTest {

    @Mock
    private EstadoNotificacionJPARepository jpaRepository;

    @InjectMocks
    private EstadoNotificacionRepositoryAdapter adapter;

    private EstadoNotificacionEntity entity;

    @BeforeEach
    void setup() {
        entity = new EstadoNotificacionEntity();
        entity.setId(UUID.randomUUID());
        entity.setCodigo("PENDIENTE");
        entity.setEtiqueta("Pendiente");
    }

    @Test
    void findByCodigo_debeRetornarDomain() {
        when(jpaRepository.findByCodigo("PENDIENTE"))
            .thenReturn(Optional.of(entity));

        Optional<EstadoNotificacionDomainEntity> result =
            adapter.findByCodigo("PENDIENTE");

        assertTrue(result.isPresent());
        assertEquals("PENDIENTE", result.get().getCodigo());
    }

    @Test
    void findAll_debeMapearLista() {
        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<EstadoNotificacionDomainEntity> result = adapter.findAll();

        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).getCodigo());
    }

    @Test
    void existsByCodigo_true() {
        when(jpaRepository.findByCodigo("PENDIENTE"))
            .thenReturn(Optional.of(entity));

        assertTrue(adapter.existsByCodigo("PENDIENTE"));
    }
}
