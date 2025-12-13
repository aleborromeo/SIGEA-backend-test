package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LoginUsuarioRequestDTOTest {

    @Test
    void setters_and_getters_work() {
        LoginUsuarioRequestDTO dto = new LoginUsuarioRequestDTO();

        dto.setCorreo("test@correo.com");
        dto.setPassword("123456");
        dto.setRememberMe(true);

        assertEquals("test@correo.com", dto.getCorreo());
        assertEquals("123456", dto.getPassword());
        assertTrue(dto.getRememberMe());
    }

    @Test
    void rememberMe_default_is_false() {
        LoginUsuarioRequestDTO dto = new LoginUsuarioRequestDTO();
        assertFalse(dto.getRememberMe());
    }
}
