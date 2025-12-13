package com.zentry.sigea.module_sesiones.services.usecases.sesion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_sesiones.core.repositories.ISesionRepository;

@ExtendWith(MockitoExtension.class)
class EliminarSesionUseCaseTest {

    @Mock
    private ISesionRepository sesionRepository;

    @InjectMocks
    private EliminarSesionUseCase useCase;

    @Test
    void eliminarSesion_ok() {
        when(sesionRepository.existsById("1")).thenReturn(true);

        useCase.execute("1");

        verify(sesionRepository).deleteById("1");
    }

    @Test
    void eliminarSesion_noExiste_lanzaExcepcion() {
        when(sesionRepository.existsById("1")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute("1"));
    }
}
