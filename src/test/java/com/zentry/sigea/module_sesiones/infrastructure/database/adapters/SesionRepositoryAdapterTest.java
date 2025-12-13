package com.zentry.sigea.module_sesiones.infrastructure.database.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.repository.ActividadJPARepository;
import com.zentry.sigea.module_sesiones.core.entities.SesionDomainEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity.Modalidad;
import com.zentry.sigea.module_sesiones.infrastructure.database.mappers.SesionMapper;
import com.zentry.sigea.module_sesiones.infrastructure.repositories.SesionJPARepository;

@ExtendWith(MockitoExtension.class)
class SesionRepositoryAdapterTest {

    @Mock
    private SesionJPARepository sesionJPARepository;

    @Mock
    private ActividadJPARepository actividadJPARepository;

    @InjectMocks
    private SesionRepositoryAdapter adapter;

    private UUID actividadId;
    private ActividadEntity actividadEntity;
    private SesionDomainEntity sesionDomain;
    private SesionEntity sesionEntity;

    @BeforeEach
    void setup() {
        actividadId = UUID.randomUUID();

        actividadEntity = new ActividadEntity();
        actividadEntity.setId(actividadId);

        sesionDomain = SesionDomainEntity.create(
            actividadId.toString(),
            "Sesión",
            "Descripción",
            LocalDateTime.now().plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Ponente",
            Modalidad.PRESENCIAL,
            "Auditorio",
            null,
            "1"
        );

        sesionEntity = new SesionEntity();
        sesionEntity.setId(UUID.randomUUID());
        sesionEntity.setActividad(actividadEntity);
        sesionEntity.setTitulo("Sesión");
        sesionEntity.setModalidad(Modalidad.PRESENCIAL);
    }

    // =========================
    // SAVE
    // =========================

    @Test
    void save_ok() {
        when(actividadJPARepository.findById(actividadId))
            .thenReturn(Optional.of(actividadEntity));

        when(sesionJPARepository.save(any()))
            .thenReturn(sesionEntity);

        SesionDomainEntity result = adapter.save(sesionDomain);

        assertNotNull(result);
        verify(sesionJPARepository).save(any());
    }

    @Test
    void save_sinActividadId_lanzaError() {
        sesionDomain.setActividadId(null);

        assertThrows(IllegalArgumentException.class,
            () -> adapter.save(sesionDomain));
    }

    @Test
    void save_actividadNoExiste_lanzaError() {
        when(actividadJPARepository.findById(any()))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> adapter.save(sesionDomain));
    }

    // =========================
    // FIND BY ID
    // =========================

    @Test
    void findById_ok() {
        UUID id = UUID.randomUUID();
        sesionEntity.setId(id);

        when(sesionJPARepository.findById(id))
            .thenReturn(Optional.of(sesionEntity));

        Optional<SesionDomainEntity> result = adapter.findById(id.toString());

        assertTrue(result.isPresent());
    }

    @Test
    void findById_null_lanzaError() {
        assertThrows(IllegalArgumentException.class,
            () -> adapter.findById(null));
    }

    // =========================
    // FIND ALL
    // =========================

    @Test
    void findAll_ok() {
        when(sesionJPARepository.findAll())
            .thenReturn(List.of(sesionEntity));

        List<SesionDomainEntity> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    // =========================
    // FIND BY ACTIVIDAD
    // =========================

    @Test
    void findByActividadId_ok() {
        when(sesionJPARepository.findByActividadId(actividadId))
            .thenReturn(List.of(sesionEntity));

        List<SesionDomainEntity> result =
            adapter.findByActividadId(actividadId.toString());

        assertEquals(1, result.size());
    }

    @Test
    void findByActividadId_null_lanzaError() {
        assertThrows(IllegalArgumentException.class,
            () -> adapter.findByActividadId(null));
    }

    // =========================
    // EXISTS
    // =========================

    @Test
    void existsById_ok() {
        UUID id = UUID.randomUUID();

        when(sesionJPARepository.existsById(id))
            .thenReturn(true);

        assertTrue(adapter.existsById(id.toString()));
    }

    @Test
    void existsById_null_lanzaError() {
        assertThrows(IllegalArgumentException.class,
            () -> adapter.existsById(null));
    }

    // =========================
    // DELETE
    // =========================

    @Test
    void deleteById_ok() {
        UUID id = UUID.randomUUID();

        adapter.deleteById(id.toString());

        verify(sesionJPARepository).deleteById(id);
    }

    @Test
    void deleteById_null_lanzaError() {
        assertThrows(IllegalArgumentException.class,
            () -> adapter.deleteById(null));
    }

    // =========================
    // COUNT
    // =========================

    @Test
    void countByActividadId_ok() {
        when(sesionJPARepository.countByActividadId(actividadId))
            .thenReturn(5L);

        long count = adapter.countByActividadId(actividadId.toString());

        assertEquals(5L, count);
    }

    // =========================
    // FILTERS
    // =========================

    @Test
    void findByPonente_ok() {
        when(sesionJPARepository.findByPonente("Ponente"))
            .thenReturn(List.of(sesionEntity));

        List<SesionDomainEntity> result = adapter.findByPonente("Ponente");

        assertEquals(1, result.size());
    }

    @Test
    void findByPonente_vacio_lanzaError() {
        assertThrows(IllegalArgumentException.class,
            () -> adapter.findByPonente(""));
    }

    @Test
    void findByTituloContaining_ok() {
        when(sesionJPARepository.findByTituloContaining("Sesión"))
            .thenReturn(List.of(sesionEntity));

        List<SesionDomainEntity> result =
            adapter.findByTituloContaining("Sesión");

        assertEquals(1, result.size());
    }

    @Test
    void findByModalidad_ok() {
        when(sesionJPARepository.findByModalidad(Modalidad.PRESENCIAL))
            .thenReturn(List.of(sesionEntity));

        List<SesionDomainEntity> result =
            adapter.findByModalidad(Modalidad.PRESENCIAL);

        assertEquals(1, result.size());
    }

    @Test
    void findByModalidad_null_lanzaError() {
        assertThrows(IllegalArgumentException.class,
            () -> adapter.findByModalidad(null));
    }

    @Test
    void findByActividadIdAndModalidad_ok() {
        when(sesionJPARepository.findByActividadIdAndModalidad(
            actividadId, Modalidad.PRESENCIAL))
            .thenReturn(List.of(sesionEntity));

        List<SesionDomainEntity> result =
            adapter.findByActividadIdAndModalidad(
                actividadId, Modalidad.PRESENCIAL);

        assertEquals(1, result.size());
    }
}
