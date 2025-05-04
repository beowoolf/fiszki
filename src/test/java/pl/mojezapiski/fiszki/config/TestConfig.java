package pl.mojezapiski.fiszki.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestConfig {
    private static final MariaDBContainer<?> mariaDBContainer;

    static {
        mariaDBContainer = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.6"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        mariaDBContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
    }

    @Bean
    public MariaDBContainer<?> getMariaDBContainer() {
        return mariaDBContainer;
    }
} 