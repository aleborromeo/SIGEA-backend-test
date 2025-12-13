package com.zentry.sigea.module_certificaciones.infrastructure.database.mappers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.EstadoCertificadoEntity;

@DisplayName("EstadoCertificadoMapper - Unit Tests")
class EstadoCertificadoMapperTest {

    private final EstadoCertificadoMapper mapper = new EstadoCertificadoMapper();

    @Test
    @DisplayName("toDomain - Convierte correctamente de Entity a Domain")
    void toDomain_ok() {
        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();
        entity.setCodigo("EMITIDO");
        entity.setEtiqueta("Emitido");

        EstadoCertificadoDomainEntity domain = mapper.toDomain(entity);

        assertAll(
            () -> assertThat(domain).isNotNull(),
            () -> assertThat(domain.getCodigo()).isEqualTo("EMITIDO"),
            () -> assertThat(domain.getEtiqueta()).isEqualTo("Emitido")
        );
    }

    @Test
    @DisplayName("toDomain - Retorna null si entity es null")
    void toDomain_null() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    @DisplayName("toInfrastructure - Convierte correctamente de Domain a Entity")
    void toInfrastructure_ok() {
        EstadoCertificadoDomainEntity domain =
            new EstadoCertificadoDomainEntity("REVOCADO", "Revocado");

        EstadoCertificadoEntity entity = mapper.toInfrastructure(domain);

        assertAll(
            () -> assertThat(entity).isNotNull(),
            () -> assertThat(entity.getCodigo()).isEqualTo("REVOCADO"),
            () -> assertThat(entity.getEtiqueta()).isEqualTo("Revocado")
        );
    }

    @Test
    @DisplayName("updateInfrastructure - Actualiza correctamente campos")
    void updateInfrastructure_ok() {
        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();
        EstadoCertificadoDomainEntity domain =
            new EstadoCertificadoDomainEntity("SUSPENDIDO", "Suspendido");

        mapper.updateInfrastructure(entity, domain);

        assertAll(
            () -> assertThat(entity.getCodigo()).isEqualTo("SUSPENDIDO"),
            () -> assertThat(entity.getEtiqueta()).isEqualTo("Suspendido")
        );
    }

    @Test
    @DisplayName("updateInfrastructure - No hace nada si algún parámetro es null")
    void updateInfrastructure_nullSafe() {
        EstadoCertificadoEntity entity = new EstadoCertificadoEntity();
        mapper.updateInfrastructure(entity, null);
        mapper.updateInfrastructure(null, new EstadoCertificadoDomainEntity());

        assertThat(entity.getCodigo()).isNull();
    }
}
