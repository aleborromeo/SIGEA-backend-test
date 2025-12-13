package com.zentry.sigea.module_inscripciones.presentation.models.responseDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;

class InscripcionResponseTest {

    @Test
    void emptyConstructorAndSetters_workCorrectly() {
        InscripcionResponse response = new InscripcionResponse();

        LocalDate fecha = LocalDate.of(2025, 1, 10);
        LocalDateTime now = LocalDateTime.now();

        EstadoInscripcionDomainEntity estado = new EstadoInscripcionDomainEntity();
        estado.setId("100");
        estado.setCodigo("PEN");
        estado.setEtiqueta("Pendiente");

        response.setId("1");
        response.setFechaInscripcion(fecha);
        response.setUsuarioId("user-1");
        response.setActividadId("act-1");
        response.setEstado(estado);
        response.setFechaCreacion(now);
        response.setFechaActualizacion(now);
        response.setConfirmada(false);
        response.setCancelada(false);
        response.setPendiente(true);

        assertEquals("1", response.getId());
        assertEquals(fecha, response.getFechaInscripcion());
        assertEquals("user-1", response.getUsuarioId());
        assertEquals("act-1", response.getActividadId());
        assertEquals(estado, response.getEstado());
        assertEquals(now, response.getFechaCreacion());
        assertEquals(now, response.getFechaActualizacion());
        assertFalse(response.isConfirmada());
        assertFalse(response.isCancelada());
        assertTrue(response.isPendiente());
    }

    @Test
    void fullConstructor_setsAllFields() {
        LocalDate fecha = LocalDate.of(2025, 3, 15);
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = created.plusHours(1);

        EstadoInscripcionDomainEntity estado = new EstadoInscripcionDomainEntity();
        estado.setId("200");
        estado.setCodigo("CONF");
        estado.setEtiqueta("Confirmada");

        InscripcionResponse response = new InscripcionResponse(
            "10",
            fecha,
            "user-x",
            "act-x",
            estado,
            created,
            updated,
            true,
            false,
            false
        );

        assertEquals("10", response.getId());
        assertEquals(fecha, response.getFechaInscripcion());
        assertEquals("user-x", response.getUsuarioId());
        assertEquals("act-x", response.getActividadId());
        assertEquals(estado, response.getEstado());
        assertEquals(created, response.getFechaCreacion());
        assertEquals(updated, response.getFechaActualizacion());
        assertTrue(response.isConfirmada());
        assertFalse(response.isCancelada());
        assertFalse(response.isPendiente());
    }

    @Test
    void fromEntity_buildsResponseCorrectly() {
        // ---------- DOMAIN SETUP ----------
        EstadoInscripcionDomainEntity estado = EstadoInscripcionDomainEntity.create("CONFIRMADA", "Confirmada");
        estado.setId("300");

        InscripcionDomainEntity domain = InscripcionDomainEntity.create(
            LocalDate.of(2025, 5, 20),
            "act-99",
            estado,
            "user-99"
        );
        domain.setId("500");

        // Make the domain return consistent flags
        assertTrue(domain.estaConfirmada());
        assertFalse(domain.estaCancelada());
        assertFalse(domain.estaPendiente());

        // ---------- CALL ----------
        InscripcionResponse response = InscripcionResponse.fromEntity(domain);

        // ---------- ASSERT ----------
        assertEquals("500", response.getId());
        assertEquals("user-99", response.getUsuarioId());
        assertEquals("act-99", response.getActividadId());
        assertEquals(domain.getFechaInscripcion(), response.getFechaInscripcion());
        assertEquals(domain.getCreatedAt(), response.getFechaCreacion());
        assertEquals(domain.getUpdatedAt(), response.getFechaActualizacion());

        assertTrue(response.isConfirmada());
        assertFalse(response.isCancelada());
        assertFalse(response.isPendiente());

        assertEquals("CONFIRMADA", response.getEstado().getCodigo());
    }
}
