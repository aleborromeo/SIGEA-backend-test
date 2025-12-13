package com.zentry.sigea.module_certificaciones.infrastructure.database.entities;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EstadoCertificadoEntity - Unit Tests")
class EstadoCertificadoEntityTest {

    @Test
    @DisplayName("Constructor vacío - Debe iniciar con campos en null")
    void constructorVacio_debeIniciarConNulls() {
        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();

        assertAll(
            () -> assertThat(entity.getIdEstadoCertificado()).isNull(),
            () -> assertThat(entity.getCodigo()).isNull(),
            () -> assertThat(entity.getEtiqueta()).isNull()
        );
    }

    @Test
    @DisplayName("Setters y getters - Debe asignar y devolver valores correctamente")
    void settersYGetters_ok() {
        UUID id = UUID.randomUUID();

        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();
        entity.setIdEstadoCertificado(id);
        entity.setCodigo("EMITIDO");
        entity.setEtiqueta("Emitido");

        assertAll(
            () -> assertThat(entity.getIdEstadoCertificado()).isEqualTo(id),
            () -> assertThat(entity.getCodigo()).isEqualTo("EMITIDO"),
            () -> assertThat(entity.getEtiqueta()).isEqualTo("Emitido")
        );
    }

    @Test
    @DisplayName("Codigo admite valores de dominio esperados")
    void codigo_valoresEsperados() {
        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();

        entity.setCodigo("REVOCADO");
        assertThat(entity.getCodigo()).isEqualTo("REVOCADO");

        entity.setCodigo("SUSPENDIDO");
        assertThat(entity.getCodigo()).isEqualTo("SUSPENDIDO");
    }

    @Test
    @DisplayName("Etiqueta puede ser null o vacía")
    void etiqueta_puedeSerNulaOVacia() {
        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();

        entity.setEtiqueta(null);
        assertThat(entity.getEtiqueta()).isNull();

        entity.setEtiqueta("");
        assertThat(entity.getEtiqueta()).isEmpty();
    }
}
