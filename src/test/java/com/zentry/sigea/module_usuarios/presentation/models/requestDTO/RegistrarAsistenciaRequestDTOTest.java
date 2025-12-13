package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;

class RegistrarAsistenciaRequestDTOTest {

    @Test
    void getters_return_correct_values() throws Exception {
        RegistrarAsistenciaRequestDTO dto =
                new RegistrarAsistenciaRequestDTO();

        RegistrarAsistenciaItemRequestDTO item =
                new RegistrarAsistenciaItemRequestDTO();

        Field sesionId =
                RegistrarAsistenciaRequestDTO.class.getDeclaredField("sesionId");
        sesionId.setAccessible(true);
        sesionId.set(dto, "sesion-1");

        Field items =
                RegistrarAsistenciaRequestDTO.class.getDeclaredField(
                        "registrarAsistenciaItemRequestDTOs");
        items.setAccessible(true);
        items.set(dto, List.of(item));

        assertEquals("sesion-1", dto.getSesionId());
        assertEquals(1, dto.getRegistrarAsistenciaItemRequestDTOs().size());
    }
}
