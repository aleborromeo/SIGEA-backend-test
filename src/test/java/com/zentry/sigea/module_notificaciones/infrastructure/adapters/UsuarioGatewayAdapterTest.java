package com.zentry.sigea.module_notificaciones.infrastructure.adapters;

import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioGatewayAdapterTest {

    @Mock
    private UsuarioJPARepository usuarioRepository;

    @InjectMocks
    private UsuarioGatewayAdapter usuarioGatewayAdapter;

    @Test
    void obtenerCorreoUsuario_debeRetornarCorreo() {
        UUID userId = UUID.randomUUID();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCorreo("correo@test.com");

        when(usuarioRepository.findById(userId))
            .thenReturn(Optional.of(usuario));

        Optional<String> correo = usuarioGatewayAdapter.obtenerCorreoUsuario(userId.toString());

        assertTrue(correo.isPresent());
        assertEquals("correo@test.com", correo.get());
    }

    @Test
    void obtenerCorreoUsuario_uuidInvalido_retornaEmpty() {
        Optional<String> correo = usuarioGatewayAdapter.obtenerCorreoUsuario("uuid-invalido");

        assertTrue(correo.isEmpty());
    }

    @Test
    void obtenerTelefonoUsuario_conExtension() {
        UUID userId = UUID.randomUUID();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setTelefono("999999999");
        usuario.setExtensionTelefonica("+51");

        when(usuarioRepository.findById(userId))
            .thenReturn(Optional.of(usuario));

        Optional<String> telefono = usuarioGatewayAdapter.obtenerTelefonoUsuario(userId.toString());

        assertTrue(telefono.isPresent());
        assertEquals("+51999999999", telefono.get());
    }

    @Test
    void obtenerTelefonoUsuario_sinTelefono_retornaEmpty() {
        UUID userId = UUID.randomUUID();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setTelefono(null);

        when(usuarioRepository.findById(userId))
            .thenReturn(Optional.of(usuario));

        Optional<String> telefono = usuarioGatewayAdapter.obtenerTelefonoUsuario(userId.toString());

        assertTrue(telefono.isEmpty());
    }

    @Test
    void obtenerNombreUsuario_conNombreYApellido() {
        UUID userId = UUID.randomUUID();

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");

        when(usuarioRepository.findById(userId))
            .thenReturn(Optional.of(usuario));

        Optional<String> nombre = usuarioGatewayAdapter.obtenerNombreUsuario(userId.toString());

        assertTrue(nombre.isPresent());
        assertEquals("Juan Pérez", nombre.get());
    }

    @Test
    void obtenerNombreUsuario_uuidInvalido_retornaEmpty() {
        Optional<String> nombre = usuarioGatewayAdapter.obtenerNombreUsuario("mal-id");

        assertTrue(nombre.isEmpty());
    }
}
