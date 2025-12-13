package com.zentry.sigea.module_certificaciones.services.usecases.validacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.core.entities.*;
import com.zentry.sigea.module_certificaciones.core.repositories.*;
import com.zentry.sigea.module_certificaciones.presentation.models.requestDTO.ValidarCertificadoRequest;

@ExtendWith(MockitoExtension.class)
class ValidarCertificadoUseCaseTest {

    @Mock
    private IValidacionRepository validacionRepository;

    @Mock
    private ICertificadoRepository certificadoRepository;

    @Mock
    private ITipoValidadorRepository tipoValidadorRepository;

    @InjectMocks
    private ValidarCertificadoUseCase useCase;

    @Test
    void validarCertificado_ok() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "ASIS-1", "CERT-1", EstadoCertificadoDomainEntity.emitido()
            );

        TipoValidadorDomainEntity tipo = TipoValidadorDomainEntity.qr();

        ValidarCertificadoRequest request = new ValidarCertificadoRequest();
        request.setCodigoValidacion("CERT-1");
        request.setTipoValidador("QR");

        when(certificadoRepository.findByCodigoValidacion("CERT-1"))
            .thenReturn(Optional.of(certificado));
        when(tipoValidadorRepository.findByCodigo("QR"))
            .thenReturn(Optional.of(tipo));
        when(validacionRepository.findByCertificadoIdAndTipoValidadorId(any(), any()))
            .thenReturn(Optional.empty());
        when(validacionRepository.save(any()))
            .thenAnswer(i -> i.getArgument(0));

        ValidacionDomainEntity result = useCase.execute(request);

        assertNotNull(result);
        assertTrue(result.esAprobada());
    }
}
