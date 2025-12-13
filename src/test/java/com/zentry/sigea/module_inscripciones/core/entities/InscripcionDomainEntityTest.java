package com.zentry.sigea.module_inscripciones.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InscripcionDomainEntity")
class InscripcionDomainEntityTest {

    private EstadoInscripcionDomainEntity estadoPendiente() {
        return EstadoInscripcionDomainEntity.create("PENDIENTE", "Pendiente");
    }

    private EstadoInscripcionDomainEntity estadoConfirmada() {
        return EstadoInscripcionDomainEntity.create("CONFIRMADA", "Confirmada");
    }

    private EstadoInscripcionDomainEntity estadoCancelada() {
        return EstadoInscripcionDomainEntity.create("CANCELADA", "Cancelada");
    }

    @Test
    @DisplayName("create() debe crear inscripción correctamente")
    void create_inscripcionValida_ok() {
        LocalDate fecha = LocalDate.of(2024, 1, 10);

        InscripcionDomainEntity inscripcion =
            InscripcionDomainEntity.create(
                fecha,
                "actividad-123",
                estadoPendiente(),
                "usuario-456"
            );

        assertNotNull(inscripcion);
        assertEquals(fecha, inscripcion.getFechaInscripcion());
        assertEquals("actividad-123", inscripcion.getActividadId());
        assertEquals("usuario-456", inscripcion.getUsuarioId());
        assertEquals("PENDIENTE", inscripcion.getEstadoInscripcionDomainEntity().getCodigo());
        assertNotNull(inscripcion.getCreatedAt());
        assertNotNull(inscripcion.getUpdatedAt());
    }

    @Test
    @DisplayName("create() sin fecha usa fecha actual")
    void create_sinFecha_usaFechaActual() {
        InscripcionDomainEntity inscripcion =
            InscripcionDomainEntity.create(
                null,
                "actividad-123",
                estadoPendiente(),
                "usuario-456"
            );

        assertEquals(LocalDate.now(), inscripcion.getFechaInscripcion());
    }

    @Test
    @DisplayName("cambiarEstado() debe cambiar estado y actualizar updatedAt")
    void cambiarEstado_ok() {
        InscripcionDomainEntity inscripcion =
            InscripcionDomainEntity.create(
                LocalDate.now(),
                "actividad-123",
                estadoPendiente(),
                "usuario-456"
            );

        LocalDateTime updatedAntes = inscripcion.getUpdatedAt();

        inscripcion.cambiarEstado(estadoConfirmada());

        assertEquals("CONFIRMADA", inscripcion.getEstadoInscripcionDomainEntity().getCodigo());
        assertFalse(inscripcion.getUpdatedAt().isBefore(updatedAntes));
    }

    @Test
    @DisplayName("cambiarEstado() con estado null debe lanzar excepción")
    void cambiarEstado_null_lanzaExcepcion() {
        InscripcionDomainEntity inscripcion =
            InscripcionDomainEntity.create(
                LocalDate.now(),
                "actividad-123",
                estadoPendiente(),
                "usuario-456"
            );

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> inscripcion.cambiarEstado(null)
        );

        assertEquals("El estado no puede ser nulo", ex.getMessage());
    }

    @Test
    @DisplayName("estaConfirmada() debe devolver true solo cuando corresponde")
    void estaConfirmada_funcionaCorrectamente() {
        InscripcionDomainEntity inscripcion =
            InscripcionDomainEntity.create(
                LocalDate.now(),
                "actividad-123",
                estadoConfirmada(),
                "usuario-456"
            );

        assertTrue(inscripcion.estaConfirmada());
        assertFalse(inscripcion.estaPendiente());
        assertFalse(inscripcion.estaCancelada());
    }

    @Test
    @DisplayName("estaCancelada() debe devolver true solo cuando corresponde")
    void estaCancelada_funcionaCorrectamente() {
        InscripcionDomainEntity inscripcion =
            InscripcionDomainEntity.create(
                LocalDate.now(),
                "actividad-123",
                estadoCancelada(),
                "usuario-456"
            );

        assertTrue(inscripcion.estaCancelada());
        assertFalse(inscripcion.estaConfirmada());
        assertFalse(inscripcion.estaPendiente());
    }

    @Test
    @DisplayName("estaPendiente() debe devolver true solo cuando corresponde")
    void estaPendiente_funcionaCorrectamente() {
        InscripcionDomainEntity inscripcion =
            InscripcionDomainEntity.create(
                LocalDate.now(),
                "actividad-123",
                estadoPendiente(),
                "usuario-456"
            );

        assertTrue(inscripcion.estaPendiente());
        assertFalse(inscripcion.estaConfirmada());
        assertFalse(inscripcion.estaCancelada());
    }
}
