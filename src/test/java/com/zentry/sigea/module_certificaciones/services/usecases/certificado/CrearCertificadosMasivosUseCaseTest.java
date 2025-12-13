package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;

@ExtendWith(MockitoExtension.class)
class CrearCertificadosMasivosUseCaseTest {

    @Mock
    private ICertificadoRepository certificadoRepository;

    @Mock
    private IEstadoCertificadoRepository estadoCertificadoRepository;

    @Mock
    private IAsistenciaRepository asistenciaRepository;

    @Mock
    private IInscripcionRepository inscripcionRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CrearCertificadosMasivosUseCase useCase;

    @Test
    void crearCertificadosMasivos_ok() {
        when(certificadoRepository.existsByAsistenciaId("A1")).thenReturn(false);
        when(certificadoRepository.existsByCodigoValidacion(any())).thenReturn(false);
        when(estadoCertificadoRepository.findByCodigo("EMITIDO"))
            .thenReturn(Optional.of(EstadoCertificadoDomainEntity.emitido()));
        when(certificadoRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

        AsistenciaDomainEntity asistencia = mock(AsistenciaDomainEntity.class);
        when(asistencia.getInscripcionId()).thenReturn("INS-1");
        when(asistenciaRepository.findById("A1"))
            .thenReturn(Optional.of(asistencia));

        InscripcionDomainEntity inscripcion = mock(InscripcionDomainEntity.class);
        when(inscripcion.getUsuarioId()).thenReturn("U1");
        when(inscripcion.getActividadId()).thenReturn("ACT1");
        when(inscripcionRepository.findById("INS-1"))
            .thenReturn(Optional.of(inscripcion));

        Map<String, Boolean> result =
            useCase.execute(List.of("A1"));

        assertTrue(result.get("A1"));
        verify(eventPublisher, atLeastOnce()).publishEvent(any());
    }

    @Test
    void crearCertificadosMasivos_certificadoYaExiste() {
        when(certificadoRepository.existsByAsistenciaId("A1"))
            .thenReturn(true);

        Map<String, Boolean> result =
            useCase.execute(List.of("A1"));

        assertTrue(result.get("A1"));
        verify(certificadoRepository, never()).save(any());
    }

    @Test
    void crearCertificadosMasivos_errorAlGuardar() {
        when(certificadoRepository.existsByAsistenciaId("A1")).thenReturn(false);
        when(estadoCertificadoRepository.findByCodigo("EMITIDO"))
            .thenReturn(Optional.of(EstadoCertificadoDomainEntity.emitido()));
        when(certificadoRepository.existsByCodigoValidacion(any())).thenReturn(false);
        when(certificadoRepository.save(any()))
            .thenThrow(new RuntimeException("DB error"));

        Map<String, Boolean> result =
            useCase.execute(List.of("A1"));

        assertFalse(result.get("A1"));
    }
}
