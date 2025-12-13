package com.zentry.sigea.module_certificaciones.core.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CertificadoDomainEntityTest {

    @Test
    @DisplayName("create - OK")
    void create_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "asistencia-1",
                "COD-123",
                EstadoCertificadoDomainEntity.emitido()
            );

        assertThat(certificado.getAsistenciaId()).isEqualTo("asistencia-1");
        assertThat(certificado.getCodigoValidacion()).isEqualTo("COD-123");
        assertThat(certificado.estaEmitido()).isTrue();
        assertThat(certificado.esValido()).isTrue();
    }

    @Test
    void create_inscripcionInvalida() {
        assertThatThrownBy(() ->
            CertificadoDomainEntity.create("", "COD", EstadoCertificadoDomainEntity.emitido())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cambiarEstado_emitido_a_suspendido() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "a1", "COD", EstadoCertificadoDomainEntity.emitido()
            );

        certificado.cambiarEstado(EstadoCertificadoDomainEntity.suspendido());

        assertThat(certificado.estaSuspendido()).isTrue();
    }

    @Test
    void revocar_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "a1", "COD", EstadoCertificadoDomainEntity.emitido()
            );

        certificado.revocar(EstadoCertificadoDomainEntity.revocado());

        assertThat(certificado.estaRevocado()).isTrue();
    }

    @Test
    void revocar_dobleRevocacion_falla() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "a1", "COD", EstadoCertificadoDomainEntity.revocado()
            );

        assertThatThrownBy(() ->
            certificado.revocar(EstadoCertificadoDomainEntity.revocado())
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reactivar_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "a1", "COD", EstadoCertificadoDomainEntity.revocado()
            );

        certificado.reactivar(EstadoCertificadoDomainEntity.emitido());

        assertThat(certificado.estaEmitido()).isTrue();
    }

    @Test
    void establecerUrlPdf_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "a1", "COD", EstadoCertificadoDomainEntity.emitido()
            );

        certificado.establecerUrlPdf("http://pdf/cert.pdf");

        assertThat(certificado.getUrlPdf()).contains("pdf");
    }

    @Test
    void establecerUrlPdf_vacia_falla() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "a1", "COD", EstadoCertificadoDomainEntity.emitido()
            );

        assertThatThrownBy(() ->
            certificado.establecerUrlPdf("")
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
