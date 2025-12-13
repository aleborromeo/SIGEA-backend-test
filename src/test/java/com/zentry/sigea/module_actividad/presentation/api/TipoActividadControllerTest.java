package com.zentry.sigea.module_actividad.presentation.api;

import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.TipoActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.responseDTO.TipoActividadResponse;
import com.zentry.sigea.module_actividad.services.TipoActividadService;
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
class TipoActividadControllerTest {

    @Mock
    private TipoActividadService tipoActividadService;

    @InjectMocks
    private TipoActividadController controller;

    @Test
    void createTipoActividad_success() {
        TipoActividadRequest request = new TipoActividadRequest("Seminario", "Desc");
        when(tipoActividadService.crearTipoActividad(request)).thenReturn("OK");

        ResponseEntity<String> response = controller.createTipoActividad(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OK", response.getBody());
    }

    @Test
    void createTipoActividad_validationError() {
        TipoActividadRequest request = new TipoActividadRequest("Seminario", "Desc");
        when(tipoActividadService.crearTipoActividad(request))
                .thenThrow(new IllegalArgumentException("bad"));

        ResponseEntity<String> response = controller.createTipoActividad(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTipoActividad_internalError() {
        TipoActividadRequest request = new TipoActividadRequest("Seminario", "Desc");
        when(tipoActividadService.crearTipoActividad(request))
                .thenThrow(new RuntimeException("err"));

        ResponseEntity<String> response = controller.createTipoActividad(request);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void listarTiposActividad_ok() {
        TipoActividadDomainEntity d = new TipoActividadDomainEntity();
        d.setTipoActividadId("T1");
        d.setNombreActividad("Taller");
        d.setDescripcion("Tech");

        when(tipoActividadService.listarTiposActividad())
                .thenReturn(List.of(d));

        ResponseEntity<List<TipoActividadResponse>> response =
                controller.listarTiposActividad();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        TipoActividadResponse r = response.getBody().get(0);
        assertEquals("T1", r.getId());
        assertEquals("Taller", r.getNombreActividad());
    }

    @Test
    void eliminarTipoActividad_success() {
        ResponseEntity<Void> response = controller.eliminarTipoActividad("ID1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tipoActividadService).eliminarTipoActividad("ID1");
    }

    @Test
    void eliminarTipoActividad_notFound() {
        doThrow(new IllegalArgumentException("no")).when(tipoActividadService)
                .eliminarTipoActividad("ID1");

        ResponseEntity<Void> response = controller.eliminarTipoActividad("ID1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void eliminarTipoActividad_internalError() {
        doThrow(new RuntimeException("err")).when(tipoActividadService)
                .eliminarTipoActividad("ID1");

        ResponseEntity<Void> response = controller.eliminarTipoActividad("ID1");

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void actualizarTipoActividad_success() {
        TipoActividadRequest request = new TipoActividadRequest("Seminario", "Desc");
        TipoActividadDomainEntity domain = new TipoActividadDomainEntity();
        domain.setTipoActividadId("T1");
        domain.setNombreActividad("Seminario");
        domain.setDescripcion("Desc");

        when(tipoActividadService.actualizarTipoActividad("T1", request))
                .thenReturn(domain);

        ResponseEntity<TipoActividadResponse> response =
                controller.actualizarTipoActividad("T1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("T1", response.getBody().getId());
    }

    @Test
    void actualizarTipoActividad_notFound() {
        TipoActividadRequest request = new TipoActividadRequest("Seminario", "Desc");
        when(tipoActividadService.actualizarTipoActividad("T1", request))
                .thenThrow(new IllegalArgumentException("no"));

        ResponseEntity<TipoActividadResponse> response =
                controller.actualizarTipoActividad("T1", request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void actualizarTipoActividad_internalError() {
        TipoActividadRequest request = new TipoActividadRequest("Seminario", "Desc");
        when(tipoActividadService.actualizarTipoActividad("T1", request))
                .thenThrow(new RuntimeException("err"));

        ResponseEntity<TipoActividadResponse> response =
                controller.actualizarTipoActividad("T1", request);

        assertEquals(500, response.getStatusCodeValue());
    }
}
