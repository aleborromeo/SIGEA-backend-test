package com.zentry.sigea.module_actividad.config;

import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.repositories.IEstadoActividadRepository;
import com.zentry.sigea.module_actividad.core.repositories.ITipoActividadRepository;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.ActividadRequest;
import com.zentry.sigea.module_actividad.presentation.models.requestDTO.CrearActividadRequest;
import com.zentry.sigea.module_actividad.config.ActividadMappingConfig.ActividadRequestMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActividadMappingConfig Tests")
class ActividadMappingConfigTest {

    @Mock
    private IEstadoActividadRepository estadoActividadRepository;

    @Mock
    private ITipoActividadRepository tipoActividadRepository;

    private ActividadMappingConfig actividadMappingConfig;
    private ActividadRequestMapper mapper;

    @BeforeEach
    void setUp() {
        actividadMappingConfig = new ActividadMappingConfig(
            estadoActividadRepository,
            tipoActividadRepository
        );
        mapper = actividadMappingConfig.actividadRequestMapper();
    }

    @Nested
    @DisplayName("Configuración de Bean")
    class BeanConfigurationTests {

        @Test
        @DisplayName("Debería crear el bean ActividadRequestMapper correctamente")
        void debeCrearBeanActividadRequestMapper() {
            // Act
            ActividadRequestMapper result = actividadMappingConfig.actividadRequestMapper();

            // Assert
            assertNotNull(result);
            assertInstanceOf(ActividadRequestMapper.class, result);
        }

        @Test
        @DisplayName("Debería crear instancias diferentes del mapper en cada llamada")
        void debeCrearInstanciasDiferentesDelMapper() {
            // Act
            ActividadRequestMapper mapper1 = actividadMappingConfig.actividadRequestMapper();
            ActividadRequestMapper mapper2 = actividadMappingConfig.actividadRequestMapper();

            // Assert
            assertNotNull(mapper1);
            assertNotNull(mapper2);
            assertNotSame(mapper1, mapper2);
        }
    }

    @Nested
    @DisplayName("Método mapToActividadRequest()")
    class MapToActividadRequestTests {

        @Test
        @DisplayName("Debería mapear correctamente un CrearActividadRequest válido")
        void debeMappearCorrectamenteRequestValido() {
            // Arrange
            String estadoId = "estado-123";
            String tipoActividadId = "tipo-456";
            String organizadorId = "org-789";

            EstadoActividadDomainEntity estadoMock = new EstadoActividadDomainEntity();
            estadoMock.setEstadoActividadId(estadoId);
            estadoMock.setCodigo("ACTIVO");
            estadoMock.setEtiqueta("Activo");

            TipoActividadDomainEntity tipoMock = new TipoActividadDomainEntity();
            tipoMock.setTipoActividadId(tipoActividadId);
            tipoMock.setNombreActividad("Conferencia");
            tipoMock.setDescripcion("Conferencia académica");

            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.of(estadoMock));
            when(tipoActividadRepository.findById(tipoActividadId)).thenReturn(Optional.of(tipoMock));

            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Conferencia de IA");
            request.setDescripcion("Conferencia sobre inteligencia artificial");
            request.setFechaInicio(LocalDate.of(2024, 6, 15));
            request.setFechaFin(LocalDate.of(2024, 6, 16));
            request.setEstadoId(estadoId);
            request.setOrganizadorId(organizadorId);
            request.setTipoActividadId(tipoActividadId);
            request.setUbicacion("Auditorio Principal");

            // Act
            ActividadRequest result = mapper.mapToActividadRequest(request);

            // Assert
            assertNotNull(result);
            assertEquals("Conferencia de IA", result.getTitulo());
            assertEquals("Conferencia sobre inteligencia artificial", result.getDescripcion());
            assertEquals(LocalDate.of(2024, 6, 15), result.getFechaInicio());
            assertEquals(LocalDate.of(2024, 6, 16), result.getFechaFin());
            assertEquals(estadoMock, result.getEstado());
            assertEquals(organizadorId, result.getOrganizadorId());
            assertEquals(tipoMock, result.getTipoActividad());
            assertEquals("Auditorio Principal", result.getUbicacion());

