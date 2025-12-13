package com.zentry.sigea.module_actividad.presentation.models.responseDTO;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActividadResponseTest {

    MockedStatic<EstadoActividadResponse> estadoMock;
    MockedStatic<TipoActividadResponse> tipoMock;

    @BeforeEach
    void setup() {
        estadoMock = mockStatic(EstadoActividadResponse.class);
        tipoMock = mockStatic(TipoActividadResponse.class);
    }

    @AfterEach
    void tearDown() {
        estadoMock.close();
        tipoMock.close();
    }

    @Test
    void testEmptyConstructor() {
        ActividadResponse response = new ActividadResponse();
        assertNotNull(response);
    }

    @Test
    void testFullConstructorAndGetters() {
        LocalDate inicio = LocalDate.of(2024, 1, 10);
        LocalDate fin = LocalDate.of(2024, 1, 12);
        LocalTime hIni = LocalTime.of(9, 0);
        LocalTime hFin = LocalTime.of(17, 0);
        LocalDateTime creacion = LocalDateTime.now();
        LocalDateTime actualizacion = LocalDateTime.now();

        EstadoActividadResponse est = new EstadoActividadResponse("ID1", "ACT", "Activo");
        TipoActividadResponse tipo = new TipoActividadResponse("TP1", "Taller", "Descripción X");

        ActividadResponse response = new ActividadResponse(
                "123",
                "Titulo", "Desc",
                inicio, fin,
                hIni, hFin,
                est,
                "ORG778",
                tipo,
                "Auditorio",
                "Co Org",
                "Sponsor SA",
                "banner.jpg",
                "900111222",
                creacion,
                actualizacion,
                true, false, false,
                3L
        );

        assertEquals("123", response.getId());
        assertEquals("Titulo", response.getTitulo());
        assertEquals("Desc", response.getDescripcion());
        assertEquals(inicio, response.getFechaInicio());
        assertEquals(fin, response.getFechaFin());
        assertEquals(hIni, response.getHoraInicio());
        assertEquals(hFin, response.getHoraFin());
        assertEquals(est, response.getEstado());
        assertEquals("ORG778", response.getOrganizadorId());
        assertEquals(tipo, response.getTipoActividad());
        assertEquals("Auditorio", response.getUbicacion());
        assertEquals("Co Org", response.getCoOrganizador());
        assertEquals("Sponsor SA", response.getSponsor());
        assertEquals("banner.jpg", response.getBannerUrl());
        assertEquals("900111222", response.getNumeroYape());
        assertEquals(creacion, response.getFechaCreacion());
        assertEquals(actualizacion, response.getFechaActualizacion());
        assertTrue(response.isActiva());
        assertFalse(response.isFinalizada());
        assertFalse(response.isPendiente());
        assertEquals(3L, response.getDuracionEnDias());
    }

    @Test
    void testFactoryMethodFromEntity() {
        ActividadDomainEntity domain = new ActividadDomainEntity();
        domain.setActividadId("55");
        domain.setTitulo("Evento X");
        domain.setDescripcion("Desc X");
        domain.setFechaInicio(LocalDate.of(2024, 6, 1));
        domain.setFechaFin(LocalDate.of(2024, 6, 3));
        domain.setHoraInicio(LocalTime.of(8, 30));
        domain.setHoraFin(LocalTime.of(17, 0));
        domain.setOrganizadorId("ORGZZ");
        domain.setLugar("Auditorio 3");
        domain.setCoOrganizador("CO");
        domain.setSponsor("SP");
        domain.setBannerUrl("B1");
        domain.setNumeroYape("YAPE1");
        domain.setCreatedAt(LocalDateTime.of(2024, 5, 10, 12, 0));
        domain.setUpdatedAt(LocalDateTime.of(2024, 5, 10, 20, 0));

        EstadoActividadDomainEntity estadoDom = new EstadoActividadDomainEntity();
        TipoActividadDomainEntity tipoDom = new TipoActividadDomainEntity();

        domain.setEstadoActividadDomainEntity(estadoDom);
        domain.setTipoActividadDomainEntity(tipoDom);

        EstadoActividadResponse estResponse =
                new EstadoActividadResponse("E1", "A", "Activo");
        TipoActividadResponse tipoResponse =
                new TipoActividadResponse("T1", "Tipo", "Descripción");

        estadoMock.when(() -> EstadoActividadResponse.fromEntity(estadoDom))
                .thenReturn(estResponse);

        tipoMock.when(() -> TipoActividadResponse.fromEntity(tipoDom))
                .thenReturn(tipoResponse);

        ActividadDomainEntity spyDomain = spy(domain);
        when(spyDomain.isActive()).thenReturn(true);
        when(spyDomain.isFinished()).thenReturn(false);
        when(spyDomain.isPending()).thenReturn(false);
        when(spyDomain.getDurationInDays()).thenReturn(3L);

        ActividadResponse response = ActividadResponse.fromEntity(spyDomain);

        assertEquals("55", response.getId());
        assertEquals("Evento X", response.getTitulo());
        assertEquals("Desc X", response.getDescripcion());
        assertEquals(LocalDate.of(2024, 6, 1), response.getFechaInicio());
        assertEquals(LocalDate.of(2024, 6, 3), response.getFechaFin());
        assertEquals(LocalTime.of(8, 30), response.getHoraInicio());
        assertEquals(LocalTime.of(17, 0), response.getHoraFin());
        assertEquals(estResponse, response.getEstado());
        assertEquals("ORGZZ", response.getOrganizadorId());
        assertEquals(tipoResponse, response.getTipoActividad());
        assertEquals("Auditorio 3", response.getUbicacion());
        assertEquals("CO", response.getCoOrganizador());
        assertEquals("SP", response.getSponsor());
        assertEquals("B1", response.getBannerUrl());
        assertEquals("YAPE1", response.getNumeroYape());
        assertEquals(LocalDateTime.of(2024, 5, 10, 12, 0), response.getFechaCreacion());
        assertEquals(LocalDateTime.of(2024, 5, 10, 20, 0), response.getFechaActualizacion());
        assertTrue(response.isActiva());
        assertFalse(response.isFinalizada());
        assertFalse(response.isPendiente());
        assertEquals(3L, response.getDuracionEnDias());
    }
}
