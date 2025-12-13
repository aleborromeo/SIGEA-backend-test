package com.zentry.sigea.module_actividad.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileBannerServiceTest {

    private FileBannerService service;

    @BeforeEach
    void setUp() {
        service = new FileBannerService();
        // Inicializar carpeta de uploads
        service.init();
    }

    @Test
    void init_creaCarpetaUploads() {
        Path uploadDir = Paths.get("uploads/banners");
        assertTrue(Files.exists(uploadDir));
    }

    @Test
    void uploadBanner_success() throws Exception {
        byte[] data = "imagen".getBytes();
        MultipartFile file = new MockMultipartFile(
                "imagen",
                "test.png",
                "image/png",
                data
        );

        String url = service.uploadBanner(file);

        assertNotNull(url);
        assertTrue(url.contains("/api/v1/actividad/banner/imagen/"));

        String filename = url.substring(url.lastIndexOf("/") + 1);
        Path storedPath = Paths.get("uploads/banners", filename);
        assertTrue(Files.exists(storedPath));

        // Limpieza
        Files.deleteIfExists(storedPath);
    }

    @Test
    void uploadBanner_lanzaErrorPorContentTypeInvalido() {
        MultipartFile file = new MockMultipartFile(
                "imagen",
                "test.png",
                "text/plain",
                "data".getBytes()
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.uploadBanner(file)
        );
        assertTrue(ex.getMessage().contains("Tipo de archivo no permitido"));
    }

    @Test
    void uploadBanner_lanzaErrorPorExtensionInvalida() {
        MultipartFile file = new MockMultipartFile(
                "imagen",
                "archivo.txt",
                "image/png",    // tipo válido
                "data".getBytes()
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.uploadBanner(file)
        );
        assertTrue(ex.getMessage().contains("Extensión de archivo no permitida"));
    }

    @Test
    void uploadBanner_lanzaErrorPorTamanyoExcesivo() {
        // 31 MB para exceder el límite de 30 MB
        byte[] bigData = new byte[31 * 1024 * 1024];
        MultipartFile file = new MockMultipartFile(
                "imagen",
                "big.png",
                "image/png",
                bigData
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.uploadBanner(file)
        );
        assertTrue(ex.getMessage().contains("demasiado grande"));
    }

    @Test
    void getImagePath_devuelvePathCorrecto() {
        Path path = service.getImagePath("test.png");

        assertEquals(Paths.get("uploads/banners", "test.png"), path);
    }

    @Test
    void existsBanner_trueCuandoExiste() throws Exception {
        // Creamos un archivo dummy
        Path filePath = Paths.get("uploads/banners", "exists.png");
        Files.writeString(filePath, "data");

        assertTrue(service.existsBanner("exists.png"));

        Files.deleteIfExists(filePath);
    }

    @Test
    void existsBanner_falseCuandoNoExiste() {
        assertFalse(service.existsBanner("no_existe_123.png"));
    }

    @Test
    void deleteBanner_ignoraNullYEmpty() {
        // No debe lanzar excepción
        service.deleteBanner(null);
        service.deleteBanner("");
    }

    @Test
    void deleteBanner_eliminaPorNombreSimple() throws Exception {
        Path filePath = Paths.get("uploads/banners", "a_borrar.png");
        Files.writeString(filePath, "data");

        assertTrue(Files.exists(filePath));

        service.deleteBanner("a_borrar.png");

        assertFalse(Files.exists(filePath));
    }

    @Test
    void deleteBanner_aceptaUrlCompleta() throws Exception {
        Path filePath = Paths.get("uploads/banners", "desde_url.png");
        Files.writeString(filePath, "data");

        service.deleteBanner("/api/v1/actividad/banner/imagen/desde_url.png");

        assertFalse(Files.exists(filePath));
    }
}
