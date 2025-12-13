package com.zentry.sigea.module_usuarios.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class TokenUsuarioDomainEntityTest {

    @Test
    void setters_y_getters_funcionanCorrectamente() {
        TokenUsuarioDomainEntity token = new TokenUsuarioDomainEntity();

        Instant expiry = Instant.now().plusSeconds(3600);

        token.setId("1");
        token.setToken("jwt-token");
        token.setUsuarioId("user-1");
        token.setExpiryDate(expiry);

        assertEquals("1", token.getId());
        assertEquals("jwt-token", token.getToken());
        assertEquals("user-1", token.getUsuarioId());
        assertEquals(expiry, token.getExpiryDate());
    }
}
