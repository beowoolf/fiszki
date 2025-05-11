package pl.mojezapiski.fiszki.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
public abstract class AbstractIntegrationTest {
    // Ta klasa będzie bazą dla wszystkich testów integracyjnych
    // Wszystkie testy, które będą dziedziczyć po tej klasie, automatycznie
    // otrzymają skonfigurowany kontener z bazą danych
} 