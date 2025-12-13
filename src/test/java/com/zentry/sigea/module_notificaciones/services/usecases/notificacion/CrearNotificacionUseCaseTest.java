package com.zentry.sigea.module_notificaciones.services.usecases.notificacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_notificaciones.core.entities.EstadoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.entities.TipoNotificacionDomainEntity;
import com.zentry.sigea.module_notificaciones.core.repositories.IEstadoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.INotificacionRepository;
import com.zentry.sigea.module_notificaciones.core.repositories.ITipoNotificacionRepository;
import com.zentry.sigea.module_notificaciones.presentation.models.requestDTO.CrearNotificacionRequest;

@ExtendWith(MockitoExtension.class)
class CrearNotificacionUseCaseTest {

    @Mock
    private INotificacionRepository notificacionRepository;
    @Mock
    private ITipoNotificacionRepository tipoRepository;
    @Mock
    private IEstadoNotificacionRepository estadoRepository;

    @InjectMocks
    private CrearNotificacionUseCase useCase;

    @Test
    void execute_ok() {
        TipoNotificacionDomainEntity tipo = new TipoNotificacionDomainEntity();
        EstadoNotificacionDomainEntity estado = new EstadoNotificacionDomainEntity();

        when(tipoRepository.findByCodigo("INSCRIPCION")).thenReturn(Optional.of(tipo));
        when(estadoRepository.findByCodigo("PENDIENTE")).thenReturn(Optional.of(estado));
        when(notificacionRepository.save(any())).thenReturn(true);

        CrearNotificacionRequest req = new CrearNotificacionRequest(
            "user", null, "INSCRIPCION", "Mensaje", null, "SISTEMA"
        );

        assertNotNull(useCase.execute(req));
    }

    @Test
    void execute_mensajeVacio() {
        CrearNotificacionRequest req = new CrearNotificacionRequest(
            "user", null, "INSCRIPCION", "", null, "SISTEMA"
        );

        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(req));
    }
}
