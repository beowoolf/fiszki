package pl.mojezapiski.fiszki.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;

@TestConfiguration
public class TestContainersConfig {

    @Bean
    @ServiceConnection
    public MariaDBContainer<?> mariaDBContainer() {
        return new MariaDBContainer<>("mariadb:10.6")
                .withDatabaseName("fiszki")
                .withUsername("fiszki")
                .withPassword("fiszki");
    }
} 