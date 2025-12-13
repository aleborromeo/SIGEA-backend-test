package com.zentry.sigea.module_actividad.infrastructure.database.adapters;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.EstadoActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.repository.EstadoActividadJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para EstadoActividadRepositoryAdapter (Capa de Infraestructura - Adaptador)
 */
class EstadoActividadRepositoryAdapterTest {

    @Mock
    private EstadoActividadJPARepository jpaRepository;

    @InjectMocks
    private EstadoActividadRepositoryAdapter adapter;

    private final UUID TEST_UUID = UUID.randomUUID();
    private final String CODIGO = "ACTIVO";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_debeConvertirEntidadJPAADominio() {
        // ARRANGE: Simular que JPA encuentra una entidad
        EstadoActividadEntity mockEntity = new EstadoActividadEntity();
        mockEntity.setId(TEST_UUID);
        mockEntity.setCodigo(CODIGO);
        when(jpaRepository.findById(TEST_UUID)).thenReturn(Optional.of(mockEntity));

        // ACT
        Optional<EstadoActividadDomainEntity> result = adapter.findById(TEST_UUID.toString());

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(CODIGO, result.get().getCodigo());
        // Verificar que se llamó al repositorio JPA con el UUID correcto
        verify(jpaRepository, times(1)).findById(TEST_UUID);
    }

    @Test
    void testSave_debeConvertirDominioAEntidadJPAyGuardar() {
        // ARRANGE: Entidad de dominio
        EstadoActividadDomainEntity domain = EstadoActividadDomainEntity.create(CODIGO, "Etiqueta");
        
        // Capturador para verificar qué entidad se pasa al JPA
        ArgumentCaptor<EstadoActividadEntity> entityCaptor = ArgumentCaptor.forClass(EstadoActividadEntity.class);
        when(jpaRepository.save(any(EstadoActividadEntity.class))).thenReturn(new EstadoActividadEntity());

        // ACT
        boolean success = adapter.save(domain);

        // ASSERT
        assertTrue(success);
        // Verificar que se llamó a save del repositorio JPA
        verify(jpaRepository, times(1)).save(entityCaptor.capture());
        // Verificar que el objeto pasado al JPA contiene los datos del dominio
        assertEquals(CODIGO, entityCaptor.getValue().getCodigo());
        assertNotNull(entityCaptor.getValue().getEtiqueta());
    }

    @Test
    void testDeleteById_debeLlamarAlJPARepository() {
        // ACT
        adapter.deleteById(TEST_UUID.toString());

        // ASSERT
        // Verificar que el repositorio JPA fue llamado con el UUID
        verify(jpaRepository, times(1)).deleteById(TEST_UUID);
    }
}