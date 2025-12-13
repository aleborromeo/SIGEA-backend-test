package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class RegistrarUsuarioRequestDTOTest {

    @Test
    void setters_and_getters_work() {
        RegistrarUsuarioRequestDTO dto =
            new RegistrarUsuarioRequestDTO();

        dto.setNombres("Juan");
        dto.setApellidos("Perez");
        dto.setCorreo("juan@test.com");
        dto.setPassword("12345678");
        dto.setDni("12345678");
        dto.setTelefono("999999999");
        dto.setExtensionTelefonica("101");
        dto.setRolId(List.of("ROLE_USER"));

        assertEquals("Juan", dto.getNombres());
        assertEquals("Perez", dto.getApellidos());
        assertEquals("juan@test.com", dto.getCorreo());
        assertEquals("12345678", dto.getPassword());
        assertEquals("12345678", dto.getDni());
        assertEquals("999999999", dto.getTelefono());
        assertEquals("101", dto.getExtensionTelefonica());
        assertEquals(1, dto.getRolId().size());
    }
}
