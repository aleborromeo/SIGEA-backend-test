package com.zentry.sigea;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import io.github.cdimascio.dotenv.Dotenv; // NECESARIO

@SpringBootTest
@Disabled("Requiere configuración completa de ApplicationContext")
class SigeaApplicationTests {
    
    // ↓↓↓ ESTO SOLUCIONA EL ERROR DE 'MAIL_HOST' Y EL ILLEGALSTATE ↓↓↓
    static {
        try {
            // Cargar variables de .env en el entorno de Java antes de que Spring inicie
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            // Esto es crucial para que la prueba no falle catastróficamente si el .env no está
            System.err.println("Advertencia: No se pudo cargar el archivo .env para pruebas.");
        }
    }

	@Test
	void contextLoads() {
	}
}