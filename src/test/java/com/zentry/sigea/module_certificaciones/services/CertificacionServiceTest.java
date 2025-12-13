package com.zentry.sigea.module_certificaciones.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.ValidacionResponse;
import com.zentry.sigea.module_certificaciones.services.interfaces.ICertificadoService;
import com.zentry.sigea.module_certificaciones.services.interfaces.IValidacionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("CertificacionService - Unit Tests")
class CertificacionServiceTest {

    @Mock
    private ICertificadoService certificadoService;

    @Mock
    private IValidacionService validacionService;

    private CertificacionService certificacionService;

    @BeforeEach
    void setUp() {
        certificacionService = new CertificacionService(certificadoService, validacionService);
    }

    @Test
    @DisplayName("crearCertificado - delega correctamente")
    void crearCertificado_ok() {
        CrearCertificadoRequest request = new CrearCertificadoRequest();
        CertificadoResponse response = new CertificadoResponse();
        response.setCodigoValidacion("ABC123");

        when(certificadoService.crearCertificado(request)).thenReturn(response);

        CertificadoResponse result = certificacionService.crearCertificado(request);

        assertThat(result).isEqualTo(response);
        verify(certificadoService).crearCertificado(request);
    }

    @Test
    @DisplayName("crearCertificadosMasivos - delega correctamente")
    void crearCertificadosMasivos_ok() {
        List<String> actividades = List.of("ACT1", "ACT2");
        Map<String, Boolean> resultado = Map.of("ACT1", true);

        when(certificadoService.crearCertificadosMasivos(actividades)).thenReturn(resultado);

        Map<String, Boolean> result = certificacionService.crearCertificadosMasivos(actividades);

        assertThat(result).containsEntry("ACT1", true);
        verify(certificadoService).crearCertificadosMasivos(actividades);
    }

    @Test
    @DisplayName("buscarCertificadoPorCodigo - retorna Optional")
    void buscarCertificadoPorCodigo_ok() {
        CertificadoResponse response = new CertificadoResponse();
        when(certificadoService.buscarCertificadoPorCodigo("COD"))
            .thenReturn(Optional.of(response));

        Optional<CertificadoResponse> result =
            certificacionService.buscarCertificadoPorCodigo("COD");

        assertThat(result).isPresent();
        verify(certificadoService).buscarCertificadoPorCodigo("COD");
    }

    @Test
    @DisplayName("validarCertificado - delega a validacionService")
    void validarCertificado_ok() {
        ValidarCertificadoRequest request = new ValidarCertificadoRequest();
        ValidacionResponse response = new ValidacionResponse();

        when(validacionService.validarCertificado(request)).thenReturn(response);

        ValidacionResponse result = certificacionService.validarCertificado(request);

        assertThat(result).isEqualTo(response);
        verify(validacionService).validarCertificado(request);
    }

    @Test
    @DisplayName("propaga excepción correctamente")
    void propagaExcepcion() {
        CrearCertificadoRequest request = new CrearCertificadoRequest();
        when(certificadoService.crearCertificado(request))
            .thenThrow(new RuntimeException("Error"));

        assertThatThrownBy(() -> certificacionService.crearCertificado(request))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Error");
    }
}
