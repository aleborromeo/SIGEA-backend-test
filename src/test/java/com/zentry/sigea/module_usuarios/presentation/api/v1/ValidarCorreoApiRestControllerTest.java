package com.zentry.sigea.module_usuarios.presentation.api.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import org.springframework.test.web.servlet.MockMvc;

import com.zentry.sigea.module_usuarios.services.EnviarCodigoVerificacionPorEmailService;
import com.zentry.sigea.module_usuarios.services.ValidarCodigoEnviadoService;

@WebMvcTest(ValidarCorreoApiRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class,
    OAuth2ResourceServerAutoConfiguration.class
})
class ValidarCorreoApiRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnviarCodigoVerificacionPorEmailService enviarService;

    @MockBean
    private ValidarCodigoEnviadoService validarService;

    @Test
    void enviarCodigo_ok() throws Exception {
        mockMvc.perform(post("/api/v1/usuarios/validar-correo/enviar-codigo-verificacion")
                .param("correo", "test@test.com")
                .param("nombres", "Juan"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void validarCodigo_incorrecto() throws Exception {
        when(validarService.execute(any(), any()))
            .thenReturn(false);

        mockMvc.perform(post("/api/v1/usuarios/validar-correo/validar-codigo-verificacion")
                .param("correo", "test@test.com")
                .param("codigo", "1234"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(false));
    }
}
