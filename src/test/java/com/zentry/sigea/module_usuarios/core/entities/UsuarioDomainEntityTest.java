package com.zentry.sigea.module_usuarios.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UsuarioDomainEntityTest {

    @Test
    void create_creaUsuarioCorrectamente() {
        UsuarioDomainEntity usuario = UsuarioDomainEntity.create(
            "Juan",
            "Pérez",
            "juan@mail.com",
            "hash123",
            "12345678",
            null,
            "900700897",
            "+51"
        );

        assertEquals("Juan", usuario.getNombres());
        assertEquals("Pérez", usuario.getApellidos());
        assertEquals("juan@mail.com", usuario.getCorreo());
        assertEquals("hash123", usuario.getPasswordHash());
        assertEquals("12345678", usuario.getDni());
        assertFalse(usuario.getCorreoVerificado());
        assertNotNull(usuario.getCreatedAt());
        assertNotNull(usuario.getUpdatedAt());
        assertEquals(usuario.getCreatedAt(), usuario.getUpdatedAt());
        assertEquals("900700897", usuario.getTelefono());
        assertEquals("+51", usuario.getExtensionTelefonica());
    }

    @Test
    void correoVerificado_true_seRespeta() {
        UsuarioDomainEntity usuario = UsuarioDomainEntity.create(
            "Ana",
            "Lopez",
            "ana@mail.com",
            "hash",
            "87654321",
            true,
            "999888777",
            "+51"
        );

        assertTrue(usuario.getCorreoVerificado());
    }
}
