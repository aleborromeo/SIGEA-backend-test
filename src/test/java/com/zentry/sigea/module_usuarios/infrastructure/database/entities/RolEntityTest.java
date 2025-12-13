package com.zentry.sigea.module_usuarios.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.zentry.sigea.module_usuarios.infrastructure.repositories.RolJPARepository;

@DataJpaTest
class RolEntityTest {

    @Autowired
    private RolJPARepository rolRepository;

    @Test
    void guardarRol_ok() {
        RolEntity rol = new RolEntity();
        rol.setNombreRol("ADMIN");

        RolEntity saved = rolRepository.save(rol);

        assertNotNull(saved.getId());
        assertEquals("ADMIN", saved.getNombreRol());
    }
}
