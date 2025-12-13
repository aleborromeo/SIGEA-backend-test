package com.zentry.sigea.module_inscripciones.presentation.models.responseDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.zentry.sigea.module_inscripciones.core.entities.EstadoInscripcionDomainEntity;

class EstadoInscripcionResponseTest {

    @Test
    void emptyConstructorAndSetters_workCorrectly() {
        EstadoInscripcionResponse response = new EstadoInscripcionResponse();

        response.setId("123");
        response.setCodigo("PEN");
        response.setEtiqueta("Pendiente");

        assertEquals("123", response.getId());
        assertEquals("PEN", response.getCodigo());
        assertEquals("Pendiente", response.getEtiqueta());
    }

    @Test
    void fullConstructor_setsAllFields() {
        EstadoInscripcionResponse response =
            new EstadoInscripcionResponse("456", "CONF", "Confirmada");

        assertEquals("456", response.getId());
        assertEquals("CONF", response.getCodigo());
        assertEquals("Confirmada", response.getEtiqueta());
    }

    @Test
    void fromEntity_buildsResponseFromDomainEntity() {
        // Arrange: dominio
        EstadoInscripcionDomainEntity domain =
            EstadoInscripcionDomainEntity.create("CAN", "Cancelada");
        domain.setId("789");

        // Act
        EstadoInscripcionResponse response = EstadoInscripcionResponse.fromEntity(domain);

        // Assert
        assertEquals("789", response.getId());
        assertEquals("CAN", response.getCodigo());
        assertEquals("Cancelada", response.getEtiqueta());
    }
}
