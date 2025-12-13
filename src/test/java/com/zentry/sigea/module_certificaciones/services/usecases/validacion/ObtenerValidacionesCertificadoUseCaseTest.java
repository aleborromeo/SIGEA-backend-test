package com.zentry.sigea.module_certificaciones.services.usecases.validacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_certificaciones.core.entities.CertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.EstadoCertificadoDomainEntity;
import com.zentry.sigea.module_certificaciones.core.entities.ValidacionDomainEntity;
import com.zentry.sigea.module_certificaciones.core.repositories.ICertificadoRepository;
import com.zentry.sigea.module_certificaciones.core.repositories.IValidacionRepository;

@ExtendWith(MockitoExtension.class)
class ObtenerValidacionesCertificadoUseCaseTest {

    @Mock
    private IValidacionRepository validacionRepository;

    @Mock
    private ICertificadoRepository certificadoRepository;

    @InjectMocks
    private ObtenerValidacionesCertificadoUseCase useCase;

    @Test
    void obtenerValidaciones_ok() {
        // Arrange
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "A1",
                "CERT-1",
                EstadoCertificadoDomainEntity.emitido()
            );
        certificado.setIdCertificado("ID-CERT-1");

        ValidacionDomainEntity v1 =
            ValidacionDomainEntity.crearAprobada(
                "ID-CERT-1",
                "QR",
                "Validación correcta"
            );

        ValidacionDomainEntity v2 =
            ValidacionDomainEntity.crearAprobada(
                "ID-CERT-1",
                "HASH",
                "Hash válido"
            );

        when(certificadoRepository.findByCodigoValidacion("CERT-1"))
            .thenReturn(Optional.of(certificado));

        when(validacionRepository.findByCertificadoId("ID-CERT-1"))
            .thenReturn(List.of(v1, v2));

        // Act
        List<ValidacionDomainEntity> result =
            useCase.execute("CERT-1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).esAprobada());
        assertTrue(result.get(1).esAprobada());

        verify(certificadoRepository).findByCodigoValidacion("CERT-1");
        verify(validacionRepository).findByCertificadoId("ID-CERT-1");
    }

    @Test
    void obtenerValidaciones_codigoNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> useCase.execute(null)
        );
    }

    @Test
    void obtenerValidaciones_codigoVacio() {
        assertThrows(
            IllegalArgumentException.class,
            () -> useCase.execute("   ")
        );
    }

    @Test
    void obtenerValidaciones_certificadoNoExiste() {
        when(certificadoRepository.findByCodigoValidacion("CERT-X"))
            .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> useCase.execute("CERT-X")
        );

        assertTrue(ex.getMessage().contains("No se encontró un certificado"));

        verify(certificadoRepository).findByCodigoValidacion("CERT-X");
        verify(validacionRepository, never()).findByCertificadoId(any());
    }

    @Test
    void obtenerValidaciones_sinValidaciones() {
        CertificadoDomainEntity certificado =
            CertificadoDomainEntity.create(
                "A1",
                "CERT-2",
                EstadoCertificadoDomainEntity.emitido()
            );
        certificado.setIdCertificado("ID-CERT-2");

        when(certificadoRepository.findByCodigoValidacion("CERT-2"))
            .thenReturn(Optional.of(certificado));

        when(validacionRepository.findByCertificadoId("ID-CERT-2"))
            .thenReturn(List.of());

        List<ValidacionDomainEntity> result =
            useCase.execute("CERT-2");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
