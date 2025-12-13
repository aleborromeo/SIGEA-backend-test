package com.zentry.sigea.module_actividad.presentation.models.requestDTO;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ActividadRequestTest {

    @Test
    void testEmptyConstructor() {
        ActividadRequest request = new ActividadRequest();
        assertNotNull(request);
    }

    @Test
    void testFullConstructor() {
        EstadoActividadDomainEntity estado = new EstadoActividadDomainEntity();
        TipoActividadDomainEntity tipo = new TipoActividadDomainEntity();

        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 5);

        ActividadRequest request = new ActividadRequest(
                "Titulo X",
                "Desc X",
                inicio,
                fin,
                "CoOrg",
                "Sponsor Y",
                estado,
                "ORG11",
                tipo,
                "Auditorio Central"
        );

        assertEquals("Titulo X", request.getTitulo());
        assertEquals("Desc X", request.getDescripcion());
        assertEquals(inicio, request.getFechaInicio());
        assertEquals(fin, request.getFechaFin());
        assertEquals("CoOrg", request.getCoOrganizador());
        assertEquals("Sponsor Y", request.getSponsor());
        assertEquals(estado, request.getEstado());
        assertEquals("ORG11", request.getOrganizadorId());
        assertEquals(tipo, request.getTipoActividad());
        assertEquals("Auditorio Central", request.getUbicacion());
    }

    @Test
    void testSettersAndGetters() {
        ActividadRequest request = new ActividadRequest();

        EstadoActividadDomainEntity estado = new EstadoActividadDomainEntity();
        TipoActividadDomainEntity tipo = new TipoActividadDomainEntity();

        request.setTitulo("Mi título");
        request.setDescripcion("Descripción");
        request.setFechaInicio(LocalDate.of(2024, 3, 1));
        request.setFechaFin(LocalDate.of(2024, 3, 10));
        request.setCoOrganizador("Co");
        request.setSponsor("Sponsor");
        request.setEstado(estado);
        request.setOrganizadorId("ORG123");
        request.setTipoActividad(tipo);
        request.setUbicacion("Salón 5");

        assertEquals("Mi título", request.getTitulo());
        assertEquals("Descripción", request.getDescripcion());
        assertEquals(LocalDate.of(2024, 3, 1), request.getFechaInicio());
        assertEquals(LocalDate.of(2024, 3, 10), request.getFechaFin());
        assertEquals("Co", request.getCoOrganizador());
        assertEquals("Sponsor", request.getSponsor());
        assertEquals(estado, request.getEstado());
        assertEquals("ORG123", request.getOrganizadorId());
        assertEquals(tipo, request.getTipoActividad());
        assertEquals("Salón 5", request.getUbicacion());
    }

    @Test
    void testNullValuesAllowed() {
        ActividadRequest request = new ActividadRequest();

        request.setTitulo(null);
        request.setDescripcion(null);
        request.setFechaInicio(null);
        request.setFechaFin(null);
        request.setCoOrganizador(null);
        request.setSponsor(null);
        request.setEstado(null);
        request.setOrganizadorId(null);
        request.setTipoActividad(null);
        request.setUbicacion(null);

        assertNull(request.getTitulo());
        assertNull(request.getDescripcion());
        assertNull(request.getFechaInicio());
        assertNull(request.getFechaFin());
        assertNull(request.getCoOrganizador());
        assertNull(request.getSponsor());
        assertNull(request.getEstado());
        assertNull(request.getOrganizadorId());
        assertNull(request.getTipoActividad());
        assertNull(request.getUbicacion());
    }

    @Test
    void testEmptyStrings() {
        ActividadRequest request = new ActividadRequest();

        request.setTitulo("");
        request.setDescripcion("");
        request.setCoOrganizador("");
        request.setSponsor("");
        request.setOrganizadorId("");
        request.setUbicacion("");

        assertEquals("", request.getTitulo());
        assertEquals("", request.getDescripcion());
        assertEquals("", request.getCoOrganizador());
        assertEquals("", request.getSponsor());
        assertEquals("", request.getOrganizadorId());
        assertEquals("", request.getUbicacion());
    }
}
