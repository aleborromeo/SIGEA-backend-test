package com.zentry.sigea.module_notificaciones.presentation.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zentry.sigea.module_notificaciones.presentation.models.responseDTO.NotificacionResponse;
import com.zentry.sigea.module_notificaciones.services.NotificacionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarNotificaciones_ok() throws Exception {
        when(notificacionService.listarNotificaciones()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/notificaciones/listar"))
            .andExpect(status().isOk());
    }

    @Test
    void obtenerNotificacion_noExiste_404() throws Exception {
        when(notificacionService.obtenerNotificacionPorId("1"))
            .thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/api/v1/notificaciones/obtener/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void eliminarNotificacion_ok() throws Exception {
        doNothing().when(notificacionService).eliminarNotificacion("1");

        mockMvc.perform(delete("/api/v1/notificaciones/eliminar/1"))
            .andExpect(status().isNoContent());
    }
}
