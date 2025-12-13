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
class GenerarPdfCertificadoUseCaseTest {

    @Mock
    private ICertificadoRepository certificadoRepository;

    @InjectMocks
    private GenerarPdfCertificadoUseCase useCase;

    @Test
    void generarPdf_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "ASIS-1", "CERT-123", EstadoCertificadoDomainEntity.emitido()
            );

        when(certificadoRepository.findByCodigoValidacion("CERT-123"))
            .thenReturn(Optional.of(certificado));
        when(certificadoRepository.save(any())).thenReturn(certificado);

        String url = useCase.execute("CERT-123");

        assertNotNull(url);
        assertTrue(url.contains("CERT-123"));
    }

    @Test
    void generarPdf_certificadoNoExiste() {
        when(certificadoRepository.findByCodigoValidacion("X"))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> useCase.execute("X"));
    }
}
