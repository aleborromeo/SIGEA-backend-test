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
import com.zentry.sigea.module_certificaciones.core.repositories.IEstadoCertificadoRepository;

@ExtendWith(MockitoExtension.class)
class RevocarCertificadoUseCaseTest {

    @Mock
    private ICertificadoRepository certificadoRepository;

    @Mock
    private IEstadoCertificadoRepository estadoCertificadoRepository;

    @InjectMocks
    private RevocarCertificadoUseCase useCase;

    @Test
    void revocar_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "ASIS-1", "CERT-1", EstadoCertificadoDomainEntity.emitido()
            );

        when(certificadoRepository.findByCodigoValidacion("CERT-1"))
            .thenReturn(Optional.of(certificado));
        when(estadoCertificadoRepository.findByCodigo("REVOCADO"))
            .thenReturn(Optional.of(EstadoCertificadoDomainEntity.revocado()));
        when(certificadoRepository.save(any())).thenReturn(certificado);

        CertificadoDomainEntity result =
            useCase.execute("CERT-1", "motivo");

        assertTrue(result.estaRevocado());
    }
}
