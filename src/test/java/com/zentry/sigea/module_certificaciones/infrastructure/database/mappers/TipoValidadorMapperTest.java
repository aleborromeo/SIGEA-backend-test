package com.zentry.sigea.module_certificaciones.infrastructure.database.mappers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_certificaciones.core.entities.TipoValidadorDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.TipoValidadorEntity;

@DisplayName("TipoValidadorMapper - Unit Tests")
class TipoValidadorMapperTest {

    private final TipoValidadorMapper mapper = new TipoValidadorMapper();

    @Test
    @DisplayName("toDomain - Convierte correctamente")
    void toDomain_ok() {
        TipoValidadorEntity entity = new TipoValidadorEntity();
        entity.setCodigo("QR");
        entity.setEtiqueta("Código QR");

        TipoValidadorDomainEntity domain = mapper.toDomain(entity);

        assertAll(
            () -> assertThat(domain).isNotNull(),
            () -> assertThat(domain.getCodigo()).isEqualTo("QR"),
            () -> assertThat(domain.getEtiqueta()).isEqualTo("Código QR")
        );
    }

    @Test
    @DisplayName("toInfrastructure - Convierte correctamente")
    void toInfrastructure_ok() {
        TipoValidadorDomainEntity domain =
            new TipoValidadorDomainEntity("HASH", "Hash criptográfico");

        TipoValidadorEntity entity = mapper.toInfrastructure(domain);

        assertAll(
            () -> assertThat(entity).isNotNull(),
            () -> assertThat(entity.getCodigo()).isEqualTo("HASH"),
            () -> assertThat(entity.getEtiqueta()).isEqualTo("Hash criptográfico")
        );
    }

    @Test
    @DisplayName("Null safety")
    void nullSafety() {
        assertThat(mapper.toDomain(null)).isNull();
        assertThat(mapper.toInfrastructure(null)).isNull();
    }
}
