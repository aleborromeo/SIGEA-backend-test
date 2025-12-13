package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CrearRolRequestDTOTest {

    @Test
    void getters_work_correctly() throws Exception {
        CrearRolRequestDTO dto = new CrearRolRequestDTO();

        Field nombreRol = CrearRolRequestDTO.class.getDeclaredField("nombreRol");
        nombreRol.setAccessible(true);
        nombreRol.set(dto, "ADMIN");

        Field descripcion = CrearRolRequestDTO.class.getDeclaredField("descripcion");
        descripcion.setAccessible(true);
        descripcion.set(dto, "Rol administrador");

        assertEquals("ADMIN", dto.getNombreRol());
        assertEquals("Rol administrador", dto.getDescripcion());
    }
}
