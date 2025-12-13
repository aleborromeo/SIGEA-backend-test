package com.zentry.sigea.module_certificaciones.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.ValidacionResponse;
import com.zentry.sigea.module_certificaciones.services.usecases.validacion.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidacionServiceImpl - Unit Tests")
class ValidacionServiceImplTest {

    @Mock private ValidarCertificadoUseCase validarCertificadoUseCase;
    @Mock private ObtenerValidacionesCertificadoUseCase obtenerValidacionesCertificadoUseCase;

    private ValidacionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ValidacionServiceImpl(
            validarCertificadoUseCase,
            obtenerValidacionesCertificadoUseCase
        );
    }

    @Test
    @DisplayName("validarCertificado - flujo correcto")
    void validarCertificado_ok() {
        ValidarCertificadoRequest request = new ValidarCertificadoRequest();
        request.setCodigoValidacion("COD");
        request.setTipoValidador("QR");

        ValidacionDomainEntity domain =
            ValidacionDomainEntity.crearAprobada("COD", "QR", "OK");

        when(validarCertificadoUseCase.execute(request)).thenReturn(domain);

        ValidacionResponse response = service.validarCertificado(request);

        assertThat(response.getResultado()).isEqualTo("APROBADO");
    }

    @Test
    @DisplayName("obtenerValidacionesCertificado - lista")
    void obtenerValidaciones_ok() {
        ValidacionDomainEntity domain =
            ValidacionDomainEntity.crearAprobada("COD", "QR", "OK");

        when(obtenerValidacionesCertificadoUseCase.execute("COD"))
            .thenReturn(List.of(domain));

        List<ValidacionResponse> result =
            service.obtenerValidacionesCertificado("COD");

        assertThat(result).hasSize(1);
    }
}
