package com.zentry.sigea.module_certificaciones.infrastructure.database.entities;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TipoValidadorEntity - Unit Tests")
class TipoValidadorEntityTest {

    @Test
    @DisplayName("Constructor vacío - Debe iniciar con todos los campos en null")
    void constructorVacio_debeIniciarConNulls() {
        TipoValidadorEntity entity = new TipoValidadorEntity();

        assertAll(
            () -> assertThat(entity.getIdTipoValidador()).isNull(),
            () -> assertThat(entity.getCodigo()).isNull(),
            () -> assertThat(entity.getEtiqueta()).isNull()
        );
    }

    @Test
    @DisplayName("Setters y getters - Debe guardar y devolver valores correctamente")
    void settersYGetters_ok() {
        UUID id = UUID.randomUUID();

        TipoValidadorEntity entity = new TipoValidadorEntity();
        entity.setIdTipoValidador(id);
        entity.setCodigo("QR");
        entity.setEtiqueta("Validación por QR");

        assertAll(
            () -> assertThat(entity.getIdTipoValidador()).isEqualTo(id),
            () -> assertThat(entity.getCodigo()).isEqualTo("QR"),
            () -> assertThat(entity.getEtiqueta()).isEqualTo("Validación por QR")
        );
    }

    @Test
    @DisplayName("Codigo puede ser uno de los valores esperados")
    void codigo_valoresEsperados() {
        TipoValidadorEntity entity = new TipoValidadorEntity();

        entity.setCodigo("HASH");
        assertThat(entity.getCodigo()).isEqualTo("HASH");

        entity.setCodigo("URL_PUBLICA");
        assertThat(entity.getCodigo()).isEqualTo("URL_PUBLICA");

        entity.setCodigo("OCSP");
        assertThat(entity.getCodigo()).isEqualTo("OCSP");
    }

    @Test
    @DisplayName("Permite etiqueta nula o vacía (la validación es de DB)")
    void etiqueta_puedeSerNulaOVacia() {
        TipoValidadorEntity entity = new TipoValidadorEntity();

        entity.setEtiqueta(null);
        assertThat(entity.getEtiqueta()).isNull();

        entity.setEtiqueta("");
        assertThat(entity.getEtiqueta()).isEmpty();
    }
}
