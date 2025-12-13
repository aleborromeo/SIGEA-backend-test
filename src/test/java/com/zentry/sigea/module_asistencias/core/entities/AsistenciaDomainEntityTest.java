package com.zentry.sigea.module_asistencias.core.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para AsistenciaDomainEntity")
class AsistenciaDomainEntityTest {

    @Test
    @DisplayName("Crear asistencia con registradoEn presente - Debe usar fecha proporcionada")
    void create_ConRegistradoEnPresente_DebeUsarFechaProporcionada() {
        // Arrange
        String sesionId = "sesion-123";
        String inscripcionId = "inscripcion-456";
        Boolean presente = true;
        LocalDateTime fechaRegistro = LocalDateTime.of(2024, 1, 15, 10, 30, 0);

        // Act
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            sesionId, 
            inscripcionId, 
            presente, 
            Optional.of(fechaRegistro)
        );

        // Assert
        assertAll("Verificar asistencia creada con fecha específica",
            () -> assertThat(asistencia.getSesionId()).isEqualTo(sesionId),
            () -> assertThat(asistencia.getInscripcionId()).isEqualTo(inscripcionId),
            () -> assertThat(asistencia.getPresente()).isTrue(),
            () -> assertThat(asistencia.getRegistradoEn()).isEqualTo(fechaRegistro),
            () -> assertThat(asistencia.getId()).isNull()
        );
    }

    @Test
    @DisplayName("Crear asistencia sin registradoEn - Debe usar fecha actual")
    void create_SinRegistradoEn_DebeUsarFechaActual() {
        // Arrange
        String sesionId = "sesion-789";
        String inscripcionId = "inscripcion-101";
        Boolean presente = false;
        LocalDateTime antes = LocalDateTime.now();

        // Act
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            sesionId, 
            inscripcionId, 
            presente, 
            Optional.empty()
        );

        // Assert
        assertAll("Verificar asistencia con fecha automática",
            () -> assertThat(asistencia.getSesionId()).isEqualTo(sesionId),
            () -> assertThat(asistencia.getInscripcionId()).isEqualTo(inscripcionId),
            () -> assertThat(asistencia.getPresente()).isFalse(),
            () -> assertThat(asistencia.getRegistradoEn()).isNotNull(),
            () -> assertThat(asistencia.getRegistradoEn()).isAfterOrEqualTo(antes),
            () -> assertThat(asistencia.getId()).isNull()
        );
    }

    @Test
    @DisplayName("Reconstruct - Debe crear asistencia con todos los datos incluyendo ID")
    void reconstruct_DebeCrearAsistenciaCompleta() {
        // Arrange
        String id = "asistencia-999";
        String sesionId = "sesion-111";
        String inscripcionId = "inscripcion-222";
        Boolean presente = true;
        LocalDateTime registradoEn = LocalDateTime.of(2024, 2, 20, 14, 45, 30);

        // Act
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.reconstruct(
            id, 
            sesionId, 
            inscripcionId, 
            presente, 
            registradoEn
        );

        // Assert
        assertAll("Verificar asistencia reconstruida",
            () -> assertThat(asistencia.getId()).isEqualTo(id),
            () -> assertThat(asistencia.getSesionId()).isEqualTo(sesionId),
            () -> assertThat(asistencia.getInscripcionId()).isEqualTo(inscripcionId),
            () -> assertThat(asistencia.getPresente()).isTrue(),
            () -> assertThat(asistencia.getRegistradoEn()).isEqualTo(registradoEn)
        );
    }

    @Test
    @DisplayName("Marcar presente - Debe cambiar estado a true")
    void marcarPresente_DebeCambiarEstadoATrue() {
        // Arrange
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-1", 
            "inscripcion-1", 
            false, 
            Optional.empty()
        );

        // Act
        asistencia.marcarPresente();

        // Assert
        assertThat(asistencia.getPresente()).isTrue();
        assertThat(asistencia.estaPresente()).isTrue();
    }

    @Test
    @DisplayName("Marcar ausente - Debe cambiar estado a false")
    void marcarAusente_DebeCambiarEstadoAFalse() {
        // Arrange
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-2", 
            "inscripcion-2", 
            true, 
            Optional.empty()
        );

        // Act
        asistencia.marcarAusente();

        // Assert
        assertThat(asistencia.getPresente()).isFalse();
        assertThat(asistencia.estaPresente()).isFalse();
    }

    @Test
    @DisplayName("EstaPresente con presente=true - Debe retornar true")
    void estaPresente_ConPresenteTrue_DebeRetornarTrue() {
        // Arrange
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-3", 
            "inscripcion-3", 
            true, 
            Optional.empty()
        );

        // Act & Assert
        assertThat(asistencia.estaPresente()).isTrue();
    }

    @Test
    @DisplayName("EstaPresente con presente=false - Debe retornar false")
    void estaPresente_ConPresenteFalse_DebeRetornarFalse() {
        // Arrange
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-4", 
            "inscripcion-4", 
            false, 
            Optional.empty()
        );

        // Act & Assert
        assertThat(asistencia.estaPresente()).isFalse();
    }

    @Test
    @DisplayName("EstaPresente con presente=null - Debe retornar false")
    void estaPresente_ConPresenteNull_DebeRetornarFalse() {
        // Arrange
        AsistenciaDomainEntity asistencia = new AsistenciaDomainEntity();
        asistencia.setPresente(null);

        // Act & Assert
        assertThat(asistencia.estaPresente()).isFalse();
    }

    @ParameterizedTest
    @DisplayName("Crear con diferentes estados de presencia")
    @ValueSource(booleans = {true, false})
    void create_ConDiferentesEstados_DebeCrearCorrectamente(Boolean presente) {
        // Act
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-test", 
            "inscripcion-test", 
            presente, 
            Optional.empty()
        );

        // Assert
        assertThat(asistencia.getPresente()).isEqualTo(presente);
        assertThat(asistencia.estaPresente()).isEqualTo(presente);
    }

    @Test
    @DisplayName("Setters y Getters - Deben funcionar correctamente")
    void settersYGetters_DebenFuncionarCorrectamente() {
        // Arrange
        AsistenciaDomainEntity asistencia = new AsistenciaDomainEntity();
        String id = "asist-001";
        String sesionId = "sesion-001";
        String inscripcionId = "inscripcion-001";
        Boolean presente = true;
        LocalDateTime registradoEn = LocalDateTime.now();

        // Act
        asistencia.setId(id);
        asistencia.setSesionId(sesionId);
        asistencia.setInscripcionId(inscripcionId);
        asistencia.setPresente(presente);
        asistencia.setRegistradoEn(registradoEn);

        // Assert
        assertAll("Verificar getters",
            () -> assertEquals(id, asistencia.getId()),
            () -> assertEquals(sesionId, asistencia.getSesionId()),
            () -> assertEquals(inscripcionId, asistencia.getInscripcionId()),
            () -> assertEquals(presente, asistencia.getPresente()),
            () -> assertEquals(registradoEn, asistencia.getRegistradoEn())
        );
    }

    @Test
    @DisplayName("Cambiar de ausente a presente y viceversa")
    void cambiarEstadoMultiplesVeces_DebeActualizarCorrectamente() {
        // Arrange
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-5", 
            "inscripcion-5", 
            false, 
            Optional.empty()
        );

        // Act & Assert - Inicialmente ausente
        assertThat(asistencia.estaPresente()).isFalse();

        // Marcar presente
        asistencia.marcarPresente();
        assertThat(asistencia.estaPresente()).isTrue();

        // Marcar ausente nuevamente
        asistencia.marcarAusente();
        assertThat(asistencia.estaPresente()).isFalse();

        // Marcar presente otra vez
        asistencia.marcarPresente();
        assertThat(asistencia.estaPresente()).isTrue();
    }

    @Test
    @DisplayName("Crear múltiples asistencias - Cada una debe ser independiente")
    void crearMultiplesAsistencias_DebenSerIndependientes() {
        // Act
        AsistenciaDomainEntity asistencia1 = AsistenciaDomainEntity.create(
            "sesion-A", "inscripcion-A", true, Optional.empty()
        );
        AsistenciaDomainEntity asistencia2 = AsistenciaDomainEntity.create(
            "sesion-B", "inscripcion-B", false, Optional.empty()
        );

        // Assert
        assertThat(asistencia1.getSesionId()).isNotEqualTo(asistencia2.getSesionId());
        assertThat(asistencia1.getInscripcionId()).isNotEqualTo(asistencia2.getInscripcionId());
        assertThat(asistencia1.getPresente()).isNotEqualTo(asistencia2.getPresente());
    }

    @Test
    @DisplayName("Reconstruct con diferentes fechas - Debe mantener fechas específicas")
    void reconstruct_ConDiferentesFechas_DebeMantenerFechas() {
        // Arrange
        LocalDateTime fecha1 = LocalDateTime.of(2024, 1, 1, 8, 0, 0);
        LocalDateTime fecha2 = LocalDateTime.of(2024, 12, 31, 23, 59, 59);

        // Act
        AsistenciaDomainEntity asistencia1 = AsistenciaDomainEntity.reconstruct(
            "id-1", "sesion-1", "inscripcion-1", true, fecha1
        );
        AsistenciaDomainEntity asistencia2 = AsistenciaDomainEntity.reconstruct(
            "id-2", "sesion-2", "inscripcion-2", false, fecha2
        );

        // Assert
        assertThat(asistencia1.getRegistradoEn()).isEqualTo(fecha1);
        assertThat(asistencia2.getRegistradoEn()).isEqualTo(fecha2);
        assertThat(asistencia1.getRegistradoEn()).isBefore(asistencia2.getRegistradoEn());
    }

    @ParameterizedTest
    @DisplayName("Crear asistencias para diferentes sesiones")
    @CsvSource({
        "sesion-001, inscripcion-100, true",
        "sesion-002, inscripcion-200, false",
        "sesion-003, inscripcion-300, true",
        "sesion-004, inscripcion-400, false"
    })
    void create_ParaDiferentesSesiones_DebeCrearCorrectamente(
        String sesionId, String inscripcionId, Boolean presente
    ) {
        // Act
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            sesionId, 
            inscripcionId, 
            presente, 
            Optional.empty()
        );

        // Assert
        assertAll("Verificar asistencia",
            () -> assertThat(asistencia.getSesionId()).isEqualTo(sesionId),
            () -> assertThat(asistencia.getInscripcionId()).isEqualTo(inscripcionId),
            () -> assertThat(asistencia.getPresente()).isEqualTo(presente)
        );
    }

    @Test
    @DisplayName("Fecha de registro debe ser inmutable después de crear")
    void fechaRegistro_DebeSerInmutableDespuesDeCrea() {
        // Arrange
        LocalDateTime fechaOriginal = LocalDateTime.of(2024, 6, 15, 10, 0, 0);
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-X", 
            "inscripcion-X", 
            true, 
            Optional.of(fechaOriginal)
        );

        // Act - Cambiar estado no debe afectar la fecha
        asistencia.marcarAusente();
        asistencia.marcarPresente();

        // Assert
        assertThat(asistencia.getRegistradoEn()).isEqualTo(fechaOriginal);
    }

    @Test
    @DisplayName("ID null en asistencia recién creada")
    void idNull_EnAsistenciaRecienCreada() {
        // Act
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-Y", 
            "inscripcion-Y", 
            true, 
            Optional.empty()
        );

        // Assert
        assertThat(asistencia.getId()).isNull();
    }

    @Test
    @DisplayName("ID presente en asistencia reconstruida")
    void idPresente_EnAsistenciaReconstruida() {
        // Arrange
        String id = "asist-12345";

        // Act
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.reconstruct(
            id, 
            "sesion-Z", 
            "inscripcion-Z", 
            true, 
            LocalDateTime.now()
        );

        // Assert
        assertThat(asistencia.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Modificar ID después de settearlo")
    void modificarId_DespuesDeSettearlo() {
        // Arrange
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-W", 
            "inscripcion-W", 
            true, 
            Optional.empty()
        );
        String id1 = "id-original";
        String id2 = "id-modificado";

        // Act
        asistencia.setId(id1);
        assertThat(asistencia.getId()).isEqualTo(id1);

        asistencia.setId(id2);

        // Assert
        assertThat(asistencia.getId()).isEqualTo(id2);
    }

    @Test
    @DisplayName("Crear con Optional.ofNullable(null) - Debe usar fecha actual")
    void create_ConOptionalOfNullableNull_DebeUsarFechaActual() {
        // Arrange
        LocalDateTime antes = LocalDateTime.now();

        // Act
        AsistenciaDomainEntity asistencia = AsistenciaDomainEntity.create(
            "sesion-V", 
            "inscripcion-V", 
            true, 
            Optional.ofNullable(null)
        );

        // Assert
        assertThat(asistencia.getRegistradoEn()).isNotNull();
        assertThat(asistencia.getRegistradoEn()).isAfterOrEqualTo(antes);
    }
}