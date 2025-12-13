package com.zentry.sigea.module_usuarios.presentation.models.requestDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class RegistrarAsistenciaItemRequestDTOTest {

    @Test
    void getters_work_correctly() throws Exception {
        RegistrarAsistenciaItemRequestDTO dto =
            new RegistrarAsistenciaItemRequestDTO();

        LocalDateTime now = LocalDateTime.now();

        Field f1 = RegistrarAsistenciaItemRequestDTO.class.getDeclaredField("inscripcionId");
        f1.setAccessible(true);
        f1.set(dto, "insc-1");

        Field f2 = RegistrarAsistenciaItemRequestDTO.class.getDeclaredField("presente");
        f2.setAccessible(true);
        f2.set(dto, true);

        Field f3 = RegistrarAsistenciaItemRequestDTO.class.getDeclaredField("registradoEn");
        f3.setAccessible(true);
        f3.set(dto, now);

        assertEquals("insc-1", dto.getInscripcionId());
        assertTrue(dto.getPresente());
        assertEquals(now, dto.getRegistradoEn());
    }
}
