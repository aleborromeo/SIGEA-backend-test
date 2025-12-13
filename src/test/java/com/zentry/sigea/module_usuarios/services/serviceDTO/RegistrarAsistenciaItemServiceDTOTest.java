package com.zentry.sigea.module_usuarios.services.serviceDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class RegistrarAsistenciaItemServiceDTOTest {

    @Test
    void constructor_sets_all_fields_when_date_present() {
        LocalDateTime now = LocalDateTime.now();

        RegistrarAsistenciaItemServiceDTO dto =
            new RegistrarAsistenciaItemServiceDTO(
                "insc-1",
                true,
                Optional.of(now)
            );

        assertEquals("insc-1", dto.getInscripcionId());
        assertTrue(dto.getPresente());
        assertEquals(now, dto.getRegistradoEn());
    }

    @Test
    void constructor_sets_registradoEn_null_when_optional_empty() {
        RegistrarAsistenciaItemServiceDTO dto =
            new RegistrarAsistenciaItemServiceDTO(
                "insc-2",
                false,
                Optional.empty()
            );

        assertEquals("insc-2", dto.getInscripcionId());
        assertFalse(dto.getPresente());
        assertNull(dto.getRegistradoEn());
    }
}
