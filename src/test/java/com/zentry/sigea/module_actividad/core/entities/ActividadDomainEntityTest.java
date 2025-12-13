package com.zentry.sigea.module_actividad.core.entities;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class ActividadDomainEntityTest {

    @Test
    void testSettersAndGetters() {
        ActividadDomainEntity entity = new ActividadDomainEntity();

        entity.setActividadId("A1");
        entity.setTitulo("Titulo");
        entity.setDescripcion("Descripcion");
        entity.setFechaInicio(LocalDate.of(2024, 1, 1));
        entity.setFechaFin(LocalDate.of(2024, 1, 10));
        entity.setHoraInicio(LocalTime.of(9, 0));
        entity.setHoraFin(LocalTime.of(11, 0));
        entity.setLugar("Lugar");
        entity.setCoOrganizador("Co");
        entity.setSponsor("Sponsor");
        entity.setBannerUrl("banner.png");
        entity.setNumeroYape("900111222");
        entity.setOrganizadorId("ORG1");

        EstadoActividadDomainEntity estado = new EstadoActividadDomainEntity();
        TipoActividadDomainEntity tipo = new TipoActividadDomainEntity();

        entity.setEstadoActividadDomainEntity(estado);
        entity.setTipoActividadDomainEntity(tipo);

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertEquals("A1", entity.getActividadId());
        assertEquals("Titulo", entity.getTitulo());
        assertEquals("Descripcion", entity.getDescripcion());
        assertEquals(LocalDate.of(2024, 1, 1), entity.getFechaInicio());
        assertEquals(LocalDate.of(2024, 1, 10), entity.getFechaFin());
        assertEquals(LocalTime.of(9, 0), entity.getHoraInicio());
        assertEquals(LocalTime.of(11, 0), entity.getHoraFin());
        assertEquals("Lugar", entity.getLugar());
        assertEquals("Co", entity.getCoOrganizador());
        assertEquals("Sponsor", entity.getSponsor());
        assertEquals("banner.png", entity.getBannerUrl());
        assertEquals("900111222", entity.getNumeroYape());
        assertEquals("ORG1", entity.getOrganizadorId());
        assertEquals(estado, entity.getEstadoActividadDomainEntity());
        assertEquals(tipo, entity.getTipoActividadDomainEntity());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testCreateValid() {
        EstadoActividadDomainEntity estado = new EstadoActividadDomainEntity();
        TipoActividadDomainEntity tipo = new TipoActividadDomainEntity();

        ActividadDomainEntity entity = ActividadDomainEntity.create(
                "Titulo",
                "Desc",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 5),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                estado,
                "ORG99",
                tipo,
                "Auditorio",
                "CoOrg",
                "SponsorX",
                "banner.jpg",
                "900000000"
        );

        assertEquals("Titulo", entity.getTitulo());
        assertEquals("Desc", entity.getDescripcion());
        assertEquals(LocalDate.of(2024, 1, 1), entity.getFechaInicio());
        assertEquals(LocalDate.of(2024, 1, 5), entity.getFechaFin());
        assertEquals(LocalTime.of(10, 0), entity.getHoraInicio());
        assertEquals(LocalTime.of(11, 0), entity.getHoraFin());
        assertEquals("ORG99", entity.getOrganizadorId());
        assertEquals("CoOrg", entity.getCoOrganizador());
        assertEquals("SponsorX", entity.getSponsor());
        assertEquals("banner.jpg", entity.getBannerUrl());
        assertEquals("900000000", entity.getNumeroYape());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testCreateHandlesNullOptionalFields() {
        ActividadDomainEntity entity = ActividadDomainEntity.create(
                null, null,
                LocalDate.now(), LocalDate.now(),
                null, null,
                new EstadoActividadDomainEntity(),
                null,
                new TipoActividadDomainEntity(),
                null, null, null, null, null
        );

        assertNull(entity.getTitulo());
        assertNull(entity.getDescripcion());
        assertNull(entity.getHoraInicio());
        assertNull(entity.getHoraFin());
        assertNull(entity.getOrganizadorId());
        assertNull(entity.getLugar());
    }

    @Test
    void testUpdateInfoUpdatesCorrectly() {
        ActividadDomainEntity entity = new ActividadDomainEntity();

        entity.updateInfo(
                "Nuevo titulo",
                "Nueva desc",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 10),
                "Nuevo lugar"
        );

        assertEquals("Nuevo titulo", entity.getTitulo());
        assertEquals("Nueva desc", entity.getDescripcion());
        assertEquals(LocalDate.of(2024, 2, 1), entity.getFechaInicio());
        assertEquals(LocalDate.of(2024, 2, 10), entity.getFechaFin());
        assertEquals("Nuevo lugar", entity.getLugar());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testChangeStatusValid() {
        ActividadDomainEntity entity = new ActividadDomainEntity();
        EstadoActividadDomainEntity estado = new EstadoActividadDomainEntity();

        entity.changeStatus(estado);

        assertEquals(estado, entity.getEstadoActividadDomainEntity());
        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testChangeStatusThrowsWhenNull() {
        ActividadDomainEntity entity = new ActividadDomainEntity();
        assertThrows(IllegalArgumentException.class, () -> entity.changeStatus(null));
    }

    @Test
    void testIsActive() {
        ActividadDomainEntity entity = new ActividadDomainEntity();
        entity.setFechaInicio(LocalDate.now().minusDays(1));
        entity.setFechaFin(LocalDate.now().plusDays(1));

        assertTrue(entity.isActive());
    }

    @Test
    void testIsActiveWithNullDates() {
        ActividadDomainEntity entity = new ActividadDomainEntity();
        assertThrows(NullPointerException.class, entity::isActive);
    }

    @Test
    void testIsFinished() {
        ActividadDomainEntity entity = new ActividadDomainEntity();
        entity.setFechaFin(LocalDate.now().minusDays(1));

        assertTrue(entity.isFinished());
    }

    @Test
    void testIsPending() {
        ActividadDomainEntity entity = new ActividadDomainEntity();
        entity.setFechaInicio(LocalDate.now().plusDays(3));

        assertTrue(entity.isPending());
    }

    @Test
    void testGetDurationInDays() {
        ActividadDomainEntity entity = new ActividadDomainEntity();
        entity.setFechaInicio(LocalDate.of(2024, 1, 1));
        entity.setFechaFin(LocalDate.of(2024, 1, 5));

        assertEquals(5, entity.getDurationInDays());
    }

    @Test
    @Disabled
    void testGetDurationInDaysInvalidDates() {
        ActividadDomainEntity entity = new ActividadDomainEntity();
        entity.setFechaInicio(LocalDate.of(2024, 1, 10));
        entity.setFechaFin(LocalDate.of(2024, 1, 5));

        assertThrows(IllegalArgumentException.class, entity::getDurationInDays);
    }
}
