package com.zentry.sigea.module_usuarios.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_usuarios.core.entities.CodigoVerificacionDomainEntity;

class CodigoVerificacionDomainEntityTest {

   @Test
    void create_generaCodigoYExpiracion() {
        CodigoVerificacionDomainEntity entity =
            CodigoVerificacionDomainEntity.create("123456", "test@mail.com");

        assertEquals("123456", entity.getCodigo());
        assertEquals("test@mail.com", entity.getCorreo());
        assertNotNull(entity.getExpiresAt());
        assertTrue(entity.getExpiresAt().isAfter(Instant.now()));
    }

}
