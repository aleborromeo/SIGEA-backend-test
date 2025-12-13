package com.zentry.sigea.module_certificaciones.services.usecases.certificado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;

@ExtendWith(MockitoExtension.class)
class ObtenerCertificadoPorCodigoUseCaseTest {

    @Mock
    private ICertificadoRepository certificadoRepository;

    @InjectMocks
    private ObtenerCertificadoPorCodigoUseCase useCase;

    @Test
    void obtenerCertificado_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "A1", "CERT-1", EstadoCertificadoDomainEntity.emitido()
            );

        when(certificadoRepository.findByCodigoValidacion("CERT-1"))
            .thenReturn(Optional.of(certificado));

        Optional<CertificadoDomainEntity> result =
            useCase.execute("CERT-1");

        assertTrue(result.isPresent());
    }

    @Test
    void obtenerCertificado_codigoVacio() {
        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(" "));
    }

    @Test
    void obtenerCertificado_noExiste() {
        when(certificadoRepository.findByCodigoValidacion("X"))
            .thenReturn(Optional.empty());

        Optional<CertificadoDomainEntity> result =
            useCase.execute("X");

        assertTrue(result.isEmpty());
    }
}
