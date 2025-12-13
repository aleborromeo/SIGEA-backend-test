package com.zentry.sigea.module_certificaciones.core.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TipoValidadorDomainEntityTest {

    @Test
    void qr_valido() {
        TipoValidadorDomainEntity qr = TipoValidadorDomainEntity.qr();

        assertThat(qr.esValido()).isTrue();
        assertThat(qr.requiereConexion()).isFalse();
        assertThat(qr.esValidoTiempoReal()).isTrue();
    }

    @Test
    void hash_criptografico() {
        TipoValidadorDomainEntity hash = TipoValidadorDomainEntity.hash();

        assertThat(hash.esCriptografico()).isTrue();
        assertThat(hash.requiereConexion()).isFalse();
    }

    @Test
    void url_publica_requiereConexion() {
        TipoValidadorDomainEntity url = TipoValidadorDomainEntity.urlPublica();

        assertThat(url.requiereConexion()).isTrue();
        assertThat(url.esValidoTiempoReal()).isTrue();
    }

    @Test
    void descripcion_por_codigo() {
        assertThat(TipoValidadorDomainEntity.ocsp().getDescripcion())
            .contains("OCSP");
    }
}
