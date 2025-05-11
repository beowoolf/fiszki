package pl.mojezapiski.fiszki.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import pl.mojezapiski.fiszki.config.AbstractIntegrationTest;
import pl.mojezapiski.fiszki.model.Flashcard;
import pl.mojezapiski.fiszki.model.User;
import pl.mojezapiski.fiszki.repository.FlashcardRepository;
import pl.mojezapiski.fiszki.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
class FlashcardServiceTest extends AbstractIntegrationTest {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlashcardService flashcardService;

    private User testUser;
    private Flashcard testFlashcard;

    @BeforeEach
    void setUp() {
        // Przygotowanie kontekstu bezpieczeństwa
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        
        // Tworzenie i zapisywanie użytkownika
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        testUser = userRepository.save(testUser);
        
        // Tworzenie i zapisywanie fiszki
        testFlashcard = new Flashcard();
        testFlashcard.setFront("Test front");
        testFlashcard.setBack("Test back");
        testFlashcard.setUser(testUser);
        testFlashcard = flashcardRepository.save(testFlashcard);
        
        // Konfiguracja mocka dla kontekstu bezpieczeństwa
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void testGetUserFlashcards() {
        List<Flashcard> result = flashcardService.getUserFlashcards();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(testFlashcard.getFront(), result.get(0).getFront());
    }

    @Test
    void testCreateFlashcard() {
        Flashcard newFlashcard = new Flashcard();
        newFlashcard.setFront("New front");
        newFlashcard.setBack("New back");
        
        Flashcard result = flashcardService.createFlashcard(newFlashcard);
        
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals("New front", result.getFront());
        assertEquals("New back", result.getBack());
    }

    @Test
    void testUpdateFlashcard() {
        Flashcard updatedFlashcard = new Flashcard();
        updatedFlashcard.setFront("Updated front");
        updatedFlashcard.setBack("Updated back");
        updatedFlashcard.setLearned(true);

        Flashcard result = flashcardService.updateFlashcard(testFlashcard.getId(), updatedFlashcard);

        assertNotNull(result);
        assertEquals("Updated front", result.getFront());
        assertEquals("Updated back", result.getBack());
        assertTrue(result.isLearned());
    }

    @Test
    void testDeleteFlashcard() {
        assertDoesNotThrow(() -> flashcardService.deleteFlashcard(testFlashcard.getId()));
        assertFalse(flashcardRepository.findById(testFlashcard.getId()).isPresent());
    }
} 