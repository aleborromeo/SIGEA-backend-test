package com.zentry.sigea.module_usuarios.presentation.models.responseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioResponseDTO Tests")
class UsuarioResponseDTOTest {

    private final String NOMBRES = "Juan";
    private final String APELLIDOS = "Pérez";
    private final String CORREO = "juan.perez@test.com";
    private final String TELEFONO_COMPLETO = "+51999888777";
    private final String ROL = "ORGANIZADOR";
    private final String CREATED_AT = "2024-01-01T10:00:00";
    private final String UPDATED_AT = "2024-01-01T11:00:00";

    @Test
    @DisplayName("Constructor vacío debe inicializar campos a nulo")
    void constructorVacio_debeInicializarNulo() {
        // ACT
        UsuarioResponseDTO dto = new UsuarioResponseDTO();

        // ASSERT
        assertNotNull(dto);
        assertNull(dto.getNombres());
        assertNull(dto.getCorreo());
    }

    @Test
    @DisplayName("Setters deben asignar valores correctamente")
    void setters_debenAsignarValores() {
        // ARRANGE
        UsuarioResponseDTO dto = new UsuarioResponseDTO();

        // ACT
        dto.setNombres(NOMBRES);
        dto.setApellidos(APELLIDOS);
        dto.setCorreo(CORREO);
        dto.setTelefonoCompleto(TELEFONO_COMPLETO);
        dto.setNombreRol(ROL);
        dto.setCreatedAt(CREATED_AT);
        dto.setUpdatedAt(UPDATED_AT);

        // ASSERT
        assertEquals(NOMBRES, dto.getNombres());
        assertEquals(APELLIDOS, dto.getApellidos());
        assertEquals(CORREO, dto.getCorreo());
        assertEquals(TELEFONO_COMPLETO, dto.getTelefonoCompleto());
        assertEquals(ROL, dto.getNombreRol());
        assertEquals(CREATED_AT, dto.getCreatedAt());
        assertEquals(UPDATED_AT, dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Setters deben aceptar valores nulos")
    void setters_debenAceptarValoresNulos() {
        // ARRANGE
        UsuarioResponseDTO dto = new UsuarioResponseDTO();

        // ACT
        dto.setNombres(null);
        dto.setNombreRol(null);
        dto.setTelefonoCompleto(null);

        // ASSERT
        assertNull(dto.getNombres());
        assertNull(dto.getNombreRol());
        assertNull(dto.getTelefonoCompleto());
    }
}