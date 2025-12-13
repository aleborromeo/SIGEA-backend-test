package com.zentry.sigea.module_certificaciones.infrastructure.database.adapters;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.*;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.TipoValidadorRepository;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.ValidacionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidacionRepositoryAdapter - Unit Tests")
class ValidacionRepositoryAdapterTest {

    @Mock
    private ValidacionRepository jpaRepository;

    @Mock
    private TipoValidadorRepository tipoValidadorRepository;

    private ValidacionRepositoryAdapter adapter;

    @Captor
    private ArgumentCaptor<ValidacionEntity> validacionCaptor;

    @BeforeEach
    void setUp() {
        adapter = new ValidacionRepositoryAdapter(jpaRepository, tipoValidadorRepository);
    }

    @Test
    @DisplayName("save() - OK (tipo validador existe)")
    void save_ok_tipoExiste() {
        ValidacionDomainEntity domain = ValidacionDomainEntity.crearAprobada(
            UUID.randomUUID().toString(),
            "QR",
            "OK"
        );
        domain.setFechaValidacion(LocalDate.of(2025, 2, 1));

        TipoValidadorEntity tipo = new TipoValidadorEntity();
        tipo.setCodigo("QR");
        tipo.setEtiqueta("Validación por QR");

        ValidacionEntity saved = new ValidacionEntity();
        saved.setResultado("APROBADO");
        saved.setDetalle("OK");
        saved.setFechaValidacion(domain.getFechaValidacion());
        saved.setTipoValidador(tipo);

        when(tipoValidadorRepository.findByCodigo("QR")).thenReturn(Optional.of(tipo));
        when(jpaRepository.save(any())).thenReturn(saved);

        ValidacionDomainEntity result = adapter.save(domain);

        verify(jpaRepository).save(validacionCaptor.capture());
        ValidacionEntity sent = validacionCaptor.getValue();

        assertAll(
            () -> assertThat(sent.getTipoValidador()).isNotNull(),
            () -> assertThat(sent.getTipoValidador().getCodigo()).isEqualTo("QR"),
            () -> assertThat(sent.getResultado()).isEqualTo("APROBADO"),
            () -> assertThat(sent.getDetalle()).isEqualTo("OK"),
            () -> assertThat(result.getResultado()).isEqualTo("APROBADO"),
            () -> assertThat(result.getTipoValidador()).isEqualTo("QR")
        );
    }

    @Test
    @DisplayName("save() - tipo validador NO existe -> actualmente se envía null (para que veas el bug)")
    void save_tipoNoExiste_enviaNull() {
        ValidacionDomainEntity domain = ValidacionDomainEntity.crearAprobada(
            UUID.randomUUID().toString(),
            "NOEXISTE",
            "OK"
        );
        domain.setFechaValidacion(LocalDate.now());

        when(tipoValidadorRepository.findByCodigo("NOEXISTE")).thenReturn(Optional.empty());

        // el adapter igual intenta guardar con tipo=null. Simulamos lo que pasaría:
        when(jpaRepository.save(any())).thenThrow(new IllegalArgumentException("tipo_validador no puede ser null"));

        assertThatThrownBy(() -> adapter.save(domain))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("no puede ser null");
    }

    @Test
    @DisplayName("findById() - UUID inválido lanza excepción")
    void findById_uuidInvalido() {
        assertThatThrownBy(() -> adapter.findById("no-es-uuid"))
            .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(jpaRepository);
    }
}
