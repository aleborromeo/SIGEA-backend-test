package com.zentry.sigea.module_usuarios.services.usecases.participante;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IRolRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.RolJPARepository;

@ExtendWith(MockitoExtension.class)
class RegistrarParticipanteUseCaseTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private IRolRepository rolRepository;

    @Mock
    private IUsuarioRolRepository usuarioRolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // Aunque no se usa, es requerido por el constructor
    @Mock
    private RolJPARepository rolJPARepository;

    @InjectMocks
    private RegistrarParticipanteUseCase registrarParticipanteUseCase;

    @Test
    void execute_registers_participant_successfully() {
        // Arrange
        UsuarioDomainEntity usuario = new UsuarioDomainEntity();
        usuario.setCorreo("test@correo.com");
        usuario.setPasswordHash("plainPassword");

        when(rolRepository.findIdByNombreRol("PARTICIPANTE"))
            .thenReturn("rol-id-1");

        when(passwordEncoder.encode("plainPassword"))
            .thenReturn("hashedPassword");

        when(usuarioRepository.findIdByCorreo("test@correo.com"))
            .thenReturn("user-id-1");

        // Act
        String result =
            registrarParticipanteUseCase.execute(usuario);

        // Assert
        assertEquals("Participante registrado con exito", result);

        verify(rolRepository).findIdByNombreRol("PARTICIPANTE");
        verify(passwordEncoder).encode("plainPassword");
        verify(usuarioRepository).save(usuario);
        verify(usuarioRepository).findIdByCorreo("test@correo.com");
        verify(usuarioRolRepository)
            .save("user-id-1", "rol-id-1");

        assertEquals("hashedPassword", usuario.getPasswordHash());
    }

    @Test
    void execute_throws_exception_when_role_not_found() {
        // Arrange
        UsuarioDomainEntity usuario = new UsuarioDomainEntity();

        when(rolRepository.findIdByNombreRol("PARTICIPANTE"))
            .thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> registrarParticipanteUseCase.execute(usuario)
        );

        assertEquals(
            "No se encontro el ID de rol para participantes.",
            exception.getMessage()
        );

        verify(usuarioRepository, never()).save(any());
        verify(usuarioRolRepository, never()).save(any(), any());
    }

    @Test
    void execute_throws_exception_when_user_id_not_found() {
        // Arrange
        UsuarioDomainEntity usuario = new UsuarioDomainEntity();
        usuario.setCorreo("fail@correo.com");
        usuario.setPasswordHash("password");

        when(rolRepository.findIdByNombreRol("PARTICIPANTE"))
            .thenReturn("rol-id-1");

        when(passwordEncoder.encode("password"))
            .thenReturn("hashedPassword");

        when(usuarioRepository.findIdByCorreo("fail@correo.com"))
            .thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> registrarParticipanteUseCase.execute(usuario)
        );

        assertEquals(
            "No se encontro el ID del usuario registrado.",
            exception.getMessage()
        );

        verify(usuarioRepository).save(usuario);
        verify(usuarioRolRepository, never()).save(any(), any());
    }
}
