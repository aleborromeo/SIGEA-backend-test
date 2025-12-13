package com.zentry.sigea.module_usuarios.presentation.models.responseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GeneralResponseDTO Tests")
class GeneralResponseDTOTest {

    @Test
    @DisplayName("Constructor debe asignar todos los campos correctamente")
    void constructor_debeAsignarCampos() {
        // ARRANGE
        boolean status = true;
        String message = "Operación exitosa";
        Map<String, String> extraData = Map.of("key", "value");

        // ACT
        GeneralResponseDTO<Map<String, String>> response = new GeneralResponseDTO<>(status, message, extraData);

        // ASSERT
        assertTrue(response.isStatus());
        assertEquals(message, response.getMessage());
        assertEquals(extraData, response.getExtraData());
        assertEquals("value", response.getExtraData().get("key"));
    }

    @Test
    @DisplayName("Constructor debe manejar extraData nulo")
    void constructor_debeManejarExtraDataNulo() {
        // ACT
        GeneralResponseDTO<Void> response = new GeneralResponseDTO<>(false, "Error", null);

        // ASSERT
        assertFalse(response.isStatus());
        assertEquals("Error", response.getMessage());
        assertNull(response.getExtraData());
    }

    @Test
    @DisplayName("isStatus() debe reflejar el estado correcto")
    void isStatus_debeReflejarEstado() {
        // ARRANGE & ACT
        GeneralResponseDTO<Void> success = new GeneralResponseDTO<>(true, "OK", null);
        GeneralResponseDTO<Void> failure = new GeneralResponseDTO<>(false, "Error", null);

        // ASSERT
        assertTrue(success.isStatus());
        assertFalse(failure.isStatus());
    }
}