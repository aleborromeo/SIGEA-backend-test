package com.zentry.sigea.module_actividad.presentation.api;

import com.zentry.sigea.module_actividad.presentation.models.requestDTO.ActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.CrearActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.responseDTO.ActividadResponse;
import com.zentry.sigea.module_actividad.services.ActividadService;

import org.junit.jupiter.api.Disabled;
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
class ActividadControllerTest {

    @Mock
    private ActividadService actividadService;

    @InjectMocks
    private ActividadController controller;

    @Test
    void crearActividad_success() {
        CrearActividadRequest request = new CrearActividadRequest();
        when(actividadService.crearActividad(request)).thenReturn("ACT-1");

        ResponseEntity<ActividadResponse> response = controller.crearActividad(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ACT-1", response.getBody().getId());
        assertEquals("Actividad registrada con éxito", response.getBody().getDescripcion());
        verify(actividadService).crearActividad(request);
    }

    @Test
    void crearActividad_validationError() {
        CrearActividadRequest request = new CrearActividadRequest();
        when(actividadService.crearActividad(request))
                .thenThrow(new IllegalArgumentException("Datos inválidos"));

        ResponseEntity<ActividadResponse> response = controller.crearActividad(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getDescripcion().contains("Error de validación"));
    }

    @Test
    void crearActividad_internalError() {
        CrearActividadRequest request = new CrearActividadRequest();
        when(actividadService.crearActividad(request))
                .thenThrow(new RuntimeException("Fallo interno"));

        ResponseEntity<ActividadResponse> response = controller.crearActividad(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getDescripcion().startsWith("Error interno:"));
    }

    @Test
    void obtenerActividad_found() {
        ActividadResponse a1 = new ActividadResponse();
        a1.setId("1");
        ActividadResponse a2 = new ActividadResponse();
        a2.setId("2");

        when(actividadService.listarActividades()).thenReturn(List.of(a1, a2));

        ResponseEntity<ActividadResponse> response = controller.obtenerActividad("2");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("2", response.getBody().getId());
    }

    @Test
    @Disabled
    void obtenerActividad_serviceThrows_returnsBadRequest() {
        when(actividadService.listarActividades())
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<ActividadResponse> response = controller.obtenerActividad("X");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void listarActividades_ok() {
        when(actividadService.listarActividades())
                .thenReturn(List.of(new ActividadResponse(), new ActividadResponse()));

        ResponseEntity<List<ActividadResponse>> response = controller.listarActividades();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void health_ok() {
        ResponseEntity<String> response = controller.health();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Actividades API is running", response.getBody());
    }

    @Test
    void eliminarActividad_success() {
        ResponseEntity<Void> response = controller.eliminarActividad("ID1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(actividadService).eliminarActividad("ID1");
    }

    @Test
    void eliminarActividad_notFound() {
        doThrow(new IllegalArgumentException("No existe")).when(actividadService).eliminarActividad("ID1");

        ResponseEntity<Void> response = controller.eliminarActividad("ID1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void eliminarActividad_internalError() {
        doThrow(new RuntimeException("Error")).when(actividadService).eliminarActividad("ID1");

        ResponseEntity<Void> response = controller.eliminarActividad("ID1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void actualizarActividad_success() {
        ActividadRequest request = new ActividadRequest();
        when(actividadService.actualizarActividad("ID1", request)).thenReturn("ID1");

        ResponseEntity<String> response = controller.actualizarActividad("ID1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ID1", response.getBody());
    }

    @Test
    void actualizarActividad_notFound() {
        ActividadRequest request = new ActividadRequest();
        when(actividadService.actualizarActividad("ID1", request))
                .thenThrow(new IllegalArgumentException("No existe"));

        ResponseEntity<String> response = controller.actualizarActividad("ID1", request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void actualizarActividad_internalError() {
        ActividadRequest request = new ActividadRequest();
        when(actividadService.actualizarActividad("ID1", request))
                .thenThrow(new RuntimeException("Fallo"));

        ResponseEntity<String> response = controller.actualizarActividad("ID1", request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().startsWith("Error interno:"));
    }
}
