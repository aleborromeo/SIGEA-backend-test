package com.zentry.sigea.module_asistencias.services.usecases;

import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_sesiones.core.repositories.ISesionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarAsistenciaUseCaseTest {

    @Mock
    private IAsistenciaRepository asistenciaRepository;
    @Mock
    private ISesionRepository sesionRepository;
    @Mock
    private IInscripcionRepository inscripcionRepository;

    private RegistrarAsistenciaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarAsistenciaUseCase(
            asistenciaRepository,
            sesionRepository,
            inscripcionRepository
        );
    }

    @Test
    @DisplayName("Debe registrar asistencia correctamente")
    void registrarAsistencia_ok() {
        String sesionId = UUID.randomUUID().toString();
        String inscripcionId = UUID.randomUUID().toString();

        RegistrarAsistenciaRequest request =
            new RegistrarAsistenciaRequest(sesionId, inscripcionId, true);

        when(sesionRepository.existsById(sesionId)).thenReturn(true);
        when(inscripcionRepository.existsById(inscripcionId)).thenReturn(true);

        String result = useCase.execute(request);

        assertThat(result).contains("PRESENTE");
        verify(asistenciaRepository).save(any());
    }

    @Test
    @DisplayName("Debe fallar si sesión no existe")
    void registrarAsistencia_sesionNoExiste() {
        RegistrarAsistenciaRequest request =
            new RegistrarAsistenciaRequest("sesion", "inscripcion", true);

        when(sesionRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("sesión");
    }

    @Test
    @DisplayName("Debe fallar si inscripción no existe")
    void registrarAsistencia_inscripcionNoExiste() {
        RegistrarAsistenciaRequest request =
            new RegistrarAsistenciaRequest("sesion", "inscripcion", true);

        when(sesionRepository.existsById(any())).thenReturn(true);
        when(inscripcionRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("inscripción");
    }
}
