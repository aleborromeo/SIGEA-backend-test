package com.zentry.sigea.module_notificaciones.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ActualizarNotificacionRequestTest {

    @Test
    void constructorVacio_noLanzaErrores() {
        ActualizarNotificacionRequest request = new ActualizarNotificacionRequest();
        assertNotNull(request);
    }

    @Test
    void constructorConParametros_asignaCamposCorrectamente() {
        ActualizarNotificacionRequest request =
            new ActualizarNotificacionRequest("Mensaje actualizado", "estado-123");

        assertEquals("Mensaje actualizado", request.getMensaje());
        assertEquals("estado-123", request.getEstadoNotificacionId());
    }

    @Test
    void settersYGetters_funcionanCorrectamente() {
        ActualizarNotificacionRequest request = new ActualizarNotificacionRequest();

        request.setMensaje("Nuevo mensaje");
        request.setEstadoNotificacionId("estado-456");

        assertEquals("Nuevo mensaje", request.getMensaje());
        assertEquals("estado-456", request.getEstadoNotificacionId());
    }

    @Test
    void permiteValoresNulos() {
        ActualizarNotificacionRequest request = new ActualizarNotificacionRequest();

        request.setMensaje(null);
        request.setEstadoNotificacionId(null);

        assertNull(request.getMensaje());
        assertNull(request.getEstadoNotificacionId());
    }
}
