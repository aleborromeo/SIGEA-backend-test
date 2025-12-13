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
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;

class CrearInscripcionUseCaseTest {

    @Test
    void execute_lanzaExcepcionSiInscripcionYaExiste() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        CrearInscripcionUseCase useCase =
                new CrearInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();
        dto.setUsuarioId("user-1");
        dto.setActividadId("act-1");
        dto.setEstadoId("estado-1");
        dto.setFechaInscripcion(LocalDate.now().plusDays(1));

        when(inscripcionRepository.existsByUsuarioIdAndActividadId("user-1", "act-1"))
                .thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(dto));

        assertEquals("El usuario ya está inscrito en esta actividad", ex.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void execute_lanzaExcepcionSiEstadoIdEsNull() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        CrearInscripcionUseCase useCase =
                new CrearInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();
        dto.setUsuarioId("user-1");
        dto.setActividadId("act-1");
        dto.setEstadoId(null);
        dto.setFechaInscripcion(LocalDate.now().plusDays(1));

        when(inscripcionRepository.existsByUsuarioIdAndActividadId("user-1", "act-1"))
                .thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(dto));

        assertEquals("El ID del estado de inscripción es obligatorio", ex.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void execute_lanzaExcepcionSiEstadoNoExiste() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        CrearInscripcionUseCase useCase =
                new CrearInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();
        dto.setUsuarioId("user-1");
        dto.setActividadId("act-1");
        dto.setEstadoId("estado-x");
        dto.setFechaInscripcion(LocalDate.now().plusDays(1));

        when(inscripcionRepository.existsByUsuarioIdAndActividadId("user-1", "act-1"))
                .thenReturn(false);

        when(estadoInscripcionRepository.findById("estado-x"))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(dto));

        assertEquals("No se encontró un estado de inscripción con ID: estado-x", ex.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void execute_lanzaExcepcionSiFechaEsPasado() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        CrearInscripcionUseCase useCase =
                new CrearInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();
        dto.setUsuarioId("user-1");
        dto.setActividadId("act-1");
        dto.setEstadoId("estado-1");
        dto.setFechaInscripcion(LocalDate.now().minusDays(1));

        when(inscripcionRepository.existsByUsuarioIdAndActividadId("user-1", "act-1"))
                .thenReturn(false);

        EstadoInscripcionDomainEntity estado = new EstadoInscripcionDomainEntity();
        estado.setCodigo("PENDIENTE");

        when(estadoInscripcionRepository.findById("estado-1"))
                .thenReturn(Optional.of(estado));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(dto));

        assertEquals("La fecha de inscripción no puede ser en el pasado", ex.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void execute_guardaYDevuelveMensajeExito() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        CrearInscripcionUseCase useCase =
                new CrearInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();
        dto.setUsuarioId("user-1");
        dto.setActividadId("act-1");
        dto.setEstadoId("estado-1");
        dto.setFechaInscripcion(LocalDate.now().plusDays(1));

        when(inscripcionRepository.existsByUsuarioIdAndActividadId("user-1", "act-1"))
                .thenReturn(false);

        EstadoInscripcionDomainEntity estado = new EstadoInscripcionDomainEntity();
        estado.setCodigo("PENDIENTE");
        when(estadoInscripcionRepository.findById("estado-1"))
                .thenReturn(Optional.of(estado));

        when(inscripcionRepository.save(any(InscripcionDomainEntity.class)))
                .thenReturn(true);

        String result = useCase.execute(dto);

        assertEquals("Inscripción registrada con éxito", result);
        verify(inscripcionRepository).save(any(InscripcionDomainEntity.class));
    }

    @Test
    void execute_devuelveErrorCuandoSaveRetornaFalse() {
        IInscripcionRepository inscripcionRepository = mock(IInscripcionRepository.class);
        IEstadoInscripcionRepository estadoInscripcionRepository = mock(IEstadoInscripcionRepository.class);
        CrearInscripcionUseCase useCase =
                new CrearInscripcionUseCase(inscripcionRepository, estadoInscripcionRepository);

        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();
        dto.setUsuarioId("user-1");
        dto.setActividadId("act-1");
        dto.setEstadoId("estado-1");
        dto.setFechaInscripcion(LocalDate.now().plusDays(1));

        when(inscripcionRepository.existsByUsuarioIdAndActividadId("user-1", "act-1"))
                .thenReturn(false);

        EstadoInscripcionDomainEntity estado = new EstadoInscripcionDomainEntity();
        estado.setCodigo("PENDIENTE");
        when(estadoInscripcionRepository.findById("estado-1"))
                .thenReturn(Optional.of(estado));

        when(inscripcionRepository.save(any(InscripcionDomainEntity.class)))
                .thenReturn(false);

        String result = useCase.execute(dto);

        assertEquals("Algo salió mal al guardar la inscripción", result);
    }
}
