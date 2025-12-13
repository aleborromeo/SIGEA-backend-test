package com.zentry.sigea.module_usuarios.services;

import com.zentry.sigea.module_usuarios.core.entities.RolDomainEntity;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.CrearRolUseCase;
import com.zentry.sigea.module_usuarios.services.usecases.administrador.RegisterUsuarioUseCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministradorServiceTest {

    @Mock
    private RegisterUsuarioUseCase registerUsuarioUseCase;

    @Mock
    private CrearRolUseCase crearRolUseCase;

    @InjectMocks
    private AdministradorService administradorService;

    private UsuarioDomainEntity usuario;
    private RolDomainEntity rol;

    @BeforeEach
    void setUp() {
        usuario = mock(UsuarioDomainEntity.class);
        rol = mock(RolDomainEntity.class);
    }

    @Test
    void register_usuario_ok() throws IOException {
        when(registerUsuarioUseCase.execute(any(), anyList()))
                .thenReturn("Usuario registrado con éxito.");

        String result =
                administradorService.registerUsuario(usuario, List.of("rol-1"));

        assertEquals("Usuario registrado con éxito.", result);
        verify(registerUsuarioUseCase).execute(usuario, List.of("rol-1"));
    }

    @Test
    void crear_rol_ok() {
        when(crearRolUseCase.execute(any()))
                .thenReturn("Rol registrado con éxito");

        String result = administradorService.crearRol(rol);

        assertEquals("Rol registrado con éxito", result);
        verify(crearRolUseCase).execute(rol);
    }
}
