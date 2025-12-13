package com.zentry.sigea.module_usuarios.services.usecases.administrador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IRolRepository;

@ExtendWith(MockitoExtension.class)
class CrearRolUseCaseTest {

    @Mock
    private IRolRepository rolRepository;

    @InjectMocks
    private CrearRolUseCase crearRolUseCase;

    @Test
    void execute_saves_role_and_returns_message() {
        // Arrange
        RolDomainEntity rol = new RolDomainEntity();

        // Act
        String result = crearRolUseCase.execute(rol);

        // Assert
        assertEquals("Rol registrado con exito", result);
        verify(rolRepository, times(1)).save(rol);
    }
}
