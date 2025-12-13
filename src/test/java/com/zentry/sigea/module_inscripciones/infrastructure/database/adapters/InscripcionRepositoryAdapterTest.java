package com.zentry.sigea.module_inscripciones.infrastructure.database.adapters;

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.repository.ActividadJPARepository;
import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.EstadoInscripcionEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.repository.EstadoInscripcionJPARepository;
import com.zentry.sigea.module_inscripciones.infrastructure.repository.InscripcionJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionRepositoryAdapterTest {

    @Mock
    private InscripcionJPARepository inscripcionJPARepository;

    @Mock
    private UsuarioJPARepository usuarioJPARepository;

    @Mock
    private ActividadJPARepository actividadJPARepository;

    @Mock
    private EstadoInscripcionJPARepository estadoInscripcionJPARepository;

    @InjectMocks
    private InscripcionRepositoryAdapter adapter;

    // ---------- save ----------

    @Test
    void save_returnsTrueWhenAllEntitiesExist() {
        // Arrange
        String usuarioId = UUID.randomUUID().toString();
        String actividadId = UUID.randomUUID().toString();
        String estadoId = UUID.randomUUID().toString();

        EstadoInscripcionDomainEntity estadoDom = new EstadoInscripcionDomainEntity();
        estadoDom.setId(estadoId);

        InscripcionDomainEntity domain = new InscripcionDomainEntity();
        domain.setUsuarioId(usuarioId);
        domain.setActividadId(actividadId);
        domain.setEstadoInscripcionDomainEntity(estadoDom);
        domain.setFechaInscripcion(LocalDate.now());

        when(usuarioJPARepository.findById(UUID.fromString(usuarioId)))
                .thenReturn(Optional.of(new UsuarioEntity()));
        when(actividadJPARepository.findById(UUID.fromString(actividadId)))
                .thenReturn(Optional.of(new ActividadEntity()));
        when(estadoInscripcionJPARepository.findById(UUID.fromString(estadoId)))
                .thenReturn(Optional.of(new EstadoInscripcionEntity()));

        // Act
        boolean result = adapter.save(domain);

        // Assert
        assertTrue(result);
        verify(inscripcionJPARepository).save(any(InscripcionEntity.class));
    }

    @Test
    void save_returnsFalseWhenUsuarioNotFound() {
        String usuarioId = UUID.randomUUID().toString();
        String actividadId = UUID.randomUUID().toString();
        String estadoId = UUID.randomUUID().toString();

        EstadoInscripcionDomainEntity estadoDom = new EstadoInscripcionDomainEntity();
        estadoDom.setId(estadoId);

        InscripcionDomainEntity domain = new InscripcionDomainEntity();
        domain.setUsuarioId(usuarioId);
        domain.setActividadId(actividadId);
        domain.setEstadoInscripcionDomainEntity(estadoDom);
        domain.setFechaInscripcion(LocalDate.now());

        when(usuarioJPARepository.findById(UUID.fromString(usuarioId)))
                .thenReturn(Optional.empty());

        boolean result = adapter.save(domain);

        assertFalse(result);
        verify(inscripcionJPARepository, never()).save(any());
    }

    @Test
    void save_returnsFalseWhenActividadNotFound() {
        String usuarioId = UUID.randomUUID().toString();
        String actividadId = UUID.randomUUID().toString();
        String estadoId = UUID.randomUUID().toString();

        EstadoInscripcionDomainEntity estadoDom = new EstadoInscripcionDomainEntity();
        estadoDom.setId(estadoId);

        InscripcionDomainEntity domain = new InscripcionDomainEntity();
        domain.setUsuarioId(usuarioId);
        domain.setActividadId(actividadId);
        domain.setEstadoInscripcionDomainEntity(estadoDom);
        domain.setFechaInscripcion(LocalDate.now());

        when(usuarioJPARepository.findById(UUID.fromString(usuarioId)))
                .thenReturn(Optional.of(new UsuarioEntity()));
        when(actividadJPARepository.findById(UUID.fromString(actividadId)))
                .thenReturn(Optional.empty());

        boolean result = adapter.save(domain);

        assertFalse(result);
        verify(inscripcionJPARepository, never()).save(any());
    }

    @Test
    void save_returnsFalseWhenEstadoNotFound() {
        String usuarioId = UUID.randomUUID().toString();
        String actividadId = UUID.randomUUID().toString();
        String estadoId = UUID.randomUUID().toString();

        EstadoInscripcionDomainEntity estadoDom = new EstadoInscripcionDomainEntity();
        estadoDom.setId(estadoId);

        InscripcionDomainEntity domain = new InscripcionDomainEntity();
        domain.setUsuarioId(usuarioId);
        domain.setActividadId(actividadId);
        domain.setEstadoInscripcionDomainEntity(estadoDom);
        domain.setFechaInscripcion(LocalDate.now());

        when(usuarioJPARepository.findById(UUID.fromString(usuarioId)))
                .thenReturn(Optional.of(new UsuarioEntity()));
        when(actividadJPARepository.findById(UUID.fromString(actividadId)))
                .thenReturn(Optional.of(new ActividadEntity()));
        when(estadoInscripcionJPARepository.findById(UUID.fromString(estadoId)))
                .thenReturn(Optional.empty());

        boolean result = adapter.save(domain);

        assertFalse(result);
        verify(inscripcionJPARepository, never()).save(any());
    }

    // ---------- findById ----------

    @Test
    void findById_callsRepositoryWithUUID() {
        UUID id = UUID.randomUUID();
        InscripcionEntity entity = new InscripcionEntity();
        entity.setId(id);

        // El mapper real se encarga; aquí solo nos interesa que no rompa
        when(inscripcionJPARepository.findById(id))
                .thenReturn(Optional.of(entity));

        var result = adapter.findById(id.toString());

        assertTrue(result.isPresent());
        verify(inscripcionJPARepository).findById(id);
    }

    // ---------- findByUsuarioId / ActividadId / Estado / All ----------

    @Test
    void findByUsuarioId_delegatesToRepository() {
        UUID usuarioId = UUID.randomUUID();
        when(inscripcionJPARepository.findByUsuarioId(usuarioId))
                .thenReturn(List.of(new InscripcionEntity()));

        var result = adapter.findByUsuarioId(usuarioId.toString());

        assertEquals(1, result.size());
        verify(inscripcionJPARepository).findByUsuarioId(usuarioId);
    }

    @Test
    void findByActividadId_delegatesToRepository() {
        UUID actividadId = UUID.randomUUID();
        when(inscripcionJPARepository.findByActividadId(actividadId))
                .thenReturn(List.of(new InscripcionEntity()));

        var result = adapter.findByActividadId(actividadId.toString());

        assertEquals(1, result.size());
        verify(inscripcionJPARepository).findByActividadId(actividadId);
    }

    @Test
    void findAll_delegatesToRepository() {
        when(inscripcionJPARepository.findAll())
                .thenReturn(List.of(new InscripcionEntity(), new InscripcionEntity()));

        var result = adapter.findAll();

        assertEquals(2, result.size());
        verify(inscripcionJPARepository).findAll();
    }

    @Test
    void findByEstadoInscripcionId_delegatesToRepository() {
        UUID estadoId = UUID.randomUUID();
        when(inscripcionJPARepository.findByEstadoInscripcionId(estadoId))
                .thenReturn(List.of(new InscripcionEntity()));

        var result = adapter.findByEstadoInscripcionId(estadoId.toString());

        assertEquals(1, result.size());
        verify(inscripcionJPARepository).findByEstadoInscripcionId(estadoId);
    }

    // ---------- exists / ids / delete ----------

    @Test
    void existsByUsuarioIdAndActividadId_delegatesCorrectly() {
        UUID usuarioId = UUID.randomUUID();
        UUID actividadId = UUID.randomUUID();

        when(inscripcionJPARepository.existsByUsuarioIdAndActividadId(usuarioId, actividadId))
                .thenReturn(true);

        boolean result = adapter.existsByUsuarioIdAndActividadId(
                usuarioId.toString(), actividadId.toString()
        );

        assertTrue(result);
        verify(inscripcionJPARepository)
                .existsByUsuarioIdAndActividadId(usuarioId, actividadId);
    }

    @Test
    void existsById_delegatesCorrectly() {
        UUID id = UUID.randomUUID();
        when(inscripcionJPARepository.existsById(id)).thenReturn(true);

        boolean result = adapter.existsById(id.toString());

        assertTrue(result);
        verify(inscripcionJPARepository).existsById(id);
    }

    @Test
    void findIdByUsuarioIdAndActividadId_returnsStringId() {
        UUID usuarioId = UUID.randomUUID();
        UUID actividadId = UUID.randomUUID();
        UUID uuidInterno = UUID.randomUUID();

        // El repo devuelve Optional<UUID>
        Optional<UUID> returnedOptional = Optional.of(uuidInterno);

        when(inscripcionJPARepository.findIdByUsuarioIdAndActividadId(usuarioId, actividadId))
                .thenReturn(returnedOptional);

        String result = adapter.findIdByUsuarioIdAndActividadId(
                usuarioId.toString(),
                actividadId.toString()
        );

        // El adapter hace .toString() sobre lo que devuelva el repo
        assertEquals(returnedOptional.toString(), result);
        verify(inscripcionJPARepository)
                .findIdByUsuarioIdAndActividadId(usuarioId, actividadId);
    }


    @Test
    void findIdByListActividadIds_convertsStringsToUUIDAndBack() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<String> stringIds = List.of(id1.toString(), id2.toString());

        UUID inscId1 = UUID.randomUUID();
        UUID inscId2 = UUID.randomUUID();

        when(inscripcionJPARepository.findIdByListActividadIds(
                List.of(id1, id2)
        )).thenReturn(List.of(inscId1, inscId2));

        List<String> result = adapter.findIdByListActividadIds(stringIds);

        assertEquals(List.of(inscId1.toString(), inscId2.toString()), result);
        verify(inscripcionJPARepository).findIdByListActividadIds(List.of(id1, id2));
    }

    @Test
    void deleteById_callsRepositoryWithUUID() {
        UUID id = UUID.randomUUID();

        adapter.deleteById(id.toString());

        verify(inscripcionJPARepository).deleteById(id);
    }
}
