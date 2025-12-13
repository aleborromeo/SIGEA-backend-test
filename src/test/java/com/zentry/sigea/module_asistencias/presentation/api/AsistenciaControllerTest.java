package com.zentry.sigea.module_asistencias.presentation.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.AsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.responseDTO.AsistenciaResponse;
import com.zentry.sigea.module_asistencias.services.AsistenciaService;

class AsistenciaControllerTest {

    private AsistenciaService asistenciaService;
    private AsistenciaController controller;

    @BeforeEach
    void setup() {
        asistenciaService = mock(AsistenciaService.class);
        controller = new AsistenciaController(asistenciaService);
    }

    // ---------------- registrarAsistencia ----------------

    @Test
    void registrarAsistencia_devuelve201_cuandoOk() {
        RegistrarAsistenciaRequest request = mock(RegistrarAsistenciaRequest.class);

        when(asistenciaService.registrarAsistencia(request)).thenReturn("Registrado");

        ResponseEntity<String> resp = controller.registrarAsistencia(request);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("Registrado", resp.getBody());
        verify(asistenciaService).registrarAsistencia(request);
    }

    @Test
    void registrarAsistencia_devuelve400_cuandoIllegalArgument() {
        RegistrarAsistenciaRequest request = mock(RegistrarAsistenciaRequest.class);

        when(asistenciaService.registrarAsistencia(request))
            .thenThrow(new IllegalArgumentException("bad"));

        ResponseEntity<String> resp = controller.registrarAsistencia(request);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("bad", resp.getBody());
        verify(asistenciaService).registrarAsistencia(request);
    }

