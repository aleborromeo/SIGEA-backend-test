package com.zentry.sigea.module_inscripciones.infrastructure.database.adapters;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.EstadoInscripcionEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.mappers.EstadoInscripcionMapper;
import com.zentry.sigea.module_inscripciones.infrastructure.repository.EstadoInscripcionJPARepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoInscripcionRepositoryAdapterTest {

    @Mock
    private EstadoInscripcionJPARepository jpaRepository;

    @InjectMocks
    private EstadoInscripcionRepositoryAdapter adapter;

    @Test
    void findById_mapeaCorrectamenteCuandoExiste() {
        String id = "550e8400-e29b-41d4-a716-446655440000";
        UUID uuid = UUID.fromString(id);

        EstadoInscripcionEntity entity = new EstadoInscripcionEntity();
        EstadoInscripcionDomainEntity domain = new EstadoInscripcionDomainEntity();

        when(jpaRepository.findById(uuid)).thenReturn(Optional.of(entity));

        try (MockedStatic<EstadoInscripcionMapper> mapperMock =
                     mockStatic(EstadoInscripcionMapper.class)) {

            mapperMock.when(() -> EstadoInscripcionMapper.toDomain(entity))
                      .thenReturn(domain);

            Optional<EstadoInscripcionDomainEntity> result = adapter.findById(id);

            assertTrue(result.isPresent());
            assertSame(domain, result.get());
            verify(jpaRepository).findById(uuid);
        }
    }

    @Test
    void findById_devuelveEmptyCuandoNoExiste() {
        String id = "550e8400-e29b-41d4-a716-446655440000";
        UUID uuid = UUID.fromString(id);

        when(jpaRepository.findById(uuid)).thenReturn(Optional.empty());

        Optional<EstadoInscripcionDomainEntity> result = adapter.findById(id);

        assertTrue(result.isEmpty());
        verify(jpaRepository).findById(uuid);
    }

    @Test
    void findByCodigo_mapeaCorrectamenteCuandoExiste() {
        String codigo = "INS_ACT";

        EstadoInscripcionEntity entity = new EstadoInscripcionEntity();
        EstadoInscripcionDomainEntity domain = new EstadoInscripcionDomainEntity();

        when(jpaRepository.findByCodigo(codigo)).thenReturn(Optional.of(entity));

        try (MockedStatic<EstadoInscripcionMapper> mapperMock =
                     mockStatic(EstadoInscripcionMapper.class)) {

            mapperMock.when(() -> EstadoInscripcionMapper.toDomain(entity))
                      .thenReturn(domain);

            Optional<EstadoInscripcionDomainEntity> result = adapter.findByCodigo(codigo);

            assertTrue(result.isPresent());
            assertSame(domain, result.get());
            verify(jpaRepository).findByCodigo(codigo);
        }
    }

    @Test
    void findByCodigo_devuelveEmptyCuandoNoExiste() {
        String codigo = "NO_EXISTE";

        when(jpaRepository.findByCodigo(codigo)).thenReturn(Optional.empty());

        Optional<EstadoInscripcionDomainEntity> result = adapter.findByCodigo(codigo);

        assertTrue(result.isEmpty());
        verify(jpaRepository).findByCodigo(codigo);
    }

    @Test
    void findAll_mapeaListaCompleta() {
        EstadoInscripcionEntity e1 = new EstadoInscripcionEntity();
        EstadoInscripcionEntity e2 = new EstadoInscripcionEntity();

        EstadoInscripcionDomainEntity d1 = new EstadoInscripcionDomainEntity();
        EstadoInscripcionDomainEntity d2 = new EstadoInscripcionDomainEntity();

        when(jpaRepository.findAll()).thenReturn(List.of(e1, e2));

        try (MockedStatic<EstadoInscripcionMapper> mapperMock =
                     mockStatic(EstadoInscripcionMapper.class)) {

            mapperMock.when(() -> EstadoInscripcionMapper.toDomain(e1)).thenReturn(d1);
            mapperMock.when(() -> EstadoInscripcionMapper.toDomain(e2)).thenReturn(d2);

            List<EstadoInscripcionDomainEntity> result = adapter.findAll();

            assertEquals(2, result.size());
            assertSame(d1, result.get(0));
            assertSame(d2, result.get(1));
            verify(jpaRepository).findAll();
        }
    }

    @Test
    void save_devuelveTrueCuandoNoHayExcepcion() {
        EstadoInscripcionDomainEntity domain = new EstadoInscripcionDomainEntity();
        EstadoInscripcionEntity entity = new EstadoInscripcionEntity();

        try (MockedStatic<EstadoInscripcionMapper> mapperMock =
                     mockStatic(EstadoInscripcionMapper.class)) {

            mapperMock.when(() -> EstadoInscripcionMapper.toEntity(domain))
                      .thenReturn(entity);

            when(jpaRepository.save(entity)).thenReturn(entity);

            boolean result = adapter.save(domain);

            assertTrue(result);
            verify(jpaRepository).save(entity);
        }
    }

    @Test
    void save_devuelveFalseCuandoLanzaExcepcion() {
        EstadoInscripcionDomainEntity domain = new EstadoInscripcionDomainEntity();
        EstadoInscripcionEntity entity = new EstadoInscripcionEntity();

        try (MockedStatic<EstadoInscripcionMapper> mapperMock =
                     mockStatic(EstadoInscripcionMapper.class)) {

            mapperMock.when(() -> EstadoInscripcionMapper.toEntity(domain))
                      .thenReturn(entity);

            when(jpaRepository.save(entity))
                    .thenThrow(new RuntimeException("Fallo DB"));

            boolean result = adapter.save(domain);

            assertFalse(result);
            verify(jpaRepository).save(entity);
        }
    }

    @Test
    void deleteById_convierteStringAUUIDYDelegaaRepositorio() {
        String id = "550e8400-e29b-41d4-a716-446655440000";
        UUID uuid = UUID.fromString(id);

        adapter.deleteById(id);

        verify(jpaRepository).deleteById(uuid);
    }
}
