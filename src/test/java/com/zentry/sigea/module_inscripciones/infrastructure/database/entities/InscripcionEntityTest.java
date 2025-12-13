package com.zentry.sigea.module_inscripciones.infrastructure.database.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;

class InscripcionEntityTest {

    @Test
    void testGettersAndSetters() {
        InscripcionEntity inscripcion = new InscripcionEntity();

        UUID id = UUID.randomUUID();
        LocalDate fecha = LocalDate.of(2025, 1, 10);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(UUID.randomUUID());

        ActividadEntity actividad = new ActividadEntity();
        actividad.setId(UUID.randomUUID());

        EstadoInscripcionEntity estado = new EstadoInscripcionEntity();
        estado.setId(UUID.randomUUID());
        estado.setCodigo("PENDIENTE");

        inscripcion.setId(id);
        inscripcion.setFechaInscripcion(fecha);
        inscripcion.setUsuario(usuario);
        inscripcion.setActividad(actividad);
        inscripcion.setEstadoInscripcion(estado);

        assertEquals(id, inscripcion.getId());
        assertEquals(fecha, inscripcion.getFechaInscripcion());
        assertSame(usuario, inscripcion.getUsuario());
        assertSame(actividad, inscripcion.getActividad());
        assertSame(estado, inscripcion.getEstadoInscripcion());
    }

    @Test
    void testNoArgsConstructorDefaults() {
        InscripcionEntity inscripcion = new InscripcionEntity();

        assertNull(inscripcion.getId());
        assertNull(inscripcion.getFechaInscripcion());
        assertNull(inscripcion.getUsuario());
        assertNull(inscripcion.getActividad());
        assertNull(inscripcion.getEstadoInscripcion());
    }
}
