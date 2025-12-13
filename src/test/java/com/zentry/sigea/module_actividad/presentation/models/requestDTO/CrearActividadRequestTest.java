package com.zentry.sigea.module_actividad.presentation.models.requestDTO;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CrearActividadRequestTest {

    @Test
    void testEmptyConstructor() {
        CrearActividadRequest request = new CrearActividadRequest();
        assertNotNull(request);
    }

    @Test
    void testFullConstructor() {
        LocalDate inicio = LocalDate.of(2024, 1, 10);
        LocalDate fin = LocalDate.of(2024, 1, 15);
        LocalTime horaIni = LocalTime.of(9, 30);
        LocalTime horaFin = LocalTime.of(18, 0);

        CrearActividadRequest request = new CrearActividadRequest(
                "Titulo X",
                "Descripcion X",
                inicio,
                fin,
                horaIni,
                horaFin,
                "EST01",
                "ORG33",
                "TP01",
                "Auditorio Central",
                "Co Org",
                "Empresa Sponsor",
                "banner123.jpg",
                "900111222"
        );

        assertEquals("Titulo X", request.getTitulo());
        assertEquals("Descripcion X", request.getDescripcion());
        assertEquals(inicio, request.getFechaInicio());
        assertEquals(fin, request.getFechaFin());
        assertEquals(horaIni, request.getHoraInicio());
        assertEquals(horaFin, request.getHoraFin());
        assertEquals("EST01", request.getEstadoId());
        assertEquals("ORG33", request.getOrganizadorId());
        assertEquals("TP01", request.getTipoActividadId());
        assertEquals("Auditorio Central", request.getUbicacion());
        assertEquals("Co Org", request.getCoOrganizador());
        assertEquals("Empresa Sponsor", request.getSponsor());
        assertEquals("banner123.jpg", request.getBannerUrl());
        assertEquals("900111222", request.getNumeroYape());
    }

    @Test
    void testSettersAndGetters() {
        CrearActividadRequest request = new CrearActividadRequest();

        LocalDate inicio = LocalDate.of(2025, 5, 1);
        LocalDate fin = LocalDate.of(2025, 5, 3);
        LocalTime horaIni = LocalTime.of(8, 0);
        LocalTime horaFin = LocalTime.of(17, 0);

        request.setTitulo("T1");
        request.setDescripcion("D1");
        request.setFechaInicio(inicio);
        request.setFechaFin(fin);
        request.setHoraInicio(horaIni);
        request.setHoraFin(horaFin);
        request.setEstadoId("ES1");
        request.setOrganizadorId("ORG1");
        request.setTipoActividadId("TIP1");
        request.setUbicacion("Salon Azul");
        request.setCoOrganizador("CO");
        request.setSponsor("SP");
        request.setBannerUrl("banner.png");
        request.setNumeroYape("999888777");

        assertEquals("T1", request.getTitulo());
        assertEquals("D1", request.getDescripcion());
        assertEquals(inicio, request.getFechaInicio());
        assertEquals(fin, request.getFechaFin());
        assertEquals(horaIni, request.getHoraInicio());
        assertEquals(horaFin, request.getHoraFin());
        assertEquals("ES1", request.getEstadoId());
        assertEquals("ORG1", request.getOrganizadorId());
        assertEquals("TIP1", request.getTipoActividadId());
        assertEquals("Salon Azul", request.getUbicacion());
        assertEquals("CO", request.getCoOrganizador());
        assertEquals("SP", request.getSponsor());
        assertEquals("banner.png", request.getBannerUrl());
        assertEquals("999888777", request.getNumeroYape());
    }

    @Test
    void testNullValuesAllowed() {
        CrearActividadRequest request = new CrearActividadRequest();

        request.setTitulo(null);
        request.setDescripcion(null);
        request.setFechaInicio(null);
        request.setFechaFin(null);
        request.setHoraInicio(null);
        request.setHoraFin(null);
        request.setEstadoId(null);
        request.setOrganizadorId(null);
        request.setTipoActividadId(null);
        request.setUbicacion(null);
        request.setCoOrganizador(null);
        request.setSponsor(null);
        request.setBannerUrl(null);
        request.setNumeroYape(null);

        assertNull(request.getTitulo());
        assertNull(request.getDescripcion());
        assertNull(request.getFechaInicio());
        assertNull(request.getFechaFin());
        assertNull(request.getHoraInicio());
        assertNull(request.getHoraFin());
        assertNull(request.getEstadoId());
        assertNull(request.getOrganizadorId());
        assertNull(request.getTipoActividadId());
        assertNull(request.getUbicacion());
        assertNull(request.getCoOrganizador());
        assertNull(request.getSponsor());
        assertNull(request.getBannerUrl());
        assertNull(request.getNumeroYape());
    }

    @Test
    void testEmptyStrings() {
        CrearActividadRequest request = new CrearActividadRequest();

        request.setTitulo("");
        request.setDescripcion("");
        request.setEstadoId("");
        request.setOrganizadorId("");
        request.setTipoActividadId("");
        request.setUbicacion("");
        request.setCoOrganizador("");
        request.setSponsor("");
        request.setBannerUrl("");
        request.setNumeroYape("");

        assertEquals("", request.getTitulo());
        assertEquals("", request.getDescripcion());
        assertEquals("", request.getEstadoId());
        assertEquals("", request.getOrganizadorId());
        assertEquals("", request.getTipoActividadId());
        assertEquals("", request.getUbicacion());
        assertEquals("", request.getCoOrganizador());
        assertEquals("", request.getSponsor());
        assertEquals("", request.getBannerUrl());
        assertEquals("", request.getNumeroYape());
    }
}
