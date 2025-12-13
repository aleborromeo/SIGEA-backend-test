package com.zentry.sigea.module_inscripciones.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IEstadoInscripcionRepository;
import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.EstadoInscripcionRequest;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.EstadoInscripcionResponse;
import com.zentry.sigea.module_inscripciones.services.usecases.estado_inscripcion.CrearEstadoInscripcionUseCase;
import com.zentry.sigea.module_inscripciones.services.usecases.estado_inscripcion.EliminarEstadoInscripcionUseCase;

class EstadoInscripcionServiceTest {

    @Test
    void crearEstadoInscripcion_delegaEnUseCaseYRetornaRespuesta() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase crearUseCase = mock(CrearEstadoInscripcionUseCase.class);
        EliminarEstadoInscripcionUseCase eliminarUseCase = mock(EliminarEstadoInscripcionUseCase.class);

        EstadoInscripcionService service = new EstadoInscripcionService(
                repository,
                crearUseCase,
                eliminarUseCase
        );

        EstadoInscripcionRequest request = new EstadoInscripcionRequest("CONFIRMADA", "Confirmada");
        when(crearUseCase.execute(request)).thenReturn("ok");

        String result = service.crearEstadoInscripcion(request);

        assertEquals("ok", result);
        verify(crearUseCase).execute(request);
    }

    @Test
    void listarEstadosInscripcion_mapeaCorrectamenteLasRespuestas() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase crearUseCase = mock(CrearEstadoInscripcionUseCase.class);
        EliminarEstadoInscripcionUseCase eliminarUseCase = mock(EliminarEstadoInscripcionUseCase.class);

        EstadoInscripcionService service = new EstadoInscripcionService(
                repository,
                crearUseCase,
                eliminarUseCase
        );

        EstadoInscripcionDomainEntity e1 = new EstadoInscripcionDomainEntity();
        e1.setId("1");
        e1.setCodigo("PENDIENTE");
        e1.setEtiqueta("Pendiente");

        EstadoInscripcionDomainEntity e2 = new EstadoInscripcionDomainEntity();
        e2.setId("2");
        e2.setCodigo("CONFIRMADA");
        e2.setEtiqueta("Confirmada");

        when(repository.findAll()).thenReturn(List.of(e1, e2));

        List<EstadoInscripcionResponse> result = service.listarEstadosInscripcion();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("PENDIENTE", result.get(0).getCodigo());
        assertEquals("2", result.get(1).getId());
        assertEquals("CONFIRMADA", result.get(1).getCodigo());
    }

    @Test
    void obtenerEstadoInscripcionPorId_devuelveResponseCuandoExiste() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase crearUseCase = mock(CrearEstadoInscripcionUseCase.class);
        EliminarEstadoInscripcionUseCase eliminarUseCase = mock(EliminarEstadoInscripcionUseCase.class);

        EstadoInscripcionService service = new EstadoInscripcionService(
                repository,
                crearUseCase,
                eliminarUseCase
        );

        EstadoInscripcionDomainEntity entity = new EstadoInscripcionDomainEntity();
        entity.setId("abc");
        entity.setCodigo("CANCELADA");
        entity.setEtiqueta("Cancelada");

        when(repository.findById("abc")).thenReturn(Optional.of(entity));

        EstadoInscripcionResponse result = service.obtenerEstadoInscripcionPorId("abc");

        assertEquals("abc", result.getId());
        assertEquals("CANCELADA", result.getCodigo());
        assertEquals("Cancelada", result.getEtiqueta());
    }

    @Test
    void obtenerEstadoInscripcionPorId_lanzaExcepcionSiNoExiste() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase crearUseCase = mock(CrearEstadoInscripcionUseCase.class);
        EliminarEstadoInscripcionUseCase eliminarUseCase = mock(EliminarEstadoInscripcionUseCase.class);

        EstadoInscripcionService service = new EstadoInscripcionService(
                repository,
                crearUseCase,
                eliminarUseCase
        );

        when(repository.findById("no-id")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.obtenerEstadoInscripcionPorId("no-id")
        );

        assertEquals("No se encontró un estado de inscripción con ID: no-id", ex.getMessage());
    }

    @Test
    void obtenerEstadoInscripcionPorCodigo_devuelveResponseCuandoExiste() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase crearUseCase = mock(CrearEstadoInscripcionUseCase.class);
        EliminarEstadoInscripcionUseCase eliminarUseCase = mock(EliminarEstadoInscripcionUseCase.class);

        EstadoInscripcionService service = new EstadoInscripcionService(
                repository,
                crearUseCase,
                eliminarUseCase
        );

        EstadoInscripcionDomainEntity entity = new EstadoInscripcionDomainEntity();
        entity.setId("1");
        entity.setCodigo("PENDIENTE");
        entity.setEtiqueta("Pendiente");

        when(repository.findByCodigo("PENDIENTE")).thenReturn(Optional.of(entity));

        EstadoInscripcionResponse result = service.obtenerEstadoInscripcionPorCodigo("PENDIENTE");

        assertEquals("1", result.getId());
        assertEquals("PENDIENTE", result.getCodigo());
        assertEquals("Pendiente", result.getEtiqueta());
    }

    @Test
    void obtenerEstadoInscripcionPorCodigo_lanzaExcepcionSiNoExiste() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase crearUseCase = mock(CrearEstadoInscripcionUseCase.class);
        EliminarEstadoInscripcionUseCase eliminarUseCase = mock(EliminarEstadoInscripcionUseCase.class);

        EstadoInscripcionService service = new EstadoInscripcionService(
                repository,
                crearUseCase,
                eliminarUseCase
        );

        when(repository.findByCodigo("XYZ")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.obtenerEstadoInscripcionPorCodigo("XYZ")
        );

        assertEquals("No se encontró un estado de inscripción con código: XYZ", ex.getMessage());
    }

    @Test
    void eliminarEstadoInscripcion_delegaEnUseCase() {
        IEstadoInscripcionRepository repository = mock(IEstadoInscripcionRepository.class);
        CrearEstadoInscripcionUseCase crearUseCase = mock(CrearEstadoInscripcionUseCase.class);
        EliminarEstadoInscripcionUseCase eliminarUseCase = mock(EliminarEstadoInscripcionUseCase.class);

        EstadoInscripcionService service = new EstadoInscripcionService(
                repository,
                crearUseCase,
                eliminarUseCase
        );

        service.eliminarEstadoInscripcion("123");

        verify(eliminarUseCase).execute("123");
    }
}
