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
class ReactivarCertificadoUseCaseTest {

    @Mock
    private ICertificadoRepository certificadoRepository;

    @Mock
    private IEstadoCertificadoRepository estadoCertificadoRepository;

    @InjectMocks
    private ReactivarCertificadoUseCase useCase;

    @Test
    void reactivarCertificado_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "A1", "CERT-1", EstadoCertificadoDomainEntity.revocado()
            );

        when(certificadoRepository.findByCodigoValidacion("CERT-1"))
            .thenReturn(Optional.of(certificado));
        when(estadoCertificadoRepository.findByCodigo("EMITIDO"))
            .thenReturn(Optional.of(EstadoCertificadoDomainEntity.emitido()));
        when(certificadoRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

        CertificadoDomainEntity result =
            useCase.execute("CERT-1");

        assertTrue(result.estaEmitido());
    }

    @Test
    void reactivarCertificado_noRevocado() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "A1", "CERT-1", EstadoCertificadoDomainEntity.emitido()
            );

        when(certificadoRepository.findByCodigoValidacion("CERT-1"))
            .thenReturn(Optional.of(certificado));

        assertThrows(IllegalStateException.class,
            () -> useCase.execute("CERT-1"));
    }
}
