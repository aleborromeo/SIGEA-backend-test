package com.zentry.sigea.module_certificaciones.infrastructure.database.mappers;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.CertificadoEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.TipoValidadorEntity;
import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.ValidacionEntity;

@DisplayName("ValidacionMapper - Unit Tests")
class ValidacionMapperTest {

    private final ValidacionMapper mapper =
        new ValidacionMapper(new CertificadoMapper(new EstadoCertificadoMapper()),
                             new TipoValidadorMapper());

    @Test
    @DisplayName("toDomain - Convierte correctamente")
    void toDomain_ok() {
        CertificadoEntity cert = new CertificadoEntity();
        cert.setIdCertificado(UUID.randomUUID());

        TipoValidadorEntity tipo = new TipoValidadorEntity();
        tipo.setCodigo("QR");

        ValidacionEntity entity = new ValidacionEntity();
        entity.setCertificado(cert);
        entity.setTipoValidador(tipo);
        entity.setResultado("APROBADO");
        entity.setFechaValidacion(LocalDate.now());

        ValidacionDomainEntity domain = mapper.toDomain(entity);

        assertThat(domain.getTipoValidador()).isEqualTo("QR");
        assertThat(domain.getResultado()).isEqualTo("APROBADO");
    }
}
