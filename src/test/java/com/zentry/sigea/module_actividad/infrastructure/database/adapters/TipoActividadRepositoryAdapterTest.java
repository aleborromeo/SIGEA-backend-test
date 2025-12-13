package com.zentry.sigea.module_actividad.infrastructure.database.adapters;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.TipoActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.repository.TipoActividadJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoActividadRepositoryAdapterTest {

    @Mock
    private TipoActividadJPARepository jpaRepository;

    private TipoActividadRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TipoActividadRepositoryAdapter(jpaRepository);
    }

    @Test
    void findAll_debeRetornarListaDeTipos() {
        // ARRANGE
        TipoActividadEntity entity1 = new TipoActividadEntity();
        entity1.setId(UUID.randomUUID());
        entity1.setNombreActividad("Taller");
        
        TipoActividadEntity entity2 = new TipoActividadEntity();
        entity2.setId(UUID.randomUUID());
        entity2.setNombreActividad("Conferencia");
        
        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        // ACT
        List<TipoActividadDomainEntity> result = adapter.findAll();

        // ASSERT
        assertEquals(2, result.size());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void save_debeRetornarTrue_cuandoGuardaCorrectamente() {
        // ARRANGE
        TipoActividadDomainEntity domain = TipoActividadDomainEntity.create("Taller", "Desc");
        when(jpaRepository.save(any(TipoActividadEntity.class))).thenReturn(new TipoActividadEntity());

        // ACT
        boolean result = adapter.save(domain);

        // ASSERT
        assertTrue(result);
        verify(jpaRepository, times(1)).save(any(TipoActividadEntity.class));
    }

    @Test
    void save_debeRetornarFalse_cuandoOcurreError() {
        // ARRANGE
        TipoActividadDomainEntity domain = TipoActividadDomainEntity.create("Taller", "Desc");
        when(jpaRepository.save(any(TipoActividadEntity.class))).thenThrow(new RuntimeException());

        // ACT
        boolean result = adapter.save(domain);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void findById_debeRetornarTipo_cuandoExiste() {
        // ARRANGE
        UUID uuid = UUID.randomUUID();
        TipoActividadEntity entity = new TipoActividadEntity();
        entity.setId(uuid);
        entity.setNombreActividad("Taller");
        
        when(jpaRepository.findById(uuid)).thenReturn(Optional.of(entity));

        // ACT
        Optional<TipoActividadDomainEntity> result = adapter.findById(uuid.toString());

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals("Taller", result.get().getNombreActividad());
    }

    @Test
    void deleteById_debeEliminarTipo() {
        // ARRANGE
        UUID uuid = UUID.randomUUID();
        doNothing().when(jpaRepository).deleteById(uuid);

        // ACT
        adapter.deleteById(uuid.toString());

        // ASSERT
        verify(jpaRepository, times(1)).deleteById(uuid);
    }
}