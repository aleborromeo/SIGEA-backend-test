package com.zentry.sigea.module_actividad.infrastructure.database.adapters;

import com.zentry.sigea.module_actividad.core.entities.ActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.EstadoActividadDomainEntity;
import com.zentry.sigea.module_actividad.core.entities.TipoActividadDomainEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.ActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.EstadoActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.database.entities.TipoActividadEntity;
import com.zentry.sigea.module_actividad.infrastructure.repository.ActividadJPARepository;
import com.zentry.sigea.module_usuarios.infrastructure.database.entities.UsuarioEntity;
import com.zentry.sigea.module_usuarios.infrastructure.repositories.UsuarioJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime; // Importación necesaria para los campos de hora
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActividadRepositoryAdapterTest {

    @Mock
    private ActividadJPARepository actividadJPARepository;

    @Mock
    private UsuarioJPARepository usuarioJPARepository;

    private ActividadRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ActividadRepositoryAdapter(actividadJPARepository, usuarioJPARepository);
    }

    @Test
    // Se ha cambiado el nombre para reflejar el retorno de ActividadDomainEntity
    void save_debeRetornarActividadDomainEntity_cuandoGuardaCorrectamente() {
        // ARRANGE
        UUID organizadorId = UUID.randomUUID();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(organizadorId);
        
        ActividadDomainEntity domain = crearActividadDomain(organizadorId.toString());
        // Se crea un mock de la entidad guardada para que el ActividadMapper la convierta de nuevo
        ActividadEntity savedEntityMock = crearActividadEntityCompleta(UUID.randomUUID()); 
        
        when(usuarioJPARepository.findById(organizadorId)).thenReturn(Optional.of(usuario));
        when(actividadJPARepository.save(any(ActividadEntity.class))).thenReturn(savedEntityMock);

        // ACT
        // El adaptador.save() ahora devuelve ActividadDomainEntity
        ActividadDomainEntity result = adapter.save(domain);

        // ASSERT
        assertNotNull(result); // Verifica que el guardado fue exitoso y devolvió un objeto
        assertEquals(savedEntityMock.getTitulo(), result.getTitulo());
        verify(usuarioJPARepository, times(1)).findById(organizadorId);
        verify(actividadJPARepository, times(1)).save(any(ActividadEntity.class));
    }

    @Test
    // Se elimina el @Disabled y se cambia la lógica para esperar una excepción
    void save_debeLanzarExcepcion_cuandoUsuarioNoExiste() {
        // ARRANGE
        UUID organizadorId = UUID.randomUUID();
        ActividadDomainEntity domain = crearActividadDomain(organizadorId.toString());
        
        when(usuarioJPARepository.findById(organizadorId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        // Se espera que ActividadRepositoryAdapter.save() lance una RuntimeException
        RuntimeException thrown = assertThrows(
            RuntimeException.class, 
            () -> adapter.save(domain),
            "Se esperaba RuntimeException porque el usuario no existe."
        );
        
        // Opcional: verificar el mensaje específico de la excepción para "No se encontró el usuario especificado"
        assertTrue(thrown.getMessage().contains("No se encontró el usuario especificado")); 
        
        verify(usuarioJPARepository, times(1)).findById(organizadorId);
        verify(actividadJPARepository, never()).save(any());
    }

    @Test
    // Se cambia la lógica para esperar una excepción
    void save_debeLanzarExcepcion_cuandoOcurreExcepcionEnGuardado() {
        // ARRANGE
        UUID organizadorId = UUID.randomUUID();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(organizadorId);
        
        ActividadDomainEntity domain = crearActividadDomain(organizadorId.toString());
        
        when(usuarioJPARepository.findById(organizadorId)).thenReturn(Optional.of(usuario));
        // Se simula la excepción de la base de datos
        when(actividadJPARepository.save(any(ActividadEntity.class))).thenThrow(new RuntimeException("Error BD"));

        // ACT & ASSERT
        // Se espera que ActividadRepositoryAdapter.save() propague la RuntimeException
        assertThrows(
            RuntimeException.class, 
            () -> adapter.save(domain),
            "Se esperaba RuntimeException por error de base de datos."
        );
    }

    @Test
    void findById_debeRetornarActividad_cuandoExiste() {
        // ARRANGE
        UUID uuid = UUID.randomUUID();
        ActividadEntity entity = crearActividadEntityCompleta(uuid);
        
        when(actividadJPARepository.findById(uuid)).thenReturn(Optional.of(entity));

        // ACT
        Optional<ActividadDomainEntity> result = adapter.findById(uuid.toString());

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals("Taller Java", result.get().getTitulo());
        verify(actividadJPARepository, times(1)).findById(uuid);
    }

    @Test
    void findById_debeRetornarVacio_cuandoNoExiste() {
        // ARRANGE
        UUID uuid = UUID.randomUUID();
        when(actividadJPARepository.findById(uuid)).thenReturn(Optional.empty());

        // ACT
        Optional<ActividadDomainEntity> result = adapter.findById(uuid.toString());

        // ASSERT
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_debeRetornarListaDeActividades() {
        // ARRANGE
        ActividadEntity entity1 = crearActividadEntityCompleta(UUID.randomUUID());
        ActividadEntity entity2 = crearActividadEntityCompleta(UUID.randomUUID());
        
        when(actividadJPARepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        // ACT
        List<ActividadDomainEntity> result = adapter.findAll();

        // ASSERT
        assertEquals(2, result.size());
        verify(actividadJPARepository, times(1)).findAll();
    }

    @Test
    void findByOrganizadorId_debeRetornarActividadesDelOrganizador() {
        // ARRANGE
        UUID organizadorId = UUID.randomUUID();
        ActividadEntity entity = crearActividadEntityCompleta(UUID.randomUUID());
        
        when(actividadJPARepository.findByOrganizadorId(organizadorId))
            .thenReturn(Arrays.asList(entity));

        // ACT
        List<ActividadDomainEntity> result = adapter.findByOrganizadorId(organizadorId.toString());

        // ASSERT
        assertEquals(1, result.size());
        verify(actividadJPARepository, times(1)).findByOrganizadorId(organizadorId);
    }

    @Test
    void findAllIds_debeRetornarListaDeIds() {
        // ARRANGE
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        
        when(actividadJPARepository.findAllIds()).thenReturn(Arrays.asList(id1, id2));

        // ACT
        List<String> result = adapter.findAllIds();

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.contains(id1.toString()));
        assertTrue(result.contains(id2.toString()));
    }

    @Test
    void existsById_debeRetornarTrue_cuandoExiste() {
        // ARRANGE
        UUID uuid = UUID.randomUUID();
        when(actividadJPARepository.existsById(uuid)).thenReturn(true);

        // ACT
        boolean result = adapter.existsById(uuid.toString());

        // ASSERT
        assertTrue(result);
    }

    @Test
    void existsById_debeRetornarFalse_cuandoNoExiste() {
        // ARRANGE
        UUID uuid = UUID.randomUUID();
        when(actividadJPARepository.existsById(uuid)).thenReturn(false);

        // ACT
        boolean result = adapter.existsById(uuid.toString());

        // ASSERT
        assertFalse(result);
    }

    @Test
    void deleteById_debeEliminarActividad() {
        // ARRANGE
        UUID uuid = UUID.randomUUID();
        doNothing().when(actividadJPARepository).deleteById(uuid);

        // ACT
        adapter.deleteById(uuid.toString());

        // ASSERT
        verify(actividadJPARepository, times(1)).deleteById(uuid);
    }

    @Test
    void findByEstadoActividadId_debeRetornarActividades() {
        // ARRANGE
        UUID estadoId = UUID.randomUUID();
        ActividadEntity entity = crearActividadEntityCompleta(UUID.randomUUID());
        
        when(actividadJPARepository.findByEstadoActividadId(estadoId))
            .thenReturn(Arrays.asList(entity));

        // ACT
        List<ActividadDomainEntity> result = adapter.findByEstadoActividadId(estadoId.toString());

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void findByTipoActividadId_debeRetornarActividades() {
        // ARRANGE
        UUID tipoId = UUID.randomUUID();
        ActividadEntity entity = crearActividadEntityCompleta(UUID.randomUUID());
        
        when(actividadJPARepository.findByTipoActividadId(tipoId))
            .thenReturn(Arrays.asList(entity));

        // ACT
        List<ActividadDomainEntity> result = adapter.findByTipoActividadId(tipoId.toString());

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void findActiveActivities_debeRetornarActividadesActivas() {
        // ARRANGE
        ActividadEntity entity = crearActividadEntityCompleta(UUID.randomUUID());
        
        when(actividadJPARepository.findByEstadoActividad_Codigo("ACTIVO"))
            .thenReturn(Arrays.asList(entity));

        // ACT
        List<ActividadDomainEntity> result = adapter.findActiveActivities("ACTIVO");

        // ASSERT
        assertEquals(1, result.size());
    }

    // ========== MÉTODOS AUXILIARES ==========

    private ActividadDomainEntity crearActividadDomain(String organizadorId) {
        return ActividadDomainEntity.create(
            "Taller Java",
            "Descripción",
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            LocalTime.of(9, 0), 
            LocalTime.of(17, 0), 
            EstadoActividadDomainEntity.create("ACTIVO", "Activo"),
            organizadorId,
            TipoActividadDomainEntity.create("Taller", "Desc"),
            "Lima",
            "Co-Organizador Test", 
            "Sponsor Test", 
            "http://banner.url/test.png", 
            "999999999" 
        );
    }

    /**
     * Crea una ActividadEntity completa con todas sus relaciones necesarias
     * para evitar NullPointerException en el mapper.
     */
    private ActividadEntity crearActividadEntityCompleta(UUID actividadId) {
        // Crear usuario (organizador)
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(UUID.randomUUID());
        
        // Crear estado
        EstadoActividadEntity estado = new EstadoActividadEntity();
        estado.setId(UUID.randomUUID());
        estado.setCodigo("ACTIVO");
        estado.setEtiqueta("Activo");
        
        // Crear tipo
        TipoActividadEntity tipo = new TipoActividadEntity();
        tipo.setId(UUID.randomUUID());
        tipo.setNombreActividad("Taller");
        tipo.setDescripcion("Descripción");
        
        // Crear actividad completa
        ActividadEntity entity = new ActividadEntity();
        entity.setId(actividadId);
        entity.setTitulo("Taller Java");
        entity.setDescripcion("Descripción del taller");
        entity.setFechaInicio(LocalDate.now());
        entity.setFechaFin(LocalDate.now().plusDays(5));

        // Configuración de los nuevos campos de tiempo y detalles
        entity.setHoraInicio(LocalTime.of(9, 0)); 
        entity.setHoraFin(LocalTime.of(17, 0));
        entity.setCoOrganizador("Co-Organizador Test");
        entity.setSponsor("Sponsor Test");
        entity.setBannerUrl("http://banner.url/test.png");
        entity.setNumeroYape("999999999");
        
        entity.setOrganizador(usuario);  
        entity.setEstadoActividad(estado);
        entity.setTipoActividad(tipo);
        entity.setLugar("Lima");
        
        return entity;
    }
}