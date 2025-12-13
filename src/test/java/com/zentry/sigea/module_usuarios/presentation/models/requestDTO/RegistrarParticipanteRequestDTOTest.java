package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class RegistrarParticipanteRequestDTOTest {

    @Test
    void getters_work_correctly() throws Exception {
        RegistrarParticipanteRequestDTO dto =
            new RegistrarParticipanteRequestDTO();

        set(dto, "nombres", "Juan");
        set(dto, "apellidos", "Perez");
        set(dto, "correo", "juan@test.com");
        set(dto, "password", "12345678");
        set(dto, "dni", "12345678");
        set(dto, "telefono", "999999999");
        set(dto, "extensionTelefonica", "101");

        assertEquals("Juan", dto.getNombres());
        assertEquals("Perez", dto.getApellidos());
        assertEquals("juan@test.com", dto.getCorreo());
        assertEquals("12345678", dto.getPassword());
        assertEquals("12345678", dto.getDni());
        assertEquals("999999999", dto.getTelefono());
        assertEquals("101", dto.getExtensionTelefonica());
    }

    private void set(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }
}
