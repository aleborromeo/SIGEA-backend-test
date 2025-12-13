package com.zentry.sigea.module_inscripciones.presentation.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.EstadoInscripcionRequest;
import com.zentry.sigea.module_inscripciones.presentation.models.responseDTO.EstadoInscripcionResponse;
import com.zentry.sigea.module_inscripciones.services.EstadoInscripcionService;

class EstadoInscripcionControllerTest {

    private EstadoInscripcionService service;
    private EstadoInscripcionController controller;

    @BeforeEach
    void setup() {
        service = mock(EstadoInscripcionService.class);
        controller = new EstadoInscripcionController(service);
    }

    // -------- crearEstadoInscripcion --------

    @Test
    void crearEstadoInscripcion_returnsCreatedOnSuccess() {
        EstadoInscripcionRequest request = mock(EstadoInscripcionRequest.class);
        when(request.getCodigo()).thenReturn("PEN");
        when(request.getEtiqueta()).thenReturn("Pendiente");

        when(service.crearEstadoInscripcion(request)).thenReturn("creado OK");

        ResponseEntity<String> response = controller.crearEstadoInscripcion(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("creado OK", response.getBody());
        verify(service).crearEstadoInscripcion(request);
    }

    // -------- listarEstadosInscripcion --------

    @Test
    void listarEstadosInscripcion_returnsOkWithList() {
        List<EstadoInscripcionResponse> list = List.of(
            new EstadoInscripcionResponse("1", "PEN", "Pendiente")
        );
        when(service.listarEstadosInscripcion()).thenReturn(list);

        ResponseEntity<List<EstadoInscripcionResponse>> response =
                controller.listarEstadosInscripcion();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(service).listarEstadosInscripcion();
    }

    // -------- obtenerEstadoInscripcionPorId --------

    @Test
    void obtenerEstadoInscripcionPorId_returnsOkOnSuccess() {
        String id = "1";
        EstadoInscripcionResponse estado =
            new EstadoInscripcionResponse(id, "PEN", "Pendiente");
        when(service.obtenerEstadoInscripcionPorId(id)).thenReturn(estado);

        ResponseEntity<EstadoInscripcionResponse> response =
                controller.obtenerEstadoInscripcionPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(estado, response.getBody());
        verify(service).obtenerEstadoInscripcionPorId(id);
    }

    // -------- obtenerEstadoInscripcionPorCodigo --------

    @Test
    void obtenerEstadoInscripcionPorCodigo_returnsOkOnSuccess() {
        String codigo = "PEN";
        EstadoInscripcionResponse estado =
            new EstadoInscripcionResponse("1", codigo, "Pendiente");
        when(service.obtenerEstadoInscripcionPorCodigo(codigo)).thenReturn(estado);

        ResponseEntity<EstadoInscripcionResponse> response =
                controller.obtenerEstadoInscripcionPorCodigo(codigo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(estado, response.getBody());
        verify(service).obtenerEstadoInscripcionPorCodigo(codigo);
    }

    // -------- eliminarEstadoInscripcion --------

    @Test
    void eliminarEstadoInscripcion_returnsNoContentOnSuccess() {
        String id = "1";

        ResponseEntity<Void> response = controller.eliminarEstadoInscripcion(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).eliminarEstadoInscripcion(id);
    }

    // -------- health --------

    @Test
    void health_returnsOk() {
        ResponseEntity<String> response = controller.health();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Estados de Inscripción API is running", response.getBody());
    }
}
