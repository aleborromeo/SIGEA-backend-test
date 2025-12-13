package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.core.repositories.IAsistenciaRepository;
import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_inscripciones.core.entities.InscripcionDomainEntity;
import com.zentry.sigea.module_inscripciones.core.repositories.IInscripcionRepository;
import com.zentry.sigea.module_notificaciones.events.domain.CertificadoGeneradoEvent;

@ExtendWith(MockitoExtension.class)
@DisplayName("CrearCertificadoUseCase")
class CrearCertificadoUseCaseTest {

    @Mock private ICertificadoRepository certificadoRepository;
    @Mock private IEstadoCertificadoRepository estadoRepository;
    @Mock private IAsistenciaRepository asistenciaRepository;
    @Mock private IInscripcionRepository inscripcionRepository;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CrearCertificadoUseCase useCase;

    private String asistenciaId;
    private CrearCertificadoRequest request;

    @BeforeEach
    void setUp() {
        asistenciaId = UUID.randomUUID().toString();
        request = new CrearCertificadoRequest(asistenciaId);
    }

    @Test
    @DisplayName("✔ Crear certificado correctamente")
    void crearCertificado_ok() {
        when(certificadoRepository.existsByAsistenciaId(asistenciaId)).thenReturn(false);
        when(certificadoRepository.existsByCodigoValidacion(any())).thenReturn(false);
        when(estadoRepository.findByCodigo("EMITIDO"))
            .thenReturn(Optional.of(EstadoCertificadoDomainEntity.emitido()));
        when(certificadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        AsistenciaDomainEntity asistencia = mock(AsistenciaDomainEntity.class);
        when(asistencia.getInscripcionId()).thenReturn("INS-1");
        when(asistenciaRepository.findById(asistenciaId))
            .thenReturn(Optional.of(asistencia));

        InscripcionDomainEntity inscripcion = mock(InscripcionDomainEntity.class);
        when(inscripcion.getUsuarioId()).thenReturn("USR-1");
        when(inscripcion.getActividadId()).thenReturn("ACT-1");
        when(inscripcionRepository.findById("INS-1"))
            .thenReturn(Optional.of(inscripcion));

        CertificadoDomainEntity certificado = useCase.execute(request);

        assertThat(certificado).isNotNull();
        assertThat(certificado.estaEmitido()).isTrue();
        assertThat(certificado.getCodigoValidacion()).startsWith("CERT-");

        verify(eventPublisher, times(1)).publishEvent(any(CertificadoGeneradoEvent.class));
    }

    @Test
    @DisplayName("❌ Error si ya existe certificado")
    void crearCertificado_duplicado() {
        when(certificadoRepository.existsByAsistenciaId(asistenciaId)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Ya existe un certificado");
    }

    @Test
    @DisplayName("❌ Error si estado EMITIDO no existe")
    void crearCertificado_sinEstadoEmitido() {
        when(certificadoRepository.existsByAsistenciaId(asistenciaId)).thenReturn(false);
        when(estadoRepository.findByCodigo("EMITIDO")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(request))
            .isInstanceOf(IllegalStateException.class);
    }
}
