package com.zentry.sigea.module_notificaciones.presentation.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EstadoNotificacionController.class)
class EstadoNotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEstadoNotificacionRepository estadoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarEstados_ok() throws Exception {
        EstadoNotificacionDomainEntity estado = EstadoNotificacionDomainEntity.create("ENVIADA", "Enviada");

        when(estadoRepository.findAll()).thenReturn(List.of(estado));

        mockMvc.perform(get("/api/v1/estados-notificacion/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].codigo").value("ENVIADA"));
    }

    @Test
    void obtenerEstadoPorId_ok() throws Exception {
        EstadoNotificacionDomainEntity estado = EstadoNotificacionDomainEntity.create("LEIDA", "Leída");

        when(estadoRepository.findById("1")).thenReturn(Optional.of(estado));

        mockMvc.perform(get("/api/v1/estados-notificacion/obtener/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.codigo").value("LEIDA"));
    }

    @Test
    void eliminarEstado_noExiste_404() throws Exception {
        when(estadoRepository.existsById("99")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/estados-notificacion/eliminar/99"))
            .andExpect(status().isNotFound());
    }
}
