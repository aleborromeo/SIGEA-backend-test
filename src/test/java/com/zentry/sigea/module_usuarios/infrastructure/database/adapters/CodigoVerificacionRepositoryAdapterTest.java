package com.zentry.sigea.module_usuarios.infrastructure.database.adapters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zentry.sigea.module_usuarios.core.entities.CodigoVerificacionDomainEntity;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.CodigoVerificacionEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.CodigoVerificacionJPARepository;

@ExtendWith(MockitoExtension.class)
class CodigoVerificacionRepositoryAdapterTest {

    @Mock
    private CodigoVerificacionJPARepository jpaRepository;

    @InjectMocks
    private CodigoVerificacionRepositoryAdapter adapter;

    @Test
    void save_guardaCodigo() {
        CodigoVerificacionDomainEntity domain =
            CodigoVerificacionDomainEntity.create("123456", "test@mail.com");

        adapter.save(domain);

        verify(jpaRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void findById_retornaDomain() {
        UUID id = UUID.randomUUID();
        CodigoVerificacionEntity entity = new CodigoVerificacionEntity();
        entity.setId(id);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<CodigoVerificacionDomainEntity> result =
            adapter.findById(id.toString());

        assertTrue(result.isPresent());
    }

    @Test
    void findCodigoByCorreo_retornaLista() {
        when(jpaRepository.findCodigoByCorreo("mail@test.com"))
            .thenReturn(List.of("111", "222"));

        List<String> codigos = adapter.findCodigoByCorreo("mail@test.com");

        assertEquals(2, codigos.size());
    }

    @Test
    void deleteExpiredCodes_noLanzaError() {
        adapter.deleteExpiresCodes(Instant.now());
        verify(jpaRepository).deleteExpiredCodes(any());
    }
}
