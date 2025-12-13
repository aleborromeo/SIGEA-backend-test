package com.zentry.sigea.module_certificaciones.infrastructure.database.adapters;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.EstadoCertificadoEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.repository.EstadoCertificadoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("EstadoCertificadoRepositoryAdapter - Unit Tests")
class EstadoCertificadoRepositoryAdapterTest {

    @Mock
    private EstadoCertificadoRepository jpaRepository;

    private EstadoCertificadoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new EstadoCertificadoRepositoryAdapter(jpaRepository);
    }

    @Test
    @DisplayName("save() - Debe mapear y devolver domain")
    void save_ok() {
        EstadoCertificadoDomainEntity domain = EstadoCertificadoDomainEntity.emitido();

        EstadoCertificadoEntity saved = new EstadoCertificadoEntity();
        saved.setCodigo("EMITIDO");
        saved.setEtiqueta("Emitido");

        when(jpaRepository.save(any())).thenReturn(saved);

        EstadoCertificadoDomainEntity result = adapter.save(domain);

        assertAll(
            () -> assertThat(result.getCodigo()).isEqualTo("EMITIDO"),
            () -> assertThat(result.getEtiqueta()).isEqualTo("Emitido")
        );
        verify(jpaRepository).save(any());
    }

    @Test
    @DisplayName("findByCodigo() - presente y vacío")
    void findByCodigo_present_y_empty() {
        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();
        entity.setCodigo("REVOCADO");
        entity.setEtiqueta("Revocado");

        when(jpaRepository.findByCodigo("REVOCADO")).thenReturn(Optional.of(entity));
        when(jpaRepository.findByCodigo("X")).thenReturn(Optional.empty());

        assertAll(
            () -> assertThat(adapter.findByCodigo("REVOCADO")).isPresent(),
            () -> assertThat(adapter.findByCodigo("X")).isEmpty()
        );
    }

    @Test
    @DisplayName("findById() - UUID inválido lanza excepción")
    void findById_uuidInvalido() {
        assertThatThrownBy(() -> adapter.findById("no-es-uuid"))
            .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(jpaRepository);
    }
}
