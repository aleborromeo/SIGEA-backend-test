package com.zentry.sigea.module_sesiones.services.usecases.sesion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_sesiones.core.entities.SesionDomainEntity;
import com.zentry.sigea.module_sesiones.core.repositories.ISesionRepository;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity.Modalidad;

@ExtendWith(MockitoExtension.class)
class ActualizarSesionUseCaseTest {

    @Mock
    private ISesionRepository sesionRepository;

    @InjectMocks
    private ActualizarSesionUseCase useCase;

    @Test
    void actualizarSesion_ok() {
        SesionDomainEntity existente = SesionDomainEntity.reconstruct(
            "1", "act-1", "Viejo", "Desc",
            LocalDateTime.now().plusDays(2),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            "Ponente",
            Modalidad.PRESENCIAL,
            "Lugar",
            null,
            "1",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        SesionDomainEntity nuevosDatos = SesionDomainEntity.create(
            "act-1", "Nuevo título", "Nueva desc",
            LocalDateTime.now().plusDays(3),
            LocalTime.of(10, 0),
            LocalTime.of(12, 0),
            "Nuevo ponente",
            Modalidad.VIRTUAL,
            "Virtual",
            "https://meet.test",
            "2"
        );

        when(sesionRepository.findById("1")).thenReturn(Optional.of(existente));
        when(sesionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<SesionDomainEntity> result =
            useCase.execute("1", nuevosDatos);

        assertTrue(result.isPresent());
        assertEquals("Nuevo título", result.get().getTitulo());
    }

    @Test
    void actualizarSesion_noExiste() {
        when(sesionRepository.findById("1")).thenReturn(Optional.empty());

        assertTrue(useCase.execute("1",
            mock(SesionDomainEntity.class)).isEmpty());
    }
}
