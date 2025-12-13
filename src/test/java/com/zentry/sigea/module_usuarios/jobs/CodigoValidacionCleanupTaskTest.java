package com.zentry.sigea.module_usuarios.jobs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_usuarios.core.repositories.ICodigoVerificacionRepository;

@ExtendWith(MockitoExtension.class)
class CodigoValidacionCleanupTaskTest {

    @Mock
    private ICodigoVerificacionRepository codigoVerificacionRepository;

    @InjectMocks
    private CodigoValidacionCleanupTask task;

    @Test
    void limpiarTokensExpirados_invocaRepositorioConInstantActual() {
        // Act
        task.limpiarTokensExpirados();

        // Assert
        ArgumentCaptor<Instant> captor = ArgumentCaptor.forClass(Instant.class);

        verify(codigoVerificacionRepository, times(1))
                .deleteExpiresCodes(captor.capture());

        assertNotNull(captor.getValue());
        assertTrue(
                captor.getValue().isBefore(Instant.now().plusSeconds(1)),
                "El Instant debe ser cercano al now()"
        );
    }
}
