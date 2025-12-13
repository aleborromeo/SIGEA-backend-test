package com.zentry.sigea.module_usuarios.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class CodigoVerificacionDomainEntityTest {

    @Test
    void create_creaCodigoConExpiracionCorrecta() {
        CodigoVerificacionDomainEntity entity =
            CodigoVerificacionDomainEntity.create("123456", "test@mail.com");

        assertEquals("123456", entity.getCodigo());
        assertEquals("test@mail.com", entity.getCorreo());

        Instant ahora = Instant.now();
        assertTrue(entity.getExpiresAt().isAfter(ahora));
        assertTrue(entity.getExpiresAt().isBefore(ahora.plusSeconds(5 * 60 + 5)));
    }
}
