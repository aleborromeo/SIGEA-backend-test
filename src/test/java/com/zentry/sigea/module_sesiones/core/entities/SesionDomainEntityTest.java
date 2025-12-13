package com.zentry.sigea.module_sesiones.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity.Modalidad;

class SesionDomainEntityTest {

    @Test
    void create_ok() {
        SesionDomainEntity sesion = SesionDomainEntity.create(
            "actividad-id",
            "Sesión 1",
            "Descripción",
            LocalDateTime.now().plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Ponente",
            Modalidad.PRESENCIAL,
            "Auditorio",
            null,
            "1"
        );

        assertNotNull(sesion);
        assertEquals("Sesión 1", sesion.getTitulo());
        assertNotNull(sesion.getCreatedAt());
        assertNotNull(sesion.getUpdatedAt());
    }

    @Test
    void create_sinActividadId_lanzaError() {
        assertThrows(IllegalArgumentException.class, () ->
            SesionDomainEntity.create(
                null,
                "Sesión",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Ponente",
                Modalidad.PRESENCIAL,
                "Lugar",
                null,
                "1"
            )
        );
    }

    @Test
    void reconstruct_ok() {
        LocalDateTime now = LocalDateTime.now();

        SesionDomainEntity sesion = SesionDomainEntity.reconstruct(
            "id",
            "actividad-id",
            "Sesión",
            "Desc",
            now.plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Ponente",
            Modalidad.VIRTUAL,
            null,
            "https://meet.com",
            "1",
            now,
            now
        );

        assertEquals("id", sesion.getId());
        assertEquals(Modalidad.VIRTUAL, sesion.getModalidad());
    }

    @Test
    void updateInfo_ok() {
        SesionDomainEntity sesion = SesionDomainEntity.create(
            "actividad-id",
            "Sesión",
            "Desc",
            LocalDateTime.now().plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Ponente",
            Modalidad.PRESENCIAL,
            "Lugar",
            null,
            "1"
        );

        sesion.updateInfo(
            "actividad-id",
            "Sesión actualizada",
            "Nueva desc",
            LocalDateTime.now().plusDays(2),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            "Nuevo ponente",
            Modalidad.VIRTUAL,
            null,
            "https://meet.com",
            "2"
        );

        assertEquals("Sesión actualizada", sesion.getTitulo());
        assertEquals(Modalidad.VIRTUAL, sesion.getModalidad());
    }

    @Test
    void reprogramar_fechaPasada_lanzaError() {
        SesionDomainEntity sesion = SesionDomainEntity.create(
            "actividad-id",
            "Sesión",
            "Desc",
            LocalDateTime.now().plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Ponente",
            Modalidad.PRESENCIAL,
            "Lugar",
            null,
            "1"
        );

        assertThrows(IllegalArgumentException.class, () ->
            sesion.reprogramar(LocalDateTime.now().minusDays(1))
        );
    }

    @Test
    void estaEnProgreso_false() {
        SesionDomainEntity sesion = SesionDomainEntity.create(
            "actividad-id",
            "Sesión",
            "Desc",
            LocalDateTime.now().plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Ponente",
            Modalidad.PRESENCIAL,
            "Lugar",
            null,
            "1"
        );

        assertFalse(sesion.estaEnProgreso());
    }

    @Test
    void esProxima_true() {
        SesionDomainEntity sesion = SesionDomainEntity.create(
            "actividad-id",
            "Sesión",
            "Desc",
            LocalDateTime.now().plusHours(2),
            LocalTime.now().plusMinutes(30),
            LocalTime.now().plusHours(1),
            "Ponente",
            Modalidad.PRESENCIAL,
            "Lugar",
            null,
            "1"
        );

        assertTrue(sesion.esProxima());
    }
}
