package com.zentry.sigea.module_certificaciones.core.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EstadoCertificadoDomainEntityTest {

    @Test
    void factory_emitido() {
        EstadoCertificadoDomainEntity estado = EstadoCertificadoDomainEntity.emitido();

        assertThat(estado.getCodigo()).isEqualTo("EMITIDO");
        assertThat(estado.permiteEmision()).isTrue();
        assertThat(estado.esFinal()).isFalse();
    }

    @Test
    void factory_revocado() {
        EstadoCertificadoDomainEntity estado = EstadoCertificadoDomainEntity.revocado();

        assertThat(estado.esFinal()).isTrue();
        assertThat(estado.permiteEmision()).isFalse();
    }

    @Test
    void esValido_true() {
        assertThat(EstadoCertificadoDomainEntity.emitido().esValido()).isTrue();
        assertThat(EstadoCertificadoDomainEntity.revocado().esValido()).isTrue();
        assertThat(EstadoCertificadoDomainEntity.suspendido().esValido()).isTrue();
    }

    @Test
    void esValido_false() {
        EstadoCertificadoDomainEntity estado =
            new EstadoCertificadoDomainEntity("OTRO", "???");

        assertThat(estado.esValido()).isFalse();
    }
}
