package com.zentry.sigea.module_notificaciones.infrastructure.external;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTemplatesTest {

    @Test
    void notificacionHtml_debeContenerHtmlYPlaceholders() {
        String html = EmailTemplates.notificacionHtml();

        assertNotNull(html);
        assertTrue(html.contains("<html"));
        assertTrue(html.contains("%s"));
        assertTrue(html.contains("SIGEA"));
    }

    @Test
    void codigoVerificacionHtml_debeContenerCodigo() {
        String html = EmailTemplates.codigoVerificacionHtml();

        assertNotNull(html);
        assertTrue(html.contains("Código de Verificación"));
        assertTrue(html.contains("%s"));
    }
}
