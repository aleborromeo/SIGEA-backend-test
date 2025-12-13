package com.zentry.sigea.module_certificaciones.infrastructure.database.adapters;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.core.entities.*;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.*;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.CertificadoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CertificadoRepositoryAdapter - Unit Tests")
class CertificadoRepositoryAdapterTest {

    @Mock
    private CertificadoRepository jpaRepository;

    private CertificadoRepositoryAdapter adapter;

    @Captor
    private ArgumentCaptor<CertificadoEntity> certificadoCaptor;

    @BeforeEach
    void setUp() {
        adapter = new CertificadoRepositoryAdapter(jpaRepository);
    }

    @Test
    @DisplayName("save() - Debe mapear Domain->Entity, llamar save, y retornar Domain mapeado")
    void save_ok() {
        // Arrange (datos reales)
        UUID asistenciaId = UUID.randomUUID();
        LocalDate fecha = LocalDate.of(2025, 1, 10);
        LocalDateTime now = LocalDateTime.of(2025, 1, 10, 10, 0);

        CertificadoDomainEntity domain = new CertificadoDomainEntity();
        domain.setAsistenciaId(asistenciaId.toString());
        domain.setCodigoValidacion("COD-123");
        domain.setFechaEmision(fecha);
        domain.setUrlPdf("http://pdf");
        domain.setCreatedAt(now);
        domain.setUpdatedAt(now);
        domain.setEstado(EstadoCertificadoDomainEntity.emitido());

        CertificadoEntity saved = new CertificadoEntity();
        saved.setIdCertificado(UUID.randomUUID());
        saved.setAsistenciaId(asistenciaId);
        saved.setCodigoValidacion("COD-123");
        saved.setFechaEmision(fecha);
        saved.setUrlPdf("http://pdf");
        saved.setCreatedAt(now);
        saved.setUpdatedAt(now);

        EstadoCertificadoEntity estadoEntity = new EstadoCertificadoEntity();
        estadoEntity.setCodigo("EMITIDO");
        estadoEntity.setEtiqueta("Emitido");
        saved.setEstado(estadoEntity);

        when(jpaRepository.save(any(CertificadoEntity.class))).thenReturn(saved);

        // Act
        CertificadoDomainEntity result = adapter.save(domain);

        // Assert (verificar mapeo + llamada)
        verify(jpaRepository).save(certificadoCaptor.capture());
        CertificadoEntity sent = certificadoCaptor.getValue();

        assertAll(
            () -> assertThat(sent.getAsistenciaId()).isEqualTo(asistenciaId),
            () -> assertThat(sent.getCodigoValidacion()).isEqualTo("COD-123"),
            () -> assertThat(sent.getFechaEmision()).isEqualTo(fecha),
            () -> assertThat(sent.getUrlPdf()).isEqualTo("http://pdf"),
            () -> assertThat(sent.getCreatedAt()).isEqualTo(now),
            () -> assertThat(sent.getUpdatedAt()).isEqualTo(now)
        );

        assertAll(
            () -> assertThat(result).isNotNull(),
            () -> assertThat(result.getIdCertificado()).isEqualTo(saved.getIdCertificado().toString()),
            () -> assertThat(result.getAsistenciaId()).isEqualTo(asistenciaId.toString()),
            () -> assertThat(result.getCodigoValidacion()).isEqualTo("COD-123"),
            () -> assertThat(result.getEstado().getCodigo()).isEqualTo("EMITIDO")
        );
    }

    @Test
    @DisplayName("findById() - OK con UUID válido")
    void findById_ok() {
        UUID id = UUID.randomUUID();
        CertificadoEntity entity = new CertificadoEntity();
        entity.setIdCertificado(id);
        entity.setAsistenciaId(UUID.randomUUID());
        entity.setCodigoValidacion("AAA");
        entity.setFechaEmision(LocalDate.now());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        EstadoCertificadoEntity estado = new EstadoCertificadoEntity();
        estado.setCodigo("EMITIDO");
        entity.setEstado(estado);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<CertificadoDomainEntity> result = adapter.findById(id.toString());

        assertThat(result).isPresent();
        assertThat(result.get().getIdCertificado()).isEqualTo(id.toString());
        verify(jpaRepository).findById(id);
    }

    @Test
    @DisplayName("findById() - Debe lanzar IllegalArgumentException si UUID inválido")
    void findById_uuidInvalido() {
        assertThatThrownBy(() -> adapter.findById("no-es-uuid"))
            .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(jpaRepository);
    }

    @Test
    @DisplayName("existsByCodigoValidacion() - true si el repo retorna presente, false si vacío")
    void existsByCodigoValidacion_true_false() {
        when(jpaRepository.findByCodigoValidacion("X")).thenReturn(Optional.of(new CertificadoEntity()));
        when(jpaRepository.findByCodigoValidacion("Y")).thenReturn(Optional.empty());

        assertAll(
            () -> assertThat(adapter.existsByCodigoValidacion("X")).isTrue(),
            () -> assertThat(adapter.existsByCodigoValidacion("Y")).isFalse()
        );
    }

    @Test
    @DisplayName("deleteById() - Debe convertir a UUID y delegar")
    void deleteById_ok() {
        UUID id = UUID.randomUUID();

        adapter.deleteById(id.toString());

        verify(jpaRepository).deleteById(id);
    }
}
