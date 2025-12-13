package com.zentry.sigea.module_asistencias.infrastructure.database.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.infrastructure.database.entities.AsistenciaEntity;
import com.zentry.sigea.module_asistencias.infrastructure.database.mappers.AsistenciaMapper;
import com.zentry.sigea.module_asistencias.infrastructure.repositories.AsistenciaJPARepository;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.repository.InscripcionJPARepository;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;
import com.zentry.sigea.module_sesiones.infrastructure.repositories.SesionJPARepository;

class AsistenciaRepositoryAdapterTest {

    private AsistenciaJPARepository asistenciaRepo;
    private SesionJPARepository sesionRepo;
    private InscripcionJPARepository inscripcionRepo;

    private AsistenciaRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        asistenciaRepo = mock(AsistenciaJPARepository.class);
        sesionRepo = mock(SesionJPARepository.class);
        inscripcionRepo = mock(InscripcionJPARepository.class);

        adapter = new AsistenciaRepositoryAdapter(asistenciaRepo, sesionRepo, inscripcionRepo);
    }

    @Test
    void save_lanzaExcepcion_siNoExisteInscripcion() {
        String inscId = UUID.randomUUID().toString();
        String sesId = UUID.randomUUID().toString();

        AsistenciaDomainEntity domain = mock(AsistenciaDomainEntity.class);
        when(domain.getInscripcionId()).thenReturn(inscId);
        when(domain.getSesionId()).thenReturn(sesId);

        when(inscripcionRepo.findById(UUID.fromString(inscId))).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> adapter.save(domain)
        );

        assertTrue(ex.getMessage().contains("No se encontró inscripción"));
        verify(asistenciaRepo, never()).save(any());
        verify(sesionRepo, never()).findById(any());
    }

    @Test
    void save_lanzaExcepcion_siNoExisteSesion() {
        String inscId = UUID.randomUUID().toString();
        String sesId = UUID.randomUUID().toString();

        AsistenciaDomainEntity domain = mock(AsistenciaDomainEntity.class);
        when(domain.getInscripcionId()).thenReturn(inscId);
        when(domain.getSesionId()).thenReturn(sesId);

        when(inscripcionRepo.findById(UUID.fromString(inscId))).thenReturn(Optional.of(new InscripcionEntity()));
        when(sesionRepo.findById(UUID.fromString(sesId))).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> adapter.save(domain)
        );

        assertTrue(ex.getMessage().contains("No se encontró sesión"));
        verify(asistenciaRepo, never()).save(any());
    }

    @Test
    void save_llamaMapperYGuardaEntity() {
        String inscId = UUID.randomUUID().toString();
        String sesId = UUID.randomUUID().toString();

        AsistenciaDomainEntity domain = mock(AsistenciaDomainEntity.class);
        when(domain.getInscripcionId()).thenReturn(inscId);
        when(domain.getSesionId()).thenReturn(sesId);

        InscripcionEntity inscEntity = new InscripcionEntity();
        SesionEntity sesEntity = new SesionEntity();

        when(inscripcionRepo.findById(UUID.fromString(inscId))).thenReturn(Optional.of(inscEntity));
        when(sesionRepo.findById(UUID.fromString(sesId))).thenReturn(Optional.of(sesEntity));

        AsistenciaEntity mappedEntity = new AsistenciaEntity();

        try (MockedStatic<AsistenciaMapper> mocked = mockStatic(AsistenciaMapper.class)) {
            mocked.when(() -> AsistenciaMapper.toEntity(domain, sesEntity, inscEntity))
                  .thenReturn(mappedEntity);

            adapter.save(domain);

            mocked.verify(() -> AsistenciaMapper.toEntity(domain, sesEntity, inscEntity), times(1));
            verify(asistenciaRepo, times(1)).save(mappedEntity);
        }
    }

    @Test
    void saveAll_llamaMapperPorCadaItem_yGuardaLista() {
        AsistenciaDomainEntity d1 = mock(AsistenciaDomainEntity.class);
        AsistenciaDomainEntity d2 = mock(AsistenciaDomainEntity.class);

        String inscId1 = UUID.randomUUID().toString();
        String sesId1 = UUID.randomUUID().toString();
        String inscId2 = UUID.randomUUID().toString();
        String sesId2 = UUID.randomUUID().toString();

        when(d1.getInscripcionId()).thenReturn(inscId1);
        when(d1.getSesionId()).thenReturn(sesId1);
        when(d2.getInscripcionId()).thenReturn(inscId2);
        when(d2.getSesionId()).thenReturn(sesId2);

        InscripcionEntity inscEntity1 = new InscripcionEntity();
        SesionEntity sesEntity1 = new SesionEntity();
        InscripcionEntity inscEntity2 = new InscripcionEntity();
        SesionEntity sesEntity2 = new SesionEntity();

        when(inscripcionRepo.findById(UUID.fromString(inscId1))).thenReturn(Optional.of(inscEntity1));
        when(sesionRepo.findById(UUID.fromString(sesId1))).thenReturn(Optional.of(sesEntity1));
        when(inscripcionRepo.findById(UUID.fromString(inscId2))).thenReturn(Optional.of(inscEntity2));
        when(sesionRepo.findById(UUID.fromString(sesId2))).thenReturn(Optional.of(sesEntity2));

        AsistenciaEntity e1 = new AsistenciaEntity();
        AsistenciaEntity e2 = new AsistenciaEntity();

        try (MockedStatic<AsistenciaMapper> mocked = mockStatic(AsistenciaMapper.class)) {
            mocked.when(() -> AsistenciaMapper.toEntity(d1, sesEntity1, inscEntity1)).thenReturn(e1);
            mocked.when(() -> AsistenciaMapper.toEntity(d2, sesEntity2, inscEntity2)).thenReturn(e2);

            adapter.saveAll(List.of(d1, d2));

            mocked.verify(() -> AsistenciaMapper.toEntity(d1, sesEntity1, inscEntity1), times(1));
            mocked.verify(() -> AsistenciaMapper.toEntity(d2, sesEntity2, inscEntity2), times(1));

            verify(asistenciaRepo, times(1)).saveAll(argThat((List<AsistenciaEntity> list) ->
                list != null
                    && list.size() == 2
                    && list.contains(e1)
                    && list.contains(e2)
            ));

        }
    }

    @Test
    void findByInscripcionId_delegaAlRepo() {
        UUID inscId = UUID.randomUUID();
        when(asistenciaRepo.findByInscripcionId(inscId)).thenReturn(Collections.emptyList());

        var result = adapter.findByInscripcionId(inscId.toString());

        assertNotNull(result);
        verify(asistenciaRepo).findByInscripcionId(inscId);
    }

    @Test
    void findBySesionIdAndPresente_delegaAlRepo() {
        UUID sesId = UUID.randomUUID();
        when(asistenciaRepo.findBySesionIdAndPresente(sesId, true)).thenReturn(Collections.emptyList());

        var result = adapter.findBySesionIdAndPresente(sesId.toString(), true);

        assertNotNull(result);
        verify(asistenciaRepo).findBySesionIdAndPresente(sesId, true);
    }

    @Test
    void existsById_delegaAlRepo() {
        UUID id = UUID.randomUUID();
        when(asistenciaRepo.existsById(id)).thenReturn(true);

        assertTrue(adapter.existsById(id.toString()));
        verify(asistenciaRepo).existsById(id);
    }
}
