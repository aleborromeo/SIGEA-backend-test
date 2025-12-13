package com.zentry.sigea.module_inscripciones.services.usecases.inscripcion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_inscripciones.core.repositories.IEstadoInscripcionRepository;
import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.InscripcionRequest;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.InscripcionResponse;

class ActualizarInscripcionUseCaseTest {

    @Test
    void execute_updatesFechaYEstadoYGuarda() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        ActualizarInscripcionUseCase useCase =
                new ActualizarInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        String inscripcionId = "ins-123";

        // Inscripción actual en base de datos
        InscripcionDomainEntity existente = new InscripcionDomainEntity();
        existente.setId(inscripcionId);
        existente.setFechaInscripcion(LocalDate.of(2025, 1, 1));

        EstadoInscripcionDomainEntity estadoActual = new EstadoInscripcionDomainEntity();
        estadoActual.setCodigo("PENDIENTE");
        existente.setEstadoInscripcionDomainEntity(estadoActual);

        when(inscripcionRepository.findById(inscripcionId))
                .thenReturn(Optional.of(existente));

        // Nuevo estado
        EstadoInscripcionDomainEntity nuevoEstado = new EstadoInscripcionDomainEntity();
        nuevoEstado.setCodigo("CONFIRMADA");
        nuevoEstado.setEtiqueta("Confirmada");

        when(estadoInscripcionRepository.findById("estado-1"))
                .thenReturn(Optional.of(nuevoEstado));

        InscripcionRequest request = new InscripcionRequest(
                LocalDate.of(2025, 2, 10),
                "estado-1",
                "user-1",
                "act-1"
        );

        InscripcionResponse response = useCase.execute(request, inscripcionId);

        // Verificamos cambios en el dominio
        assertEquals(LocalDate.of(2025, 2, 10), existente.getFechaInscripcion());
        assertEquals("CONFIRMADA", existente.getEstadoInscripcionDomainEntity().getCodigo());

        // Verificamos respuesta
        assertEquals(inscripcionId, response.getId());
        assertEquals(LocalDate.of(2025, 2, 10), response.getFechaInscripcion());
        assertNotNull(response.getEstado());
        assertEquals("CONFIRMADA", response.getEstado().getCodigo());

        verify(inscripcionRepository).save(existente);
    }

    @Test
    void execute_soloActualizaFechaCuandoEstadoEsNullOVacio() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        ActualizarInscripcionUseCase useCase =
                new ActualizarInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        String inscripcionId = "ins-123";

        InscripcionDomainEntity existente = new InscripcionDomainEntity();
        existente.setId(inscripcionId);
        existente.setFechaInscripcion(LocalDate.of(2025, 1, 1));

        EstadoInscripcionDomainEntity estadoActual = new EstadoInscripcionDomainEntity();
        estadoActual.setCodigo("PENDIENTE");
        existente.setEstadoInscripcionDomainEntity(estadoActual);

        when(inscripcionRepository.findById(inscripcionId))
                .thenReturn(Optional.of(existente));

        // EstadoId vacío => no debería buscar ni cambiar estado
        InscripcionRequest request = new InscripcionRequest(
                LocalDate.of(2025, 3, 5),
                "   ",
                "user-1",
                "act-1"
        );

        InscripcionResponse response = useCase.execute(request, inscripcionId);

        assertEquals(LocalDate.of(2025, 3, 5), existente.getFechaInscripcion());
        assertEquals("PENDIENTE", existente.getEstadoInscripcionDomainEntity().getCodigo());

        verify(estadoInscripcionRepository, never()).findById(anyString());
        verify(inscripcionRepository).save(existente);
        assertEquals("PENDIENTE", response.getEstado().getCodigo());
    }

    @Test
    void execute_lanzaExcepcionCuandoInscripcionNoExiste() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        ActualizarInscripcionUseCase useCase =
                new ActualizarInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        when(inscripcionRepository.findById("no-existe"))
                .thenReturn(Optional.empty());

        InscripcionRequest request = new InscripcionRequest(
                LocalDate.now(),
                "estado-1",
                "user",
                "act"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(request, "no-existe"));

        assertEquals("No se encontró una inscripción con ID: no-existe", ex.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void execute_lanzaExcepcionCuandoNuevoEstadoNoExiste() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        ActualizarInscripcionUseCase useCase =
                new ActualizarInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        String inscripcionId = "ins-123";

        InscripcionDomainEntity existente = new InscripcionDomainEntity();
        existente.setId(inscripcionId);
        existente.setFechaInscripcion(LocalDate.now());
        existente.setEstadoInscripcionDomainEntity(new EstadoInscripcionDomainEntity());

        when(inscripcionRepository.findById(inscripcionId))
                .thenReturn(Optional.of(existente));

        when(estadoInscripcionRepository.findById("estado-x"))
                .thenReturn(Optional.empty());

        InscripcionRequest request = new InscripcionRequest(
                LocalDate.now(),
                "estado-x",
                "user",
                "act"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(request, inscripcionId));

        assertEquals("No se encontró un estado de inscripción con ID: estado-x", ex.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }
}
