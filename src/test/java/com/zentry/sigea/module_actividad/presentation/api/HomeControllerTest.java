package com.zentry.sigea.module_actividad.presentation.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @Test
    void index_returnsExpectedMessage() {
        HomeController controller = new HomeController();

        String response = controller.Index();

        assertEquals("el backend esta corriendo-se", response);
    }
}
