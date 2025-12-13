package com.zentry.sigea.module_actividad.presentation.api;

import com.zentry.sigea.module_actividad.services.FileBannerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileBannerControllerTest {

    @Mock
    private FileBannerService fileBannerService;

    @InjectMocks
    private FileBannerController controller;

    @Test
    void uploadBanner_success() {
        MultipartFile file =
                new MockMultipartFile("imagen", "test.png", "image/png", "data".getBytes());

        when(fileBannerService.uploadBanner(file))
                .thenReturn("/api/v1/actividad/banner/imagen/test.png");

        ResponseEntity<FileBannerController.BannerResponse> response =
                controller.uploadBanner(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        assertEquals("/api/v1/actividad/banner/imagen/test.png",
                response.getBody().url());
        assertEquals("test.png", response.getBody().filename());
    }

    @Test
    void uploadBanner_validationError() {
        MultipartFile file =
                new MockMultipartFile("imagen", "test.png", "image/png", "data".getBytes());

        when(fileBannerService.uploadBanner(file))
                .thenThrow(new IllegalArgumentException("Archivo inválido"));

        ResponseEntity<FileBannerController.BannerResponse> response =
                controller.uploadBanner(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals("Archivo inválido", response.getBody().message());
    }

    @Test
    void uploadBanner_internalError() {
        MultipartFile file =
                new MockMultipartFile("imagen", "test.png", "image/png", "data".getBytes());

        when(fileBannerService.uploadBanner(file))
                .thenThrow(new RuntimeException("Fallo"));

        ResponseEntity<FileBannerController.BannerResponse> response =
                controller.uploadBanner(file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertTrue(response.getBody().message().startsWith("Error al subir la imagen:"));
    }

    @Test
    void getBanner_found() throws Exception {
        Path tempFile = Files.createTempFile("banner-test", ".png");
        Files.writeString(tempFile, "imgdata");

        when(fileBannerService.getImagePath("file.png"))
                .thenReturn(tempFile);

        ResponseEntity<byte[]> response = controller.getBanner("file.png");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void getBanner_notFound() {
        Path nonExistent = Path.of("no_existe_123.png");
        when(fileBannerService.getImagePath("file.png"))
                .thenReturn(nonExistent);

        ResponseEntity<byte[]> response = controller.getBanner("file.png");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteBanner_success() {
        ResponseEntity<FileBannerController.BannerResponse> response =
                controller.deleteBanner("test.png");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        verify(fileBannerService).deleteBanner("test.png");
    }

    @Test
    void deleteBanner_internalError() {
        doThrow(new RuntimeException("err")).when(fileBannerService)
                .deleteBanner("test.png");

        ResponseEntity<FileBannerController.BannerResponse> response =
                controller.deleteBanner("test.png");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertTrue(response.getBody().message().startsWith("Error al eliminar la imagen:"));
    }
}
