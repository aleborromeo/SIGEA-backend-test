package com.zentry.sigea.module_certificaciones.infrastructure.database.entities;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CertificadoEntity - Unit Tests")
class CertificadoEntityTest {

    @Test
    @DisplayName("Defaults - Debe iniciar con nulls y sin auto-fechas hasta PrePersist")
    void defaults_debeIniciarConNulls() {
        CertificadoEntity entity = new CertificadoEntity();

        assertAll(
            () -> assertThat(entity.getIdCertificado()).isNull(),
            () -> assertThat(entity.getAsistenciaId()).isNull(),
            () -> assertThat(entity.getCodigoValidacion()).isNull(),
            () -> assertThat(entity.getFechaEmision()).isNull(),
            () -> assertThat(entity.getEstado()).isNull(),
            () -> assertThat(entity.getUrlPdf()).isNull(),
            () -> assertThat(entity.getCreatedAt()).isNull(),
            () -> assertThat(entity.getUpdatedAt()).isNull()
        );
    }

    @Test
    @DisplayName("@PrePersist - Debe setear fechaEmision, createdAt y updatedAt")
    void prePersist_debeSetearFechas() throws Exception {
        CertificadoEntity entity = new CertificadoEntity();
        entity.setAsistenciaId(UUID.randomUUID());
        entity.setCodigoValidacion("COD-123");

        // Estado obligatorio por la entidad (nullable=false). A nivel unit test basta setearlo.
        EstadoCertificadoEntity estado = new EstadoCertificadoEntity();
        estado.setCodigo("EMITIDO");
        estado.setEtiqueta("Emitido");
        entity.setEstado(estado);

        invoke(entity, "onCreate");

        assertAll(
            () -> assertThat(entity.getFechaEmision()).isNotNull(),
            () -> assertThat(entity.getCreatedAt()).isNotNull(),
            () -> assertThat(entity.getUpdatedAt()).isNotNull(),
            () -> assertThat(entity.getUpdatedAt()).isEqualTo(entity.getCreatedAt())
        );
    }

    @Test
    @DisplayName("@PreUpdate - Debe actualizar updatedAt")
    void preUpdate_debeActualizarUpdatedAt() throws Exception {
        CertificadoEntity entity = new CertificadoEntity();
        invoke(entity, "onCreate");

        LocalDateTime before = entity.getUpdatedAt();
        // esperamos un pelín para asegurar cambio de tiempo
        Thread.sleep(5);

        invoke(entity, "onUpdate");

        assertAll(
            () -> assertThat(entity.getUpdatedAt()).isNotNull(),
            () -> assertThat(entity.getUpdatedAt()).isAfter(before)
        );
    }

    private static void invoke(Object target, String methodName) throws Exception {
        Method m = target.getClass().getDeclaredMethod(methodName);
        m.setAccessible(true);
        m.invoke(target);
    }
}
