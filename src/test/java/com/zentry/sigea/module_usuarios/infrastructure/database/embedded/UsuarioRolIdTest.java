package com.zentry.sigea.module_usuarios.infrastructure.database.embedded;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UsuarioRolIdTest {

    @Test
    void constructorVacio_noLanzaError() {
        UsuarioRolId id = new UsuarioRolId();
        assertNotNull(id);
    }

    @Test
    void constructorConParametros_asignaValores() {
        UUID usuarioId = UUID.randomUUID();
        UUID rolId = UUID.randomUUID();

        UsuarioRolId id = new UsuarioRolId(usuarioId, rolId);

        assertEquals(usuarioId, id.getIdUsuario());
        assertEquals(rolId, id.getIdRol());
    }

    @Test
    void equals_mismoObjeto_retornaTrue() {
        UsuarioRolId id = new UsuarioRolId(UUID.randomUUID(), UUID.randomUUID());

        assertEquals(id, id);
    }

    @Test
    void equals_mismosValores_retornaTrue() {
        UUID usuarioId = UUID.randomUUID();
        UUID rolId = UUID.randomUUID();

        UsuarioRolId id1 = new UsuarioRolId(usuarioId, rolId);
        UsuarioRolId id2 = new UsuarioRolId(usuarioId, rolId);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equals_valoresDistintos_retornaFalse() {
        UsuarioRolId id1 = new UsuarioRolId(UUID.randomUUID(), UUID.randomUUID());
        UsuarioRolId id2 = new UsuarioRolId(UUID.randomUUID(), UUID.randomUUID());

        assertNotEquals(id1, id2);
    }

    @Test
    void equals_null_retornaFalse() {
        UsuarioRolId id = new UsuarioRolId(UUID.randomUUID(), UUID.randomUUID());

        assertNotEquals(id, null);
    }

    @Test
    void equals_otroTipo_retornaFalse() {
        UsuarioRolId id = new UsuarioRolId(UUID.randomUUID(), UUID.randomUUID());

        assertNotEquals(id, "no-soy-un-id");
    }
}
