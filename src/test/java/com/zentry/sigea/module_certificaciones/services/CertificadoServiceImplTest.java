package com.zentry.sigea.module_certificaciones.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.CrearCertificadoRequest;
import com.zentry.sigea.module_certificaciones.presentation.models.responseDTO.CertificadoResponse;
import com.zentry.sigea.module_certificaciones.services.usecases.certificado.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CertificadoServiceImpl - Unit Tests")
class CertificadoServiceImplTest {

    @Mock private CrearCertificadoUseCase crearCertificadoUseCase;
    @Mock private CrearCertificadosMasivosUseCase crearCertificadosMasivosUseCase;
    @Mock private ObtenerCertificadoPorCodigoUseCase obtenerCertificadoPorCodigoUseCase;
    @Mock private RevocarCertificadoUseCase revocarCertificadoUseCase;
    @Mock private ReactivarCertificadoUseCase reactivarCertificadoUseCase;
    @Mock private GenerarPdfCertificadoUseCase generarPdfCertificadoUseCase;
    @Mock private IEstadoCertificadoRepository estadoRepo;

    private CertificadoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CertificadoServiceImpl(
            crearCertificadoUseCase,
            crearCertificadosMasivosUseCase,
            obtenerCertificadoPorCodigoUseCase,
            revocarCertificadoUseCase,
            reactivarCertificadoUseCase,
            generarPdfCertificadoUseCase,
            estadoRepo,
            null
        );
    }

    @Test
    @DisplayName("crearCertificado - flujo exitoso")
    void crearCertificado_ok() {
        CrearCertificadoRequest request = new CrearCertificadoRequest();
        request.setAsistenciaId("123");

        CertificadoDomainEntity domain = CertificadoDomainEntity.create(
            "123", "COD123", EstadoCertificadoDomainEntity.emitido()
        );
        domain.setFechaEmision(LocalDate.now());

        when(crearCertificadoUseCase.execute(request)).thenReturn(domain);

        CertificadoResponse response = service.crearCertificado(request);

        assertThat(response.getCodigoValidacion()).isEqualTo("COD123");
        assertThat(response.getEstado()).isEqualTo("EMITIDO");
    }

    @Test
    @DisplayName("buscarCertificadoPorCodigo - retorna vacío")
    void buscarCertificadoPorCodigo_empty() {
        when(obtenerCertificadoPorCodigoUseCase.execute("COD"))
            .thenReturn(Optional.empty());

        Optional<CertificadoResponse> result =
            service.buscarCertificadoPorCodigo("COD");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("generarPdfCertificado - retorna URL")
    void generarPdf_ok() {
        when(generarPdfCertificadoUseCase.execute("COD"))
            .thenReturn("http://pdf");

        String url = service.generarPdfCertificado("COD");

        assertThat(url).isEqualTo("http://pdf");
    }

    @Test
    @DisplayName("revocarCertificado - delega correctamente")
    void revocar_ok() {
        CertificadoDomainEntity domain = new CertificadoDomainEntity();
        when(revocarCertificadoUseCase.execute("COD", "MOTIVO"))
            .thenReturn(domain);

        assertThat(service.revocarCertificado("COD", "MOTIVO")).isNotNull();
    }
}