    @Test
    void registrarAsistencia_devuelve500_cuandoException() {
        RegistrarAsistenciaRequest request = mock(RegistrarAsistenciaRequest.class);

        when(asistenciaService.registrarAsistencia(request))
            .thenThrow(new RuntimeException("boom"));

        ResponseEntity<String> resp = controller.registrarAsistencia(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertEquals("Error interno del servidor", resp.getBody());
        verify(asistenciaService).registrarAsistencia(request);
    }

    // ---------------- obtenerAsistencia ----------------

    @Test
    void obtenerAsistencia_devuelve200_cuandoExiste() {
        AsistenciaResponse response = new AsistenciaResponse();
        when(asistenciaService.obtenerAsistenciaPorId("a1")).thenReturn(response);

        ResponseEntity<AsistenciaResponse> resp = controller.obtenerAsistencia("a1");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(response, resp.getBody());
        verify(asistenciaService).obtenerAsistenciaPorId("a1");
    }

    @Test
    void obtenerAsistencia_devuelve404_cuandoNoExiste() {
        when(asistenciaService.obtenerAsistenciaPorId("a1"))
            .thenThrow(new IllegalArgumentException("no"));

        ResponseEntity<AsistenciaResponse> resp = controller.obtenerAsistencia("a1");

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(asistenciaService).obtenerAsistenciaPorId("a1");
    }

    @Test
    void obtenerAsistencia_devuelve500_cuandoException() {
        when(asistenciaService.obtenerAsistenciaPorId("a1"))
            .thenThrow(new RuntimeException("boom"));

        ResponseEntity<AsistenciaResponse> resp = controller.obtenerAsistencia("a1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(asistenciaService).obtenerAsistenciaPorId("a1");
    }

    // ---------------- actualizarAsistencia ----------------

    @Test
    void actualizarAsistencia_devuelve200_cuandoOk() {
        AsistenciaRequest request = mock(AsistenciaRequest.class);
        when(request.getPresente()).thenReturn(true);

        AsistenciaResponse updated = new AsistenciaResponse();
        when(asistenciaService.actualizarEstadoAsistencia("id-1", true)).thenReturn(updated);

        ResponseEntity<AsistenciaResponse> resp = controller.actualizarAsistencia("id-1", request);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(updated, resp.getBody());
        verify(asistenciaService).actualizarEstadoAsistencia("id-1", true);
    }

    @Test
    void actualizarAsistencia_devuelve404_cuandoIllegalArgument() {
        AsistenciaRequest request = mock(AsistenciaRequest.class);
        when(request.getPresente()).thenReturn(false);

        when(asistenciaService.actualizarEstadoAsistencia("id-1", false))
            .thenThrow(new IllegalArgumentException("no"));

        ResponseEntity<AsistenciaResponse> resp = controller.actualizarAsistencia("id-1", request);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(asistenciaService).actualizarEstadoAsistencia("id-1", false);
    }

    @Test
    void actualizarAsistencia_devuelve500_cuandoException() {
        AsistenciaRequest request = mock(AsistenciaRequest.class);
        when(request.getPresente()).thenReturn(false);

        when(asistenciaService.actualizarEstadoAsistencia("id-1", false))
            .thenThrow(new RuntimeException("boom"));

        ResponseEntity<AsistenciaResponse> resp = controller.actualizarAsistencia("id-1", request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(asistenciaService).actualizarEstadoAsistencia("id-1", false);
    }

    // ---------------- listarPorSesion ----------------

    @Test
    void listarPorSesion_devuelve200() {
        List<AsistenciaResponse> list = Collections.emptyList();
        when(asistenciaService.listarAsistenciasPorSesion("ses-1")).thenReturn(list);

        ResponseEntity<List<AsistenciaResponse>> resp = controller.listarPorSesion("ses-1");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(list, resp.getBody());
        verify(asistenciaService).listarAsistenciasPorSesion("ses-1");
    }

    @Test
    void listarPorSesion_devuelve500_cuandoException() {
        when(asistenciaService.listarAsistenciasPorSesion("ses-1"))
            .thenThrow(new RuntimeException("boom"));

        ResponseEntity<List<AsistenciaResponse>> resp = controller.listarPorSesion("ses-1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(asistenciaService).listarAsistenciasPorSesion("ses-1");
    }

    // ---------------- listarPorInscripcion ----------------

    @Test
    void listarPorInscripcion_devuelve200() {
        List<AsistenciaResponse> list = Collections.emptyList();
        when(asistenciaService.listarAsistenciasPorInscripcion("ins-1")).thenReturn(list);

        ResponseEntity<List<AsistenciaResponse>> resp = controller.listarPorInscripcion("ins-1");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(list, resp.getBody());
        verify(asistenciaService).listarAsistenciasPorInscripcion("ins-1");
    }

    @Test
    void listarPorInscripcion_devuelve500_cuandoException() {
        when(asistenciaService.listarAsistenciasPorInscripcion("ins-1"))
            .thenThrow(new RuntimeException("boom"));

        ResponseEntity<List<AsistenciaResponse>> resp = controller.listarPorInscripcion("ins-1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(asistenciaService).listarAsistenciasPorInscripcion("ins-1");
    }

    // ---------------- listarPresentesPorSesion ----------------

    @Test
    void listarPresentesPorSesion_devuelve200() {
        List<AsistenciaResponse> list = Collections.emptyList();
        when(asistenciaService.listarPresentesPorSesion("ses-1")).thenReturn(list);

        ResponseEntity<List<AsistenciaResponse>> resp = controller.listarPresentesPorSesion("ses-1");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(list, resp.getBody());
        verify(asistenciaService).listarPresentesPorSesion("ses-1");
    }

    @Test
    void listarPresentesPorSesion_devuelve500_cuandoException() {
        when(asistenciaService.listarPresentesPorSesion("ses-1"))
            .thenThrow(new RuntimeException("boom"));

        ResponseEntity<List<AsistenciaResponse>> resp = controller.listarPresentesPorSesion("ses-1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(asistenciaService).listarPresentesPorSesion("ses-1");
    }

    // ---------------- health ----------------

    @Test
    void health_devuelve200_yMensaje() {
        ResponseEntity<String> resp = controller.health();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("Asistencias API is running", resp.getBody());
    }
}
