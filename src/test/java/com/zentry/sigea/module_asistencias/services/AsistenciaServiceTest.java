package com.zentry.sigea.module_asistencias.services;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.responseDTO.AsistenciaResponse;
import com.zentry.sigea.module_asistencias.services.usecases.ActualizarAsistenciaUseCase;
import com.zentry.sigea.module_asistencias.services.usecases.ListarAsistenciasUseCase;
import com.zentry.sigea.module_asistencias.services.usecases.ObtenerAsistenciaPorIdUseCase;
import com.zentry.sigea.module_asistencias.services.usecases.RegistrarAsistenciaUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AsistenciaService - Unit Test Completo")
class AsistenciaServiceTest {

    @Mock
    private RegistrarAsistenciaUseCase registrarAsistenciaUseCase;
    @Mock
    private ListarAsistenciasUseCase listarAsistenciasUseCase;
    @Mock
    private ObtenerAsistenciaPorIdUseCase obtenerAsistenciaPorIdUseCase;
    @Mock
    private ActualizarAsistenciaUseCase actualizarAsistenciaUseCase;

    private AsistenciaService asistenciaService;

    private String asistenciaId;
    private String sesionId;
    private String inscripcionId;
    private AsistenciaDomainEntity asistencia;

    @BeforeEach
    void setUp() {
        asistenciaService = new AsistenciaService(
            registrarAsistenciaUseCase,
            listarAsistenciasUseCase,
            obtenerAsistenciaPorIdUseCase,
            actualizarAsistenciaUseCase
        );

        asistenciaId = UUID.randomUUID().toString();
        sesionId = UUID.randomUUID().toString();
        inscripcionId = UUID.randomUUID().toString();

        asistencia = AsistenciaDomainEntity.reconstruct(
            asistenciaId,
            sesionId,
            inscripcionId,
            true,
            LocalDateTime.now()
        );
    }

    // ===================== registrarAsistencia =====================

    @Test
    @DisplayName("registrarAsistencia - OK")
    void registrarAsistencia_ok() {
        RegistrarAsistenciaRequest request =
            new RegistrarAsistenciaRequest(sesionId, inscripcionId, true);

        when(registrarAsistenciaUseCase.execute(request))
            .thenReturn("Asistencia registrada");

        String result = asistenciaService.registrarAsistencia(request);

        assertThat(result).isEqualTo("Asistencia registrada");
        verify(registrarAsistenciaUseCase).execute(request);
    }

    @Test
    @DisplayName("registrarAsistencia - Propaga excepción")
    void registrarAsistencia_error() {
        RegistrarAsistenciaRequest request =
            new RegistrarAsistenciaRequest(sesionId, inscripcionId, true);

        when(registrarAsistenciaUseCase.execute(request))
            .thenThrow(new IllegalArgumentException("Error"));

        assertThatThrownBy(() -> asistenciaService.registrarAsistencia(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Error");
    }

    // ===================== listarAsistenciasPorSesion =====================

    @Test
    void listarAsistenciasPorSesion_ok() {
        when(listarAsistenciasUseCase.executeBySesion(sesionId))
            .thenReturn(List.of(asistencia));

        List<AsistenciaResponse> result =
            asistenciaService.listarAsistenciasPorSesion(sesionId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSesionId()).isEqualTo(sesionId);
    }

    // ===================== listarAsistenciasPorInscripcion =====================

    @Test
    void listarAsistenciasPorInscripcion_ok() {
        when(listarAsistenciasUseCase.executeByInscripcion(inscripcionId))
            .thenReturn(List.of(asistencia));

        List<AsistenciaResponse> result =
            asistenciaService.listarAsistenciasPorInscripcion(inscripcionId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getInscripcionId()).isEqualTo(inscripcionId);
    }

    // ===================== listarPresentesPorSesion =====================

    @Test
    void listarPresentesPorSesion_ok() {
        when(listarAsistenciasUseCase.executeBySesionYEstado(sesionId, true))
            .thenReturn(List.of(asistencia));

        List<AsistenciaResponse> result =
            asistenciaService.listarPresentesPorSesion(sesionId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPresente()).isTrue();
    }

    // ===================== obtenerAsistenciaPorId =====================

    @Test
    void obtenerAsistenciaPorId_ok() {
        when(obtenerAsistenciaPorIdUseCase.execute(asistenciaId))
            .thenReturn(Optional.of(asistencia));

        AsistenciaResponse result =
            asistenciaService.obtenerAsistenciaPorId(asistenciaId);

        assertThat(result.getId()).isEqualTo(asistenciaId);
    }

    @Test
    void obtenerAsistenciaPorId_noExiste() {
        when(obtenerAsistenciaPorIdUseCase.execute(asistenciaId))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> asistenciaService.obtenerAsistenciaPorId(asistenciaId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(asistenciaId);
    }

    // ===================== actualizarEstadoAsistencia =====================

    @Test
    void actualizarEstadoAsistencia_ok() {
        when(actualizarAsistenciaUseCase.execute(asistenciaId, false))
            .thenReturn(
                AsistenciaDomainEntity.reconstruct(
                    asistenciaId, sesionId, inscripcionId, false, LocalDateTime.now()
                )
            );

        AsistenciaResponse result =
            asistenciaService.actualizarEstadoAsistencia(asistenciaId, false);

        assertThat(result.getPresente()).isFalse();
        verify(actualizarAsistenciaUseCase).execute(asistenciaId, false);
    }

    @Test
    void actualizarEstadoAsistencia_error() {
        when(actualizarAsistenciaUseCase.execute(asistenciaId, true))
            .thenThrow(new IllegalArgumentException("No existe"));

        assertThatThrownBy(() ->
            asistenciaService.actualizarEstadoAsistencia(asistenciaId, true)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
