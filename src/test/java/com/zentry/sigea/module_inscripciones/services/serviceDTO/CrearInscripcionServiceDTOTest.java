package com.zentry.sigea.module_inscripciones.services.serviceDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CrearInscripcionServiceDTOTest {

    @Test
    void settersAndGetters_workCorrectly() {
        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();

        LocalDate fecha = LocalDate.of(2025, 2, 15);

        dto.setFechaInscripcion(fecha);
        dto.setActividadId("act-123");
        dto.setEstadoId("estado-1");
        dto.setUsuarioId("user-999");

        assertEquals(fecha, dto.getFechaInscripcion());
        assertEquals("act-123", dto.getActividadId());
        assertEquals("estado-1", dto.getEstadoId());
        assertEquals("user-999", dto.getUsuarioId());
    }

    @Test
    void dto_allowsNullValuesInitially() {
        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();

        assertNull(dto.getFechaInscripcion());
        assertNull(dto.getActividadId());
        assertNull(dto.getEstadoId());
        assertNull(dto.getUsuarioId());
    }

    @Test
    void dto_handlesDifferentValuesCorrectly() {
        CrearInscripcionServiceDTO dto = new CrearInscripcionServiceDTO();

        dto.setFechaInscripcion(LocalDate.now());
        dto.setActividadId("A");
        dto.setEstadoId("B");
        dto.setUsuarioId("C");

        assertEquals("A", dto.getActividadId());
        assertEquals("B", dto.getEstadoId());
        assertEquals("C", dto.getUsuarioId());
    }
}
