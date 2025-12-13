package com.zentry.sigea.module_usuarios.services;

import com.zentry.sigea.module_notificaciones.core.ports.IEmailService;
import com.zentry.sigea.module_usuarios.core.entities.CodigoVerificacionDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.ICodigoVerificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnviarCodigoVerificacionPorEmailServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ICodigoVerificacionRepository codigoVerificacionRepository;

    @Mock
    IEmailService emailService;

    @InjectMocks
    EnviarCodigoVerificacionPorEmailService service;

    @Test
    void execute_envia_email_y_guarda_codigo_ok() {
        // ARRANGE
        when(emailService.enviarCodigoVerificacion(any(), any(), anyInt()))
                .thenReturn(true);

        when(passwordEncoder.encode(any()))
                .thenReturn("codigo_encriptado");

        // ACT
        service.execute("test@correo.com", "Juan");

        // ASSERT
        verify(emailService, times(1))
                .enviarCodigoVerificacion(eq("test@correo.com"), eq("Juan"), anyInt());

        verify(codigoVerificacionRepository, times(1))
                .save(any(CodigoVerificacionDomainEntity.class));
    }

    @Test
    void execute_lanza_excepcion_si_correo_vacio() {
        // ACT & ASSERT
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                service.execute("", "Juan")
        );

        // Verifica el mensaje
        assert ex.getMessage().contains("No se envio el correo");

        // No debe intentar enviar ni guardar nada
        verifyNoInteractions(emailService);
        verifyNoInteractions(codigoVerificacionRepository);
    }

    @Test
    void execute_guarda_codigo_aun_si_email_falla() {
        // ARRANGE
        when(emailService.enviarCodigoVerificacion(any(), any(), anyInt()))
                .thenReturn(false);

        when(passwordEncoder.encode(any()))
                .thenReturn("codigo_encriptado");

        // ACT
        service.execute("test@correo.com", "Juan");

        // ASSERT
        verify(emailService, times(1))
                .enviarCodigoVerificacion(eq("test@correo.com"), eq("Juan"), anyInt());

        // Igual debe guardar el código
        verify(codigoVerificacionRepository, times(1))
                .save(any(CodigoVerificacionDomainEntity.class));
    }
}