            verify(estadoActividadRepository, times(1)).findById(estadoId);
            verify(tipoActividadRepository, times(1)).findById(tipoActividadId);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando estadoId es null")
        void debeLanzarExcepcionCuandoEstadoIdEsNull() {
            // Arrange
            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Evento");
            request.setEstadoId(null); // Null
            request.setTipoActividadId("tipo-123");
            request.setOrganizadorId("org-123");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.mapToActividadRequest(request)
            );

            assertEquals("El ID del estado de actividad es obligatorio", exception.getMessage());
            verify(estadoActividadRepository, never()).findById(anyString());
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando tipoActividadId es null")
        void debeLanzarExcepcionCuandoTipoActividadIdEsNull() {
            // Arrange
            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Evento");
            request.setEstadoId("estado-123");
            request.setTipoActividadId(null); // Null
            request.setOrganizadorId("org-123");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.mapToActividadRequest(request)
            );

            assertEquals("El ID del tipo de actividad es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando organizadorId es null")
        void debeLanzarExcepcionCuandoOrganizadorIdEsNull() {
            // Arrange
            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Evento");
            request.setEstadoId("estado-123");
            request.setTipoActividadId("tipo-123");
            request.setOrganizadorId(null); // Null

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.mapToActividadRequest(request)
            );

            assertEquals("El ID del organizador es obligatorio", exception.getMessage());
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando no se encuentra el estado")
        void debeLanzarExcepcionCuandoNoSeEncuentraEstado() {
            // Arrange
            String estadoId = "estado-inexistente";
            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.empty());

            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Evento");
            request.setEstadoId(estadoId);
            request.setTipoActividadId("tipo-123");
            request.setOrganizadorId("org-123");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.mapToActividadRequest(request)
            );

