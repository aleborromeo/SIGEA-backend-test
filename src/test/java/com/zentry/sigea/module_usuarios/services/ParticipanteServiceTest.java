package com.zentry.sigea.module_usuarios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.zentry.sigea.module_usuarios.services.usecases.participante.RegistrarParticipanteUseCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParticipanteServiceTest {

    @Mock
    private RegistrarParticipanteUseCase registrarParticipanteUseCase;

    @InjectMocks
    private ParticipanteService participanteService;

    @Test
    void registrar_participante_ok() {
        when(registrarParticipanteUseCase.execute(any()))
                .thenReturn("Registrado");

        String result = participanteService.registrarParticipante(any());

        assertEquals("Registrado", result);
        verify(registrarParticipanteUseCase).execute(any());
    }
}
