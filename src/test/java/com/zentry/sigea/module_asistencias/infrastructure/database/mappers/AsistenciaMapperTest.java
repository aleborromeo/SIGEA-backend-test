package com.zentry.sigea.module_asistencias.infrastructure.database.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_asistencias.core.entities.AsistenciaDomainEntity;
import com.zentry.sigea.module_asistencias.infrastructure.database.entities.AsistenciaEntity;
import com.zentry.sigea.module_inscripciones.infrastructure.database.entities.InscripcionEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;

class AsistenciaMapperTest {

    @Test
    void toEntity_mapeaCampos() {
        AsistenciaDomainEntity domain = mockDomain("ins-1", "ses-1", true, LocalDateTime.now());

        SesionEntity ses = new SesionEntity();
        InscripcionEntity ins = new InscripcionEntity();

        AsistenciaEntity entity = AsistenciaMapper.toEntity(domain, ses, ins);

        assertSame(ses, entity.getSesion());
        assertSame(ins, entity.getInscripcion());
        assertEquals(true, entity.getPresente());
        assertNotNull(entity.getRegistradoEn());
    }

    @Test
    void toDomain_reconstruyeIds() {
        UUID asisId = UUID.randomUUID();
        UUID sesId = UUID.randomUUID();
        UUID insId = UUID.randomUUID();

        SesionEntity ses = new SesionEntity();
        ses.setId(sesId);
        InscripcionEntity ins = new InscripcionEntity();
        ins.setId(insId);

        AsistenciaEntity entity = new AsistenciaEntity();
        entity.setId(asisId);
        entity.setSesion(ses);
        entity.setInscripcion(ins);
        entity.setPresente(true);
        entity.setRegistradoEn(LocalDateTime.now());

        AsistenciaDomainEntity domain = AsistenciaMapper.toDomain(entity);

        assertEquals(asisId.toString(), domain.getId());
        assertEquals(sesId.toString(), domain.getSesionId());
        assertEquals(insId.toString(), domain.getInscripcionId());
        assertTrue(domain.getPresente());
        assertNotNull(domain.getRegistradoEn());
    }

    private AsistenciaDomainEntity mockDomain(String insId, String sesId, boolean presente, LocalDateTime reg) {
        // Si tu domain tiene constructores distintos, ajusta acá:
        AsistenciaDomainEntity d = org.mockito.Mockito.mock(AsistenciaDomainEntity.class);
        org.mockito.Mockito.when(d.getInscripcionId()).thenReturn(insId);
        org.mockito.Mockito.when(d.getSesionId()).thenReturn(sesId);
        org.mockito.Mockito.when(d.getPresente()).thenReturn(presente);
        org.mockito.Mockito.when(d.getRegistradoEn()).thenReturn(reg);
        return d;
    }
}
