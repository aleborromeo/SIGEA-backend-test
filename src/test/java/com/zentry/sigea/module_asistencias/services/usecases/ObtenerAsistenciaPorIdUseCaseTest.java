package com.zentry.sigea.module_asistencias.services.usecases;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObtenerAsistenciaPorIdUseCaseTest {

    @Mock
    private IAsistenciaRepository asistenciaRepository;

    @Test
    void obtenerAsistencia_ok() {
        ObtenerAsistenciaPorIdUseCase useCase =
            new ObtenerAsistenciaPorIdUseCase(asistenciaRepository);

        AsistenciaDomainEntity domain = AsistenciaDomainEntity.reconstruct(
            "id", "sesion", "inscripcion", true, LocalDateTime.now()
        );

        when(asistenciaRepository.findById("id"))
            .thenReturn(Optional.of(domain));

        Optional<AsistenciaDomainEntity> result = useCase.execute("id");

        assertThat(result).isPresent();
    }

    @Test
    void obtenerAsistencia_idInvalido() {
        ObtenerAsistenciaPorIdUseCase useCase =
            new ObtenerAsistenciaPorIdUseCase(asistenciaRepository);

        assertThatThrownBy(() -> useCase.execute(""))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
