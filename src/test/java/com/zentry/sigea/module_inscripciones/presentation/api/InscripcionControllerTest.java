package com.zentry.sigea.module_inscripciones.presentation.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.InscripcionRequest;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.InscripcionResponse;
import com.zentry.sigea.module_inscripciones.services.InscripcionService;
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;

class InscripcionControllerTest {

    private InscripcionService service;
    private InscripcionController controller;

    @BeforeEach
    void setup() {
        service = mock(InscripcionService.class);
        controller = new InscripcionController(service);
    }

    // -------- crearInscripcion --------

    @Test
    void crearInscripcion_returnsCreatedOnSuccess() {
        // request que llega al controller (mock para no depender de setters/constructor)
        InscripcionRequest request = mock(InscripcionRequest.class);
        when(request.getUsuarioId()).thenReturn("user-1");
        when(request.getActividadId()).thenReturn("act-1");
        when(request.getFechaInscripcion()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.getEstadoId()).thenReturn("estado-1");

        // lo que devuelve el servicio al crear
        when(service.crearInscripcion(any(CrearInscripcionServiceDTO.class)))
                .thenReturn("insc-1");

        InscripcionResponse responseFromService = new InscripcionResponse();
        when(service.obtenerInscripcionPorId("insc-1"))
                .thenReturn(responseFromService);

        ResponseEntity<InscripcionResponse> response =
                controller.crearInscripcion(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(responseFromService, response.getBody());

        // verificaciones
        verify(service).crearInscripcion(any(CrearInscripcionServiceDTO.class));
        verify(service).obtenerInscripcionPorId("insc-1");
    }

    // -------- obtenerInscripcion --------

    @Test
    void obtenerInscripcion_returnsOkOnSuccess() {
        String id = "insc-1";
        InscripcionResponse expected = new InscripcionResponse();

        when(service.obtenerInscripcionPorId(id)).thenReturn(expected);

        ResponseEntity<InscripcionResponse> response =
                controller.obtenerInscripcion(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expected, response.getBody());
        verify(service).obtenerInscripcionPorId(id);
    }

    // -------- listarInscripciones --------

    @Test
    void listarInscripciones_returnsOkWithList() {
        List<InscripcionResponse> lista = List.of(new InscripcionResponse());
        when(service.listarInscripciones()).thenReturn(lista);

        ResponseEntity<List<InscripcionResponse>> response =
                controller.listarInscripciones();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
        verify(service).listarInscripciones();
    }

    // -------- obtenerInscripcionesPorUsuario --------

    @Test
    void obtenerInscripcionesPorUsuario_returnsOk() {
        String usuarioId = "user-1";
        List<InscripcionResponse> lista = List.of(new InscripcionResponse());
        when(service.obtenerInscripcionesPorUsuario(usuarioId)).thenReturn(lista);

        ResponseEntity<List<InscripcionResponse>> response =
                controller.obtenerInscripcionesPorUsuario(usuarioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
        verify(service).obtenerInscripcionesPorUsuario(usuarioId);
    }

    // -------- obtenerInscripcionesPorActividad --------

    @Test
    void obtenerInscripcionesPorActividad_returnsOk() {
        String actividadId = "act-1";
        List<InscripcionResponse> lista = List.of(new InscripcionResponse());
        when(service.obtenerInscripcionesPorActividad(actividadId)).thenReturn(lista);

        ResponseEntity<List<InscripcionResponse>> response =
                controller.obtenerInscripcionesPorActividad(actividadId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
        verify(service).obtenerInscripcionesPorActividad(actividadId);
    }

    // -------- actualizarInscripcion --------

    @Test
    void actualizarInscripcion_returnsOkOnSuccess() {
        String id = "insc-1";

        InscripcionRequest request = mock(InscripcionRequest.class);
        when(request.getUsuarioId()).thenReturn("user-1");
        when(request.getActividadId()).thenReturn("act-1");
        when(request.getFechaInscripcion()).thenReturn(LocalDate.of(2025, 1, 1));
        when(request.getEstadoId()).thenReturn("estado-1");

        InscripcionResponse updated = new InscripcionResponse();
        when(service.actualizarInscripcion(id, request)).thenReturn(updated);

        ResponseEntity<InscripcionResponse> response =
                controller.actualizarInscripcion(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updated, response.getBody());
        verify(service).actualizarInscripcion(id, request);
    }

    // -------- eliminarInscripcion --------

    @Test
    void eliminarInscripcion_returnsNoContentOnSuccess() {
        String id = "insc-1";

        ResponseEntity<Void> response = controller.eliminarInscripcion(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).eliminarInscripcion(id);
    }

    // -------- health --------

    @Test
    void health_returnsOk() {
        ResponseEntity<String> response = controller.health();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inscripciones API is running", response.getBody());
    }
}
