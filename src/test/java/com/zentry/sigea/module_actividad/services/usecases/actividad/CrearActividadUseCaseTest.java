package com.zentry.sigea.module_actividad.services.usecases.actividad;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IActividadRespository;
import com.zentry.sigea.module_actividad.core.repositories.IEstadoActividadRepository;
import com.zentry.sigea.module_actividad.core.repositories.ITipoActividadRepository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.CrearActividadRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearActividadUseCaseTest {

    @Mock IActividadRespository actividadRepository;
    @Mock IEstadoActividadRepository estadoRepo;
    @Mock ITipoActividadRepository tipoRepo;

    @InjectMocks
    CrearActividadUseCase useCase;

    CrearActividadRequest req;
    EstadoActividadDomainEntity estado;
    TipoActividadDomainEntity tipo;

    @BeforeEach
    void setup() {
        req = new CrearActividadRequest(
                "Titulo",
                "Descripcion",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                "EST1",
                "ORG1",
                "TIP1",
                "UPI",
                "CoOrg",
                "SponsorX",
                "banner.jpg",
                "932720078"
        );

        estado = new EstadoActividadDomainEntity();
        estado.setEstadoActividadId("EST1");
        estado.setCodigo("ACT");
        estado.setEtiqueta("Activo");

        tipo = new TipoActividadDomainEntity();
        tipo.setTipoActividadId("TIP1");
        tipo.setNombreActividad("Taller");
    }

    // ---------------- BASIC VALIDATIONS ----------------

    @Test
    void execute_fallaSiTituloEsNulo() {
        req.setTitulo(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> useCase.execute(req)
        );
        assertTrue(ex.getMessage().contains("El título es obligatorio"));
    }

    @Test
    void execute_fallaSiFechaInicioNula() {
        req.setFechaInicio(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> useCase.execute(req)
        );
        assertTrue(ex.getMessage().contains("La fecha de inicio es obligatoria"));
    }

    @Test
    void execute_fallaSiEstadoNoExiste() {
        when(estadoRepo.findById("EST1")).thenReturn(Optional.empty());
        when(tipoRepo.findById("TIP1")).thenReturn(Optional.of(tipo));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> useCase.execute(req)
        );
        assertTrue(ex.getMessage().contains("No se encontró un estado"));
    }

    @Test
    void execute_fallaSiTipoNoExiste() {
        when(estadoRepo.findById("EST1")).thenReturn(Optional.of(estado));
        when(tipoRepo.findById("TIP1")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> useCase.execute(req)
        );
        assertTrue(ex.getMessage().contains("No se encontró un tipo"));
    }

    @Test
    void execute_fallaSiFechaFinAntesDeInicio() {
        req.setFechaFin(req.getFechaInicio().minusDays(1));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> useCase.execute(req)
        );
        assertTrue(ex.getMessage().contains("La fecha de fin no puede ser anterior"));
    }

    @Test
    void execute_fallaSiSeCreaEnFechaPasada() {
        req.setFechaInicio(LocalDate.now().minusDays(1));
        req.setFechaFin(LocalDate.now().plusDays(2));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> useCase.execute(req)
        );
        assertTrue(ex.getMessage().contains("No se pueden crear actividades en fechas pasadas"));
    }

    @Test
    void execute_fallaSiSeSolapanFechas() {

        // El organizador YA tiene una actividad en esas fechas
        ActividadDomainEntity existente = new ActividadDomainEntity();
        existente.setFechaInicio(req.getFechaInicio());
        existente.setFechaFin(req.getFechaFin());

        when(actividadRepository.findByOrganizadorId("ORG1"))
                .thenReturn(List.of(existente));

        when(estadoRepo.findById("EST1")).thenReturn(Optional.of(estado));
        when(tipoRepo.findById("TIP1")).thenReturn(Optional.of(tipo));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> useCase.execute(req)
        );

        assertTrue(ex.getMessage().contains("ya tiene una actividad programada"));
    }

    @Test
    void execute_fallaSiYapeEsInvalido() {
        req.setNumeroYape("12345");

        when(estadoRepo.findById("EST1")).thenReturn(Optional.of(estado));
        when(tipoRepo.findById("TIP1")).thenReturn(Optional.of(tipo));
        when(actividadRepository.findByOrganizadorId("ORG1")).thenReturn(List.of());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> useCase.execute(req)
        );
        assertTrue(ex.getMessage().contains("número de Yape"));
    }

    // ---------------- SUCCESS EXECUTION ----------------

    @Test
    void execute_creaCorrectamente() {
        when(estadoRepo.findById("EST1")).thenReturn(Optional.of(estado));
        when(tipoRepo.findById("TIP1")).thenReturn(Optional.of(tipo));
        when(actividadRepository.findByOrganizadorId("ORG1")).thenReturn(List.of());

        ActividadDomainEntity saved = new ActividadDomainEntity();
        saved.setActividadId("ACT-99");

        when(actividadRepository.save(any())).thenReturn(saved);

        String result = useCase.execute(req);

        assertEquals("ACT-99", result);
        verify(actividadRepository).save(any(ActividadDomainEntity.class));
    }

    @Test
    void execute_fallaCuandoSaveDevuelveNull() {
        when(estadoRepo.findById("EST1")).thenReturn(Optional.of(estado));
        when(tipoRepo.findById("TIP1")).thenReturn(Optional.of(tipo));
        when(actividadRepository.findByOrganizadorId("ORG1")).thenReturn(List.of());

        when(actividadRepository.save(any())).thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class, () -> useCase.execute(req)
        );

        assertTrue(ex.getMessage().contains("Algo salió mal al guardar"));
    }
}
