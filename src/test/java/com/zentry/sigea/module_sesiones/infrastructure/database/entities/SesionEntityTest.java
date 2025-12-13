package com.zentry.sigea.module_sesiones.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;

class SesionEntityTest {

    @Test
    void crearSesionEntity_ok() {
        // Arrange
        SesionEntity sesion = new SesionEntity();

        ActividadEntity actividad = new ActividadEntity();
        actividad.setId(UUID.randomUUID());

        LocalDateTime fechaSesion = LocalDateTime.now().plusDays(1);

        // Act
        sesion.setId(UUID.randomUUID());
        sesion.setActividad(actividad);
        sesion.setTitulo("Sesión de Prueba");
        sesion.setDescripcion("Descripción de la sesión");
        sesion.setFechaSesion(fechaSesion);
        sesion.setHoraInicio(LocalTime.of(9, 0));
        sesion.setHoraFin(LocalTime.of(11, 0));
        sesion.setPonente("Juan Pérez");
        sesion.setModalidad(SesionEntity.Modalidad.PRESENCIAL);
        sesion.setLugarSesion("Auditorio Principal");
        sesion.setLinkVirtual(null);
        sesion.setOrden("1");

        // Simular callback JPA
        sesion.onCreate();

        // Assert
        assertNotNull(sesion.getId());
        assertEquals("Sesión de Prueba", sesion.getTitulo());
        assertEquals("Descripción de la sesión", sesion.getDescripcion());
        assertEquals(fechaSesion, sesion.getFechaSesion());
        assertEquals(LocalTime.of(9, 0), sesion.getHoraInicio());
        assertEquals(LocalTime.of(11, 0), sesion.getHoraFin());
        assertEquals("Juan Pérez", sesion.getPonente());
        assertEquals(SesionEntity.Modalidad.PRESENCIAL, sesion.getModalidad());
        assertEquals("Auditorio Principal", sesion.getLugarSesion());
        assertEquals("1", sesion.getOrden());
        assertNotNull(sesion.getCreatedAt());
        assertNotNull(sesion.getUpdatedAt());
    }

    @Test
    void onUpdate_actualizaUpdatedAt() throws InterruptedException {
        // Arrange
        SesionEntity sesion = new SesionEntity();

        sesion.onCreate();
        LocalDateTime createdAt = sesion.getCreatedAt();
        LocalDateTime updatedAtInicial = sesion.getUpdatedAt();

        // Esperar para asegurar diferencia de tiempo
        Thread.sleep(5);

        // Act
        sesion.onUpdate();

        // Assert
        assertEquals(createdAt, sesion.getCreatedAt());
        assertTrue(sesion.getUpdatedAt().isAfter(updatedAtInicial));
    }

    @Test
    void enumModalidad_valoresCorrectos() {
        assertEquals("PRESENCIAL", SesionEntity.Modalidad.PRESENCIAL.name());
        assertEquals("VIRTUAL", SesionEntity.Modalidad.VIRTUAL.name());
        assertEquals("HIBRIDA", SesionEntity.Modalidad.HIBRIDA.name());
    }
}
