package com.zentry.sigea.module_sesiones.infrastructure.database.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity;
import com.zentry.sigea.module_sesiones.infrastructure.database.entities.SesionEntity.Modalidad;
import com.zentry.sigea.module_sesiones.infrastructure.database.mappers.SesionMapper;
import com.zentry.sigea.module_sesiones.core.entities.SesionDomainEntity;

class SesionMapperTest {

    @Test
    void toDomain_convierteCorrectamente() {
        UUID sesionId = UUID.randomUUID();
        UUID actividadId = UUID.randomUUID();

        ActividadEntity actividad = new ActividadEntity();
        actividad.setId(actividadId);

        SesionEntity entity = new SesionEntity();
        entity.setId(sesionId);
        entity.setActividad(actividad);
        entity.setTitulo("Sesión Entity");
        entity.setDescripcion("Descripción Entity");
        entity.setFechaSesion(LocalDateTime.now().plusDays(2));
        entity.setHoraInicio(LocalTime.of(10, 0));
        entity.setHoraFin(LocalTime.of(12, 0));
        entity.setPonente("Ponente Entity");
        entity.setModalidad(Modalidad.VIRTUAL);
        entity.setLugarSesion("Virtual");
        entity.setLinkVirtual("https://meet.test");
        entity.setOrden("2");

        // ✅ En vez de entity.onCreate()
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        SesionDomainEntity domain = SesionMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(sesionId.toString(), domain.getId());
        assertEquals(actividadId.toString(), domain.getActividadId());
        assertEquals("Sesión Entity", domain.getTitulo());
        assertEquals("Descripción Entity", domain.getDescripcion());
        assertEquals(Modalidad.VIRTUAL, domain.getModalidad());
        assertNotNull(domain.getCreatedAt());
        assertNotNull(domain.getUpdatedAt());
    }
}
