package com.zentry.sigea.module_notificaciones.presentation.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.ITipoNotificacionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TipoNotificacionController.class)
class TipoNotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITipoNotificacionRepository tipoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarTipos_ok() throws Exception {
        TipoNotificacionDomainEntity tipo = TipoNotificacionDomainEntity.create("PAGO", "Pago");

        when(tipoRepository.findAll()).thenReturn(List.of(tipo));

        mockMvc.perform(get("/api/v1/tipos-notificacion/listar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].codigo").value("PAGO"));
    }

    @Test
    void obtenerTipoPorCodigo_ok() throws Exception {
        TipoNotificacionDomainEntity tipo = TipoNotificacionDomainEntity.create("CERTIFICADO", "Certificado");

        when(tipoRepository.findByCodigo("CERTIFICADO")).thenReturn(Optional.of(tipo));

        mockMvc.perform(get("/api/v1/tipos-notificacion/obtener/codigo/CERTIFICADO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.codigo").value("CERTIFICADO"));
    }

    @Test
    void eliminarTipo_noExiste_404() throws Exception {
        when(tipoRepository.existsById("99")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/tipos-notificacion/eliminar/99"))
            .andExpect(status().isNotFound());
    }
}
