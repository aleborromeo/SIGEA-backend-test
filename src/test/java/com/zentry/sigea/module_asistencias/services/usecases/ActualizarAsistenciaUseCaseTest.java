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
class ActualizarAsistenciaUseCaseTest {

    @Mock
    private IAsistenciaRepository asistenciaRepository;

    @Test
    void actualizarPresente_ok() {
        ActualizarAsistenciaUseCase useCase =
            new ActualizarAsistenciaUseCase(asistenciaRepository);

        AsistenciaDomainEntity domain = AsistenciaDomainEntity.reconstruct(
            "id", "sesion", "inscripcion", false, LocalDateTime.now()
        );

        when(asistenciaRepository.findById("id"))
            .thenReturn(Optional.of(domain));

        AsistenciaDomainEntity result = useCase.execute("id", true);

        assertThat(result.estaPresente()).isTrue();
        verify(asistenciaRepository).save(domain);
    }

    @Test
    void actualizar_noExiste() {
        ActualizarAsistenciaUseCase useCase =
            new ActualizarAsistenciaUseCase(asistenciaRepository);

        when(asistenciaRepository.findById("id"))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("id", true))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
