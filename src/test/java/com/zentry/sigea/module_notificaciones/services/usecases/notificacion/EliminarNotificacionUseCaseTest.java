package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EliminarNotificacionUseCaseTest {

    @Mock private INotificacionRepository notificacionRepository;
    
    @InjectMocks
    private EliminarNotificacionUseCase eliminarNotificacionUseCase;

    private final String NOTIF_ID = "notif-1";
    private final String USUARIO_ID = "user-10";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(notificacionRepository.existsById(NOTIF_ID)).thenReturn(true);
        doNothing().when(notificacionRepository).deleteById(NOTIF_ID);
    }

    @Test
    void execute_debeEliminarNotificacionSiExiste() {
        // ACT
        eliminarNotificacionUseCase.execute(NOTIF_ID);

        // ASSERT
        verify(notificacionRepository, times(1)).existsById(NOTIF_ID);
        verify(notificacionRepository, times(1)).deleteById(NOTIF_ID);
    }
    
    @Test
    void execute_debeLanzarExcepcionSiNotificacionNoExiste() {
        // ARRANGE
        when(notificacionRepository.existsById(NOTIF_ID)).thenReturn(false);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            eliminarNotificacionUseCase.execute(NOTIF_ID);
        });
        verify(notificacionRepository, never()).deleteById(anyString());
    }

    @Test
    void eliminarPorUsuario_debeLlamarACascadaEnRepositorio() {
        // ACT
        eliminarNotificacionUseCase.eliminarPorUsuario(USUARIO_ID);
        
        // ASSERT
        // Verifica que el repositorio llamó al método de eliminación masiva
        verify(notificacionRepository, times(1)).deleteByUsuarioId(USUARIO_ID);
    }
}