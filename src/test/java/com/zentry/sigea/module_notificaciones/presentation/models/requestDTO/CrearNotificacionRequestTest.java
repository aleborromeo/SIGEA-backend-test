package com.zentry.sigea.module_notificaciones.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CrearNotificacionRequestTest {

    @Test
    void constructorVacio_noLanzaErrores() {
        CrearNotificacionRequest request = new CrearNotificacionRequest();
        assertNotNull(request);
    }

    @Test
    void constructorCompleto_asignaCorrectamenteCampos() {
        CrearNotificacionRequest request = new CrearNotificacionRequest(
            "usuario-1",
            "actividad-1",
            "TIPO-1",
            "Mensaje de prueba",
            "ESTADO-1",
            "SISTEMA"
        );

        assertEquals("usuario-1", request.getUsuarioId());
        assertEquals("actividad-1", request.getActividadId());
        assertEquals("TIPO-1", request.getTipoNotificacionId());
        assertEquals("Mensaje de prueba", request.getMensaje());
        assertEquals("ESTADO-1", request.getEstadoNotificacionId());
        assertEquals("SISTEMA", request.getCanal());
    }

    @Test
    void settersYGetters_funcionanCorrectamente() {
        CrearNotificacionRequest request = new CrearNotificacionRequest();

        request.setUsuarioId("usuario-2");
        request.setActividadId("actividad-2");
        request.setTipoNotificacionId("TIPO-2");
        request.setMensaje("Mensaje");
        request.setEstadoNotificacionId("ESTADO-2");
        request.setCanal("CORREO");

        assertEquals("usuario-2", request.getUsuarioId());
        assertEquals("actividad-2", request.getActividadId());
        assertEquals("TIPO-2", request.getTipoNotificacionId());
        assertEquals("Mensaje", request.getMensaje());
        assertEquals("ESTADO-2", request.getEstadoNotificacionId());
        assertEquals("CORREO", request.getCanal());
    }

    @Test
    void permiteActividadOpcional() {
        CrearNotificacionRequest request = new CrearNotificacionRequest(
            "usuario-3",
            null,
            "TIPO-3",
            "Mensaje sin actividad",
            null,
            "WHATSAPP"
        );

        assertEquals("usuario-3", request.getUsuarioId());
        assertNull(request.getActividadId());
        assertEquals("TIPO-3", request.getTipoNotificacionId());
        assertEquals("Mensaje sin actividad", request.getMensaje());
        assertNull(request.getEstadoNotificacionId());
        assertEquals("WHATSAPP", request.getCanal());
    }
}
