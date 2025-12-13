package com.zentry.sigea.module_notificaciones.infrastructure.database.mappers;

import com.zentry.sigea.module_notificaciones.core.entities.*;
import com.zentry.sigea.module_notificaciones.infrastructure.database.entities.*;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionMapperTest {

    @Test
    void toEntity_ok() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(UUID.randomUUID());

        ActividadEntity actividad = new ActividadEntity();
        actividad.setId(UUID.randomUUID());

        TipoNotificacionDomainEntity tipo = new TipoNotificacionDomainEntity();
        tipo.setCodigo("CERTIFICADO");

        EstadoNotificacionDomainEntity estado = new EstadoNotificacionDomainEntity();
        estado.setCodigo("ENVIADA");

        NotificacionDomainEntity domain = new NotificacionDomainEntity();
        domain.setMensaje("Mensaje test");
        domain.setFechaEnvio(LocalDateTime.now());
        domain.setTipoNotificacion(tipo);
        domain.setEstadoNotificacion(estado);
        domain.setCanal(CanalNotificacion.SISTEMA);
        domain.setCreatedAt(LocalDateTime.now());
        domain.setUpdatedAt(LocalDateTime.now());

        NotificacionEntity entity =
            NotificacionMapper.toEntity(domain, usuario, actividad);

        assertNotNull(entity);
        assertEquals("Mensaje test", entity.getMensaje());
        assertEquals("SISTEMA", entity.getCanal());
        assertNotNull(entity.getTipoNotificacion());
        assertNotNull(entity.getEstadoNotificacion());
        assertEquals(usuario, entity.getUsuario());
        assertEquals(actividad, entity.getActividad());
    }

    @Test
    void toDomain_ok() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(UUID.randomUUID());

        NotificacionEntity entity = new NotificacionEntity();
        entity.setId(UUID.randomUUID());
        entity.setUsuario(usuario);
        entity.setMensaje("Mensaje dominio");
        entity.setCanal("SISTEMA");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        NotificacionDomainEntity domain =
            NotificacionMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("Mensaje dominio", domain.getMensaje());
        assertEquals(CanalNotificacion.SISTEMA, domain.getCanal());
        assertEquals(usuario.getId().toString(), domain.getUsuarioId());
    }

    @Test
    void toEntity_null() {
        assertNull(NotificacionMapper.toEntity(null, null, null));
    }

    @Test
    void toDomain_null() {
        assertNull(NotificacionMapper.toDomain(null));
    }
}
