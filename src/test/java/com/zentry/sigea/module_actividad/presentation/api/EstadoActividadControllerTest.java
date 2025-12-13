package com.zentry.sigea.module_actividad.presentation.api;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.EstadoActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.responseDTO.EstadoActividadResponse;
import com.zentry.sigea.module_actividad.services.EstadoActividadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoActividadControllerTest {

    @Mock
    private EstadoActividadService estadoActividadService;

    @InjectMocks
    private EstadoActividadController controller;

    @Test
    void createEstadoActividad_success() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");
        when(estadoActividadService.crearEstadoActividad(request))
                .thenReturn("creado");

        ResponseEntity<String> response = controller.createEstadoActividad(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("creado", response.getBody());
    }

    @Test
    void createEstadoActividad_validationError() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");
        when(estadoActividadService.crearEstadoActividad(request))
                .thenThrow(new IllegalArgumentException("error"));

        ResponseEntity<String> response = controller.createEstadoActividad(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createEstadoActividad_internalError() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");
        when(estadoActividadService.crearEstadoActividad(request))
                .thenThrow(new RuntimeException("fallo"));

        ResponseEntity<String> response = controller.createEstadoActividad(request);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void listarEstadoActividad_ok() {
        EstadoActividadDomainEntity e1 = new EstadoActividadDomainEntity();
        e1.setEstadoActividadId("1");
        e1.setCodigo("ACT");
        e1.setEtiqueta("Activo");

        when(estadoActividadService.listarEstadosActividad())
                .thenReturn(List.of(e1));

        ResponseEntity<List<EstadoActividadResponse>> response = controller.listarEstadoActividad();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        EstadoActividadResponse r = response.getBody().get(0);
        assertEquals("1", r.getId());
        assertEquals("ACT", r.getCodigo());
    }

    @Test
    void eliminarEstadoActividad_success() {
        ResponseEntity<Void> response = controller.eliminarEstadoActividad("ID1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(estadoActividadService).eliminarEstadoActividad("ID1");
    }

    @Test
    void eliminarEstadoActividad_notFound() {
        doThrow(new IllegalArgumentException("no")).when(estadoActividadService)
                .eliminarEstadoActividad("ID1");

        ResponseEntity<Void> response = controller.eliminarEstadoActividad("ID1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void eliminarEstadoActividad_internalError() {
        doThrow(new RuntimeException("err")).when(estadoActividadService)
                .eliminarEstadoActividad("ID1");

        ResponseEntity<Void> response = controller.eliminarEstadoActividad("ID1");

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void actualizarEstado_success() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");
        EstadoActividadDomainEntity domain = new EstadoActividadDomainEntity();
        domain.setEstadoActividadId("1");
        domain.setCodigo("ACT");
        domain.setEtiqueta("Activo");

        when(estadoActividadService.actualizarEstadoActividad(request)).thenReturn(domain);

        ResponseEntity<EstadoActividadResponse> response =
                controller.actualizarEstado("1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());
    }

    @Test
    void actualizarEstado_notFound() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");
        when(estadoActividadService.actualizarEstadoActividad(request))
                .thenThrow(new IllegalArgumentException("no"));

        ResponseEntity<EstadoActividadResponse> response =
                controller.actualizarEstado("1", request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void actualizarEstado_internalError() {
        EstadoActividadRequest request = new EstadoActividadRequest("ACT", "Activo");
        when(estadoActividadService.actualizarEstadoActividad(request))
                .thenThrow(new RuntimeException("err"));

        ResponseEntity<EstadoActividadResponse> response =
                controller.actualizarEstado("1", request);

        assertEquals(500, response.getStatusCodeValue());
    }
}
