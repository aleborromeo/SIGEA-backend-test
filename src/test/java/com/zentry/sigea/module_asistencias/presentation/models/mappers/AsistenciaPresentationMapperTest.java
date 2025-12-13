package com.zentry.sigea.module_asistencias.presentation.models.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.presentation.models.requestDTO.RegistrarAsistenciaRequest;
import com.zentry.sigea.module_asistencias.presentation.models.responseDTO.AsistenciaResponse;

class AsistenciaPresentationMapperTest {

    @Test
    void requestToDomain_retornaNull_siRequestEsNull() {
        assertNull(AsistenciaPresentationMapper.requestToDomain(null));
    }

    @Test
    void requestToDomain_lanzaNullPointerException_porOptionalOfNull() {
        RegistrarAsistenciaRequest request = mock(RegistrarAsistenciaRequest.class);
        when(request.getSesionId()).thenReturn("ses-1");
        when(request.getInscripcionId()).thenReturn("ins-1");
        when(request.getPresente()).thenReturn(true);

        assertThrows(NullPointerException.class,
            () -> AsistenciaPresentationMapper.requestToDomain(request)
        );
    }

    @Test
    void domainToResponse_retornaNull_siDomainEsNull() {
        assertNull(AsistenciaPresentationMapper.domainToResponse(null));
    }

    @Test
    void domainToResponse_mapeaCamposCorrectamente() {
        AsistenciaDomainEntity domain = mock(AsistenciaDomainEntity.class);

        LocalDateTime now = LocalDateTime.now();
        when(domain.getId()).thenReturn("a-1");
        when(domain.getSesionId()).thenReturn("ses-1");
        when(domain.getInscripcionId()).thenReturn("ins-1");
        when(domain.getPresente()).thenReturn(true);
        when(domain.getRegistradoEn()).thenReturn(now);

        AsistenciaResponse resp = AsistenciaPresentationMapper.domainToResponse(domain);

        assertNotNull(resp);
        assertEquals("a-1", resp.getId());
        assertEquals("ses-1", resp.getSesionId());
        assertEquals("ins-1", resp.getInscripcionId());
        assertTrue(resp.getPresente());
        assertEquals(now, resp.getRegistradoEn());
    }
}
