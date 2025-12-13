package com.zentry.sigea.module_inscripciones.presentation.models.mappers;

import com.zentry.sigea.module_inscripciones.presentation.models.requestDTO.CrearInscripcionRequest;
import com.zentry.sigea.module_inscripciones.services.serviceDTO.CrearInscripcionServiceDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para CrearInscripcionMapper
 * Verifica el mapeo entre la capa de Presentación y la capa de Servicios.
 */
@DisplayName("CrearInscripcionMapper Tests")
class CrearInscripcionMapperTest {

    private final LocalDate FECHA_INSCRIPCION = LocalDate.of(2025, 1, 1);
    private final String USUARIO_ID = "user-uuid-1";
    private final String ACTIVIDAD_ID = "act-uuid-2";
    private final String ESTADO_ID = "estado-uuid-3";

    private CrearInscripcionRequest createMockRequest() {
        return new CrearInscripcionRequest(
            FECHA_INSCRIPCION,
            USUARIO_ID,
            ACTIVIDAD_ID,
            ESTADO_ID
        );
    }

    @Test
    @DisplayName("requestToService - Debe mapear todos los campos correctamente")
    void requestToService_debeMapearTodosLosCampos() {
        // ARRANGE
        CrearInscripcionRequest request = createMockRequest();

        // ACT
        CrearInscripcionServiceDTO serviceDTO = CrearInscripcionMapper.requestToService(request);

        // ASSERT
        assertNotNull(serviceDTO);
        assertEquals(FECHA_INSCRIPCION, serviceDTO.getFechaInscripcion());
        assertEquals(USUARIO_ID, serviceDTO.getUsuarioId());
        assertEquals(ACTIVIDAD_ID, serviceDTO.getActividadId());
        assertEquals(ESTADO_ID, serviceDTO.getEstadoId());
    }

    @Test
    @DisplayName("requestToService - Debe manejar request nulo")
    void requestToService_debeManejarRequestNulo() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class, () -> {
            CrearInscripcionMapper.requestToService(null);
        }, "Debería lanzar NullPointerException si la entidad de entrada es nula.");
    }
    
    @Test
    @DisplayName("requestToService - Debe manejar campos nulos en el Request")
    void requestToService_debeManejarCamposNulos() {
        // ARRANGE
        CrearInscripcionRequest request = new CrearInscripcionRequest(
            null, null, null, null // Campos nulos
        );

        // ACT
        CrearInscripcionServiceDTO serviceDTO = CrearInscripcionMapper.requestToService(request);

        // ASSERT
        assertNotNull(serviceDTO);
        assertNull(serviceDTO.getFechaInscripcion());
        assertNull(serviceDTO.getUsuarioId());
        assertNull(serviceDTO.getActividadId());
        assertNull(serviceDTO.getEstadoId());
    }
}