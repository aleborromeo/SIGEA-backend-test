package com.zentry.sigea.module_usuarios.services.usecases.administrador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;

@ExtendWith(MockitoExtension.class)
class RegisterUsuarioUseCaseTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private IUsuarioRolRepository usuarioRolRepository;

    @InjectMocks
    private RegisterUsuarioUseCase registerUsuarioUseCase;

    @Test
    void execute_registers_user_and_roles_successfully() throws Exception {
        // Arrange
        UsuarioDomainEntity usuario = new UsuarioDomainEntity();
        usuario.setCorreo("test@correo.com");
        usuario.setPasswordHash("plainPassword");

        List<String> rolesId = List.of("ROLE_USER");

        when(passwordEncoder.encode("plainPassword"))
            .thenReturn("hashedPassword");

        when(usuarioRepository.findIdByCorreo("test@correo.com"))
            .thenReturn("user-id-1");

        // Act
        String result =
            registerUsuarioUseCase.execute(usuario, rolesId);

        // Assert
        assertEquals("Usuario registrado con exito.", result);

        verify(passwordEncoder).encode("plainPassword");
        verify(usuarioRepository).save(usuario);
        verify(usuarioRolRepository)
            .saveOneUserWithAllRolesId("user-id-1", rolesId);

        assertEquals("hashedPassword", usuario.getPasswordHash());
    }

       @Test
    void execute_throws_exception_when_user_id_not_found() {
        // Arrange
        UsuarioDomainEntity usuario = new UsuarioDomainEntity();
        usuario.setCorreo("fail@correo.com");
        usuario.setPasswordHash("password");

        when(passwordEncoder.encode(any()))
            .thenReturn("hashedPassword");

        when(usuarioRepository.findIdByCorreo("fail@correo.com"))
            .thenReturn(null);

        // Act & Assert
        IOException exception = assertThrows(
            IOException.class,
            () -> registerUsuarioUseCase.execute(usuario, List.of("ROLE_USER"))
        );

        assertEquals(
            "No se pudo encontrar el ID del usuario.",
            exception.getMessage()
        );

        verify(usuarioRepository).save(usuario);
        verify(usuarioRolRepository, never())
            .saveOneUserWithAllRolesId(any(), any());
    }
}
 
