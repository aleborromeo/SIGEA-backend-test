package com.zentry.sigea.module_certificaciones.infrastructure.database.entities;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ValidacionEntity - Unit Tests")
class ValidacionEntityTest {

    @Test
    @DisplayName("Defaults - resultado por defecto debe ser APROBADO")
    void defaults_resultadoPorDefecto() {
        ValidacionEntity entity = new ValidacionEntity();
        assertThat(entity.getResultado()).isEqualTo("APROBADO");
        assertThat(entity.getFechaValidacion()).isNull();
    }

    @Test
    @DisplayName("@PrePersist - Debe setear fechaValidacion")
    void prePersist_debeSetearFechaValidacion() throws Exception {
        ValidacionEntity entity = new ValidacionEntity();

        invoke(entity, "onCreate");

        assertThat(entity.getFechaValidacion()).isNotNull();
    }

    private static void invoke(Object target, String methodName) throws Exception {
        Method m = target.getClass().getDeclaredMethod(methodName);
        m.setAccessible(true);
        m.invoke(target);
    }
}
