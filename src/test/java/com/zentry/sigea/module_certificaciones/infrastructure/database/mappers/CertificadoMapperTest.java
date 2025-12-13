package com.zentry.sigea.module_certificaciones.infrastructure.database.mappers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.CertificadoEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.EstadoCertificadoEntity;

@DisplayName("CertificadoMapper - Unit Tests")
class CertificadoMapperTest {

    private final EstadoCertificadoMapper estadoMapper = new EstadoCertificadoMapper();
    private final CertificadoMapper mapper = new CertificadoMapper(estadoMapper);

    @Test
    @DisplayName("toDomain - Mapea todos los campos correctamente")
    void toDomain_ok() {
        UUID asistenciaId = UUID.randomUUID();

        EstadoCertificadoEntity estado = new EstadoCertificadoEntity();
        estado.setCodigo("EMITIDO");
        estado.setEtiqueta("Emitido");

        CertificadoEntity entity = new CertificadoEntity();
        entity.setAsistenciaId(asistenciaId);
        entity.setCodigoValidacion("ABC123");
        entity.setFechaEmision(LocalDate.now());
        entity.setEstado(estado);
        entity.setUrlPdf("http://pdf");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CertificadoDomainEntity domain = mapper.toDomain(entity);

        assertAll(
            () -> assertThat(domain.getAsistenciaId()).isEqualTo(asistenciaId.toString()),
            () -> assertThat(domain.getCodigoValidacion()).isEqualTo("ABC123"),
            () -> assertThat(domain.getEstado().getCodigo()).isEqualTo("EMITIDO")
        );
    }

    @Test
    @DisplayName("toInfrastructure - Mapea correctamente")
    void toInfrastructure_ok() {
        CertificadoDomainEntity domain = new CertificadoDomainEntity();
        domain.setAsistenciaId(UUID.randomUUID().toString());
        domain.setCodigoValidacion("XYZ");
        domain.setFechaEmision(LocalDate.now());
        domain.setEstado(EstadoCertificadoDomainEntity.emitido());

        CertificadoEntity entity = mapper.toInfrastructure(domain);

        assertThat(entity.getCodigoValidacion()).isEqualTo("XYZ");
        assertThat(entity.getEstado().getCodigo()).isEqualTo("EMITIDO");
    }
}
