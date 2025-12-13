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

import com.zentry.sigea.module_actividad.services.usecases.actividad.EliminarActividadUseCase;
import com.zentry.sigea.module_inscripciones.services.InscripcionService;
import com.zentry.sigea.module_usuarios.services.ParticipanteService;

@WebMvcTest(ParticipanteApiRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class,
    OAuth2ResourceServerAutoConfiguration.class
})
class ParticipanteApiRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipanteService participanteService;

    @MockBean
    private InscripcionService inscripcionService;

    @MockBean
    private EliminarActividadUseCase eliminarActividadUseCase;

    @Test
    void homeParticipante_ok() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/participante/home"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void registrarParticipante_badRequest() throws Exception {
        mockMvc.perform(post("/api/v1/usuarios/participante/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void registrarParticipante_ok() throws Exception {
        when(participanteService.registrarParticipante(any()))
            .thenReturn("Registrado");

        mockMvc.perform(post("/api/v1/usuarios/participante/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true));
    }
}
