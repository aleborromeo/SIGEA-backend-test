package com.zentry.sigea.module_usuarios.presentation.api.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.LoginResponseDTO;
import com.zentry.sigea.module_usuarios.services.UsuarioService;

@WebMvcTest(UsuarioApiRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class,
    OAuth2ResourceServerAutoConfiguration.class
})
class UsuarioApiRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void login_ok() throws Exception {
        LoginResponseDTO dto = mock(LoginResponseDTO.class);
        when(dto.getAccessToken()).thenReturn("access");
        when(dto.getCorreoVerificado()).thenReturn(true);

        when(usuarioService.login(any(), any(), anyBoolean()))
            .thenReturn(dto);

        mockMvc.perform(post("/api/v1/usuarios/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "correo":"test@test.com",
                  "password":"123456",
                  "rememberMe":false
                }
                """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.accessToken").value("access"))
            .andExpect(jsonPath("$.data.emailVerificado").value(true));
    }
}
