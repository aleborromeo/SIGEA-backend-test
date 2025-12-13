package com.zentry.sigea.module_usuarios.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.zentry.sigea.module_usuarios.infrastructure.repositories.RolJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioRolJPARepository;

@DataJpaTest
class UsuarioRolEntityTest {

    @Autowired
    private UsuarioJPARepository usuarioRepo;

    @Autowired
    private RolJPARepository rolRepo;

    @Autowired
    private UsuarioRolJPARepository usuarioRolRepo;

    @Test
    void guardarRelacionUsuarioRol_ok() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCorreo("rol@mail.com");
        usuario.setPasswordHash("hash"); // ✅
        usuario.setNombres("Ana");
        usuario.setApellidos("Lopez");
        usuario.setDni("87654321");
        usuario.setCorreoVerificado(false);
        usuario.setTelefono("988888888");
        usuario.setExtensionTelefonica("+51");
        usuario = usuarioRepo.save(usuario);

        RolEntity rol = new RolEntity();
        rol.setNombreRol("ADMIN");
        rol.setDescripcion("Administrador");
        rol = rolRepo.save(rol);

        UsuarioRolEntity ur = new UsuarioRolEntity();
        ur.setUsuario(usuario);
        ur.setRol(rol);
        ur.setAsignadoEn(LocalDateTime.now());

        UsuarioRolEntity saved = usuarioRolRepo.save(ur);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertNotNull(saved.getUsuario());
        assertNotNull(saved.getRol());
    }
}
