package com.zentry.sigea.module_usuarios.core.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para RolDomainEntity")
class RolDomainEntityTest {

    @Test
    @DisplayName("Crear rol con método create - Debe inicializar correctamente")
    void create_DebeInicializarRolCorrectamente() {
        // Arrange
        String nombreRol = "ROLE_ADMINISTRADOR";
        String descripcion = "Administrador del sistema";
        LocalDateTime antes = LocalDateTime.now();

        // Act
        RolDomainEntity rol = RolDomainEntity.create(nombreRol, descripcion);

        // Assert
        assertAll("Verificar propiedades del rol",
            () -> assertThat(rol.getNombreRol()).isEqualTo(nombreRol),
            () -> assertThat(rol.getDescripcion()).isEqualTo(descripcion),
            () -> assertThat(rol.getCreateAt()).isNotNull(),
            () -> assertThat(rol.getUpdateAt()).isNotNull(),
            () -> assertThat(rol.getCreateAt()).isAfterOrEqualTo(antes),
            () -> assertThat(rol.getUpdateAt()).isAfterOrEqualTo(antes),
            () -> assertThat(rol.getCreateAt()).isEqualTo(rol.getUpdateAt())
        );
    }

    @Test
    @DisplayName("Crear rol sin descripción - Debe aceptar null")
    void create_SinDescripcion_DebeAceptarNull() {
        // Arrange & Act
        RolDomainEntity rol = RolDomainEntity.create("ROLE_PARTICIPANTE", null);

        // Assert
        assertThat(rol.getNombreRol()).isEqualTo("ROLE_PARTICIPANTE");
        assertThat(rol.getDescripcion()).isNull();
    }

    @Test
    @DisplayName("Setters y Getters - Deben funcionar correctamente")
    void settersYGetters_DebenFuncionarCorrectamente() {
        // Arrange
        RolDomainEntity rol = new RolDomainEntity();
        String nombreRol = "ROLE_ORGANIZADOR";
        String descripcion = "Organizador de eventos";
        LocalDateTime now = LocalDateTime.now();

        // Act
        rol.setNombreRol(nombreRol);
        rol.setDescripcion(descripcion);
        rol.setCreateAt(now);
        rol.setUpdateAt(now);

        // Assert
        assertAll("Verificar getters",
            () -> assertEquals(nombreRol, rol.getNombreRol()),
            () -> assertEquals(descripcion, rol.getDescripcion()),
            () -> assertEquals(now, rol.getCreateAt()),
            () -> assertEquals(now, rol.getUpdateAt())
        );
    }

    @Test
    @DisplayName("Actualizar timestamps - Debe permitir modificación")
    void actualizarTimestamps_DebePermitirModificacion() {
        // Arrange
        RolDomainEntity rol = RolDomainEntity.create("ROLE_TEST", "Test");
        LocalDateTime createAt = rol.getCreateAt();
        
        // Simular espera
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        
        // Act
        LocalDateTime nuevoUpdateAt = LocalDateTime.now();
        rol.setUpdateAt(nuevoUpdateAt);

        // Assert
        assertThat(rol.getCreateAt()).isEqualTo(createAt);
        assertThat(rol.getUpdateAt()).isAfter(createAt);
    }
}