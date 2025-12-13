package com.zentry.sigea.module_asistencias.services.usecases;

import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarAsistenciasUseCaseTest {

    @Mock
    private IAsistenciaRepository asistenciaRepository;

    @Test
    void listarPorSesion_ok() {
        ListarAsistenciasUseCase useCase =
            new ListarAsistenciasUseCase(asistenciaRepository);

        useCase.executeBySesion("sesion");

        verify(asistenciaRepository).findBySesionId("sesion");
    }

    @Test
    void listarPorInscripcion_idInvalido() {
        ListarAsistenciasUseCase useCase =
            new ListarAsistenciasUseCase(asistenciaRepository);

        assertThatThrownBy(() -> useCase.executeByInscripcion(""))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void listarPorSesionYEstado_ok() {
        ListarAsistenciasUseCase useCase =
            new ListarAsistenciasUseCase(asistenciaRepository);

        useCase.executeBySesionYEstado("sesion", true);

        verify(asistenciaRepository)
            .findBySesionIdAndPresente("sesion", true);
    }
}