            assertEquals("No se encontró un estado de actividad con ID: " + estadoId, exception.getMessage());
            verify(estadoActividadRepository, times(1)).findById(estadoId);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando no se encuentra el tipo de actividad")
        void debeLanzarExcepcionCuandoNoSeEncuentraTipoActividad() {
            // Arrange
            String estadoId = "estado-123";
            String tipoActividadId = "tipo-inexistente";

            EstadoActividadDomainEntity estadoMock = new EstadoActividadDomainEntity();
            estadoMock.setEstadoActividadId(estadoId);

            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.of(estadoMock));
            when(tipoActividadRepository.findById(tipoActividadId)).thenReturn(Optional.empty());

            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Evento");
            request.setEstadoId(estadoId);
            request.setTipoActividadId(tipoActividadId);
            request.setOrganizadorId("org-123");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.mapToActividadRequest(request)
            );

            assertEquals("No se encontró un tipo de actividad con ID: " + tipoActividadId, exception.getMessage());
            verify(tipoActividadRepository, times(1)).findById(tipoActividadId);
        }

        @Test
        @DisplayName("Debería mapear correctamente con ubicación null")
        void debeMappearCorrectamenteConUbicacionNull() {
            // Arrange
            String estadoId = "estado-123";
            String tipoActividadId = "tipo-456";

            EstadoActividadDomainEntity estadoMock = new EstadoActividadDomainEntity();
            TipoActividadDomainEntity tipoMock = new TipoActividadDomainEntity();

            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.of(estadoMock));
            when(tipoActividadRepository.findById(tipoActividadId)).thenReturn(Optional.of(tipoMock));

            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Evento Virtual");
            request.setEstadoId(estadoId);
            request.setTipoActividadId(tipoActividadId);
            request.setOrganizadorId("org-123");
            request.setUbicacion(null); // Ubicación null

            // Act
            ActividadRequest result = mapper.mapToActividadRequest(request);

            // Assert
            assertNotNull(result);
            assertNull(result.getUbicacion());
        }
    }

    @Nested
    @DisplayName("Método getEstadoActividadById()")
    class GetEstadoActividadByIdTests {

        @Test
        @DisplayName("Debería obtener estado correctamente con ID válido")
        void debeObtenerEstadoCorrectamente() {
            // Arrange
            String estadoId = "estado-123";
            EstadoActividadDomainEntity estadoMock = new EstadoActividadDomainEntity();
            estadoMock.setEstadoActividadId(estadoId);
            estadoMock.setCodigo("ACTIVO");

            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.of(estadoMock));

            // Act
            EstadoActividadDomainEntity result = mapper.getEstadoActividadById(estadoId);

            // Assert
            assertNotNull(result);
            assertEquals(estadoId, result.getEstadoActividadId());
            assertEquals("ACTIVO", result.getCodigo());
            verify(estadoActividadRepository, times(1)).findById(estadoId);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el ID es null")
        void debeLanzarExcepcionCuandoIdEsNull() {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.getEstadoActividadById(null)
            );

            assertEquals("El ID del estado de actividad debe ser un número positivo", exception.getMessage());
            verify(estadoActividadRepository, never()).findById(anyString());
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando no se encuentra el estado")
        void debeLanzarExcepcionCuandoNoSeEncuentraEstado() {
            // Arrange
            String estadoId = "estado-inexistente";
            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.empty());

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.getEstadoActividadById(estadoId)
            );

            assertEquals("No se encontró un estado de actividad con ID: " + estadoId, exception.getMessage());
            verify(estadoActividadRepository, times(1)).findById(estadoId);
        }
    }

    @Nested
    @DisplayName("Método getTipoActividadById()")
    class GetTipoActividadByIdTests {

        @Test
        @DisplayName("Debería obtener tipo de actividad correctamente con ID válido")
        void debeObtenerTipoActividadCorrectamente() {
            // Arrange
            String tipoActividadId = "tipo-123";
            TipoActividadDomainEntity tipoMock = new TipoActividadDomainEntity();
            tipoMock.setTipoActividadId(tipoActividadId);
            tipoMock.setNombreActividad("Workshop");

            when(tipoActividadRepository.findById(tipoActividadId)).thenReturn(Optional.of(tipoMock));

            // Act
            TipoActividadDomainEntity result = mapper.getTipoActividadById(tipoActividadId);

            // Assert
            assertNotNull(result);
            assertEquals(tipoActividadId, result.getTipoActividadId());
            assertEquals("Workshop", result.getNombreActividad());
            verify(tipoActividadRepository, times(1)).findById(tipoActividadId);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el ID es null")
        void debeLanzarExcepcionCuandoIdEsNull() {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.getTipoActividadById(null)
            );

            assertEquals("El ID del tipo de actividad debe ser un número positivo", exception.getMessage());
            verify(tipoActividadRepository, never()).findById(anyString());
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando no se encuentra el tipo")
        void debeLanzarExcepcionCuandoNoSeEncuentraTipo() {
            // Arrange
            String tipoActividadId = "tipo-inexistente";
            when(tipoActividadRepository.findById(tipoActividadId)).thenReturn(Optional.empty());

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.getTipoActividadById(tipoActividadId)
            );

            assertEquals("No se encontró un tipo de actividad con ID: " + tipoActividadId, exception.getMessage());
            verify(tipoActividadRepository, times(1)).findById(tipoActividadId);
        }
    }

    @Nested
    @DisplayName("Validación de IDs requeridos")
    class ValidateRequiredIdsTests {

        @Test
        @DisplayName("Debería validar en orden: estadoId, tipoActividadId, organizadorId")
        void debeValidarEnOrden() {
            // Arrange - Sin estadoId
            CrearActividadRequest request1 = new CrearActividadRequest();
            request1.setEstadoId(null);

            // Act & Assert
            IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.mapToActividadRequest(request1)
            );
            assertEquals("El ID del estado de actividad es obligatorio", exception1.getMessage());

            // Arrange - Sin tipoActividadId
            CrearActividadRequest request2 = new CrearActividadRequest();
            request2.setEstadoId("estado-123");
            request2.setTipoActividadId(null);

            // Act & Assert
            IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.mapToActividadRequest(request2)
            );
            assertEquals("El ID del tipo de actividad es obligatorio", exception2.getMessage());

            // Arrange - Sin organizadorId
            CrearActividadRequest request3 = new CrearActividadRequest();
            request3.setEstadoId("estado-123");
            request3.setTipoActividadId("tipo-123");
            request3.setOrganizadorId(null);

            // Act & Assert
            IllegalArgumentException exception3 = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.mapToActividadRequest(request3)
            );
            assertEquals("El ID del organizador es obligatorio", exception3.getMessage());
        }

        @Test
        @DisplayName("Debería pasar la validación con todos los IDs presentes")
        void debePasarValidacionConTodosLosIds() {
            // Arrange
            String estadoId = "estado-123";
            String tipoActividadId = "tipo-456";
            String organizadorId = "org-789";

            EstadoActividadDomainEntity estadoMock = new EstadoActividadDomainEntity();
            TipoActividadDomainEntity tipoMock = new TipoActividadDomainEntity();

            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.of(estadoMock));
            when(tipoActividadRepository.findById(tipoActividadId)).thenReturn(Optional.of(tipoMock));

            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Evento");
            request.setEstadoId(estadoId);
            request.setTipoActividadId(tipoActividadId);
            request.setOrganizadorId(organizadorId);

            // Act & Assert
            assertDoesNotThrow(() -> mapper.mapToActividadRequest(request));
        }
    }

    @Nested
    @DisplayName("Casos Edge")
    class EdgeCasesTests {

        @Test
        @DisplayName("Debería manejar IDs con caracteres especiales")
        void debeManejarIdsConCaracteresEspeciales() {
            // Arrange
            String estadoId = "estado-123-ABC_xyz";
            String tipoActividadId = "tipo-456-DEF_uvw";

            EstadoActividadDomainEntity estadoMock = new EstadoActividadDomainEntity();
            estadoMock.setEstadoActividadId(estadoId);

            TipoActividadDomainEntity tipoMock = new TipoActividadDomainEntity();
            tipoMock.setTipoActividadId(tipoActividadId);

            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.of(estadoMock));
            when(tipoActividadRepository.findById(tipoActividadId)).thenReturn(Optional.of(tipoMock));

            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo("Evento");
            request.setEstadoId(estadoId);
            request.setTipoActividadId(tipoActividadId);
            request.setOrganizadorId("org-123");

            // Act
            ActividadRequest result = mapper.mapToActividadRequest(request);

            // Assert
            assertNotNull(result);
            assertEquals(estadoMock, result.getEstado());
            assertEquals(tipoMock, result.getTipoActividad());
        }

        @Test
        @DisplayName("Debería manejar request con todos los campos opcionales null")
        void debeManejarRequestConCamposOpcionalesNull() {
            // Arrange
            String estadoId = "estado-123";
            String tipoActividadId = "tipo-456";

            EstadoActividadDomainEntity estadoMock = new EstadoActividadDomainEntity();
            TipoActividadDomainEntity tipoMock = new TipoActividadDomainEntity();

            when(estadoActividadRepository.findById(estadoId)).thenReturn(Optional.of(estadoMock));
            when(tipoActividadRepository.findById(tipoActividadId)).thenReturn(Optional.of(tipoMock));

            CrearActividadRequest request = new CrearActividadRequest();
            request.setTitulo(null);
            request.setDescripcion(null);
            request.setFechaInicio(null);
            request.setFechaFin(null);
            request.setEstadoId(estadoId);
            request.setTipoActividadId(tipoActividadId);
            request.setOrganizadorId("org-123");
            request.setUbicacion(null);

            // Act
            ActividadRequest result = mapper.mapToActividadRequest(request);

            // Assert
            assertNotNull(result);
            assertNull(result.getTitulo());
            assertNull(result.getDescripcion());
            assertNull(result.getFechaInicio());
            assertNull(result.getFechaFin());
            assertNull(result.getUbicacion());
            assertNotNull(result.getEstado());
            assertNotNull(result.getTipoActividad());
        }
    }
}