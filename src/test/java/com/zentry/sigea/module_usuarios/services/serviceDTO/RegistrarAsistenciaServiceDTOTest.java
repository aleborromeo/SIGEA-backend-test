package com.zentry.sigea.module_usuarios.services.serviceDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class RegistrarAsistenciaServiceDTOTest {

    @Test
    void setters_and_getters_work_correctly() {
        RegistrarAsistenciaServiceDTO dto =
            new RegistrarAsistenciaServiceDTO();

        RegistrarAsistenciaItemServiceDTO item =
            new RegistrarAsistenciaItemServiceDTO(
                "insc-1",
                true,
                java.util.Optional.empty()
            );

        dto.setSesionId("sesion-1");
        dto.setRegistrarAsistenciaItemServiceDTOs(List.of(item));

        assertEquals("sesion-1", dto.getSesionId());
        assertNotNull(dto.getRegistrarAsistenciaItemServiceDTOs());
        assertEquals(1, dto.getRegistrarAsistenciaItemServiceDTOs().size());
        assertEquals("insc-1",
            dto.getRegistrarAsistenciaItemServiceDTOs().get(0).getInscripcionId()
        );
    }
}
