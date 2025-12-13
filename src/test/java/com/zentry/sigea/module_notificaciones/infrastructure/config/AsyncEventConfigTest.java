package com.zentry.sigea.module_notificaciones.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

class AsyncEventConfigTest {

    @Test
    void notificationEventExecutor_debeCrearExecutorCorrectamente() {
        // Arrange
        AsyncEventConfig config = new AsyncEventConfig();

        // Act
        Executor executor = config.notificationEventExecutor();

        // Assert
        assertNotNull(executor);
        assertTrue(executor instanceof ThreadPoolTaskExecutor);

        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) executor;

        assertEquals(2, taskExecutor.getCorePoolSize());
        assertEquals(5, taskExecutor.getMaxPoolSize());
        assertEquals(100, taskExecutor.getQueueCapacity());
        assertEquals("NotificationEvent-", taskExecutor.getThreadNamePrefix());
    }
}
