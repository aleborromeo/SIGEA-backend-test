package com.zentry.sigea.module_actividad.services;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IEstadoActividadRepository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.EstadoActividadRequest;
import com.zentry.sigea.module_actividad.services.usecases.estado_actividad.ActualizarEstadoActividadUseCase;
import com.zentry.sigea.module_actividad.services.usecases.estado_actividad.CrearEstadoActividadUseCase;
import com.zentry.sigea.module_actividad.services.usecases.estado_actividad.EliminarEstadoActividadUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoActividadServiceTest {

    @Mock
    private IEstadoActividadRepository estadoActividadRepository;

    @Mock
    private CrearEstadoActividadUseCase crearEstadoActividadUseCase;

    @Mock
    private EliminarEstadoActividadUseCase eliminarEstadoActividadUseCase;

    @Mock
    private ActualizarEstadoActividadUseCase actualizarEstadoActividadUseCase;

    @InjectMocks
    private EstadoActividadService service;

    @Test
    void crearEstadoActividad_delegaEnUseCase() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");
        when(crearEstadoActividadUseCase.execute(request)).thenReturn("OK");

        String result = service.crearEstadoActividad(request);

        assertEquals("OK", result);
        verify(crearEstadoActividadUseCase).execute(request);
    }

    @Test
    void actualizarEstadoActividad_delegaEnUseCase() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");
        EstadoActividadDomainEntity domain = new EstadoActividadDomainEntity();

        when(actualizarEstadoActividadUseCase.execute("ACT", request))
                .thenReturn(domain);

        EstadoActividadDomainEntity result = service.actualizarEstadoActividad(request);

        assertEquals(domain, result);
    }

    @Test
    void eliminarEstadoActividad_delegaEnUseCase() {
        service.eliminarEstadoActividad("ID1");

        verify(eliminarEstadoActividadUseCase).execute("ID1");
    }

    @Test
    void obtenerEstadoActividadPorId_devuelveEntidadSiExiste() {
        EstadoActividadDomainEntity domain = new EstadoActividadDomainEntity();
        when(estadoActividadRepository.findById("ID1"))
                .thenReturn(Optional.of(domain));

        EstadoActividadDomainEntity result = service.obtenerEstadoActividadPorId("ID1");

        assertEquals(domain, result);
    }

    @Test
    void obtenerEstadoActividadPorId_devuelveNullSiNoExiste() {
        when(estadoActividadRepository.findById("ID1"))
                .thenReturn(Optional.empty());

        EstadoActividadDomainEntity result = service.obtenerEstadoActividadPorId("ID1");

        assertNull(result);
    }

    @Test
    void obtenerTodosLosEstadosActividad_devuelveLista() {
        when(estadoActividadRepository.findAll())
                .thenReturn(List.of(new EstadoActividadDomainEntity()));

        List<EstadoActividadDomainEntity> result =
                service.obtenerTodosLosEstadosActividad();

        assertEquals(1, result.size());
    }

    @Test
    void listarEstadosActividad_devuelveLista() {
        when(estadoActividadRepository.findAll())
                .thenReturn(List.of(new EstadoActividadDomainEntity()));

        List<EstadoActividadDomainEntity> result =
                service.listarEstadosActividad();

        assertEquals(1, result.size());
    }
}
