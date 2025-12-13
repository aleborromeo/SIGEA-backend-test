package com.zentry.sigea.module_usuarios.presentation.api.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.zentry.sigea.module_usuarios.services.OrganizadorService;

@WebMvcTest(OrganizadorApiRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class,
    OAuth2ResourceServerAutoConfiguration.class
})
class OrganizadorApiRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizadorService organizadorService;

    @Test
    void homeOrganizador_ok() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/organizador/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void registrarAsistencia_ok() throws Exception {
        when(organizadorService.registrarAsistencia(any()))
            .thenReturn("Asistencia registrada");

        mockMvc.perform(post("/api/v1/usuarios/organizador/registrar-asistencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void registrarAsistencia_errorInterno() throws Exception {
        when(organizadorService.registrarAsistencia(any()))
            .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/v1/usuarios/organizador/registrar-asistencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isInternalServerError());
    }
}
