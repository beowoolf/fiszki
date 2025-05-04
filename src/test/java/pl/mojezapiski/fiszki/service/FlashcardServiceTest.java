package pl.mojezapiski.fiszki.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import pl.mojezapiski.fiszki.config.TestConfig;
import pl.mojezapiski.fiszki.model.Flashcard;
import pl.mojezapiski.fiszki.model.User;
import pl.mojezapiski.fiszki.repository.FlashcardRepository;
import pl.mojezapiski.fiszki.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class FlashcardServiceTest {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FlashcardService flashcardService;

    private User testUser;
    private Flashcard testFlashcard;

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        userRepository.save(testUser);
        
        testFlashcard = new Flashcard();
        testFlashcard.setId(1L);
        testFlashcard.setFront("Test front");
        testFlashcard.setBack("Test back");
        testFlashcard.setUser(testUser);
        flashcardRepository.save(testFlashcard);
        
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