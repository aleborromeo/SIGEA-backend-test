package com.zentry.sigea.module_usuarios.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

@DataJpaTest
class UsuarioEntityTest {

    @Autowired
    private UsuarioJPARepository usuarioRepository;

    @Test
    void guardarUsuario_funciona() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCorreo("test@mail.com");
        usuario.setPasswordHash("hash");
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");

        UsuarioEntity saved = usuarioRepository.save(usuario);

        assertNotNull(saved.getId());
        assertEquals("test@mail.com", saved.getCorreo());
    }
}
