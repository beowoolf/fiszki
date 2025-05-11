package pl.mojezapiski.fiszki.service;

import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.mojezapiski.fiszki.model.Flashcard;
import pl.mojezapiski.fiszki.model.User;
import pl.mojezapiski.fiszki.repository.FlashcardRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final UserService userService;

    public List<Flashcard> getUserFlashcards() {
        User user = getCurrentUser();
        return flashcardRepository.findByUser(user);
    }

    public List<Flashcard> getFlashcardsForReview() {
        User user = getCurrentUser();
        return flashcardRepository.findByUserAndNextReviewDateLessThanEqual(user, LocalDateTime.now());
    }

    public List<Flashcard> getLearnedFlashcards() {
        User user = getCurrentUser();
        return flashcardRepository.findByUserAndLearned(user, true);
    }

    public Flashcard createFlashcard(Flashcard flashcard) {
        User user = getCurrentUser();
        flashcard.setUser(user);
        return flashcardRepository.save(flashcard);
    }

    public Flashcard updateFlashcard(Long id, Flashcard updatedFlashcard) {
        User user = getCurrentUser();
        Flashcard existingFlashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        
        if (!existingFlashcard.getUser().equals(user)) {
            throw new RuntimeException("Not authorized to update this flashcard");
        }

        existingFlashcard.setFront(updatedFlashcard.getFront());
        existingFlashcard.setBack(updatedFlashcard.getBack());
        existingFlashcard.setLearned(updatedFlashcard.isLearned());
        
        if (updatedFlashcard.isLearned()) {
            existingFlashcard.setNextReviewDate(LocalDateTime.now().plusDays(1));
        }
        
        return flashcardRepository.save(existingFlashcard);
    }

    public void deleteFlashcard(Long id) {
        User user = getCurrentUser();
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        
        if (!flashcard.getUser().equals(user)) {
            throw new RuntimeException("Not authorized to delete this flashcard");
        }
        
        flashcardRepository.delete(flashcard);
    }

    public void markAsLearned(Long id) {
        User user = getCurrentUser();
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        
        if (!flashcard.getUser().equals(user)) {
            throw new RuntimeException("Not authorized to mark this flashcard");
        }

        flashcard.setLearned(true);
        flashcard.setNextReviewDate(LocalDateTime.now().plusDays(1));
        flashcardRepository.save(flashcard);
    }

    public void reviewFlashcard(Long id, int quality) {
        User user = getCurrentUser();
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        
        if (!flashcard.getUser().equals(user)) {
            throw new RuntimeException("Not authorized to review this flashcard");
        }

        // Implementacja algorytmu SM-2 (uproszczona)
        if (quality >= 3) {
            if (flashcard.getReviewCount() == 0) {
                flashcard.setIntervalDays(1);
            } else if (flashcard.getReviewCount() == 1) {
                flashcard.setIntervalDays(6);
            } else {
                flashcard.setIntervalDays((int) (flashcard.getIntervalDays() * flashcard.getEaseFactor()));
            }
            
            flashcard.setEaseFactor(flashcard.getEaseFactor() + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)));
        } else {
            flashcard.setIntervalDays(1);
            flashcard.setEaseFactor(Math.max(1.3, flashcard.getEaseFactor() - 0.2));
        }

        flashcard.setReviewCount(flashcard.getReviewCount() + 1);
        flashcard.setLastReviewDate(LocalDateTime.now());
        flashcard.setNextReviewDate(LocalDateTime.now().plusDays(flashcard.getIntervalDays()));
        
        flashcardRepository.save(flashcard);
    }

    public Flashcard getFlashcard(Long id) {
        User user = getCurrentUser();
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        
        if (!flashcard.getUser().equals(user)) {
            throw new RuntimeException("Not authorized to view this flashcard");
        }
        
        return flashcard;
    }

    public Statistics getStatistics() {
        User user = getCurrentUser();
        List<Flashcard> allFlashcards = flashcardRepository.findByUser(user);
        List<Flashcard> forReview = getFlashcardsForReview();
        List<Flashcard> learned = getLearnedFlashcards();

        int totalReviews = allFlashcards.stream()
                .mapToInt(Flashcard::getReviewCount)
                .sum();

        return new Statistics(
            allFlashcards.size(),
            forReview.size(),
            learned.size(),
            totalReviews
        );
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return (User) userService.loadUserByUsername(username);
    }

    @Data
    @AllArgsConstructor
    public static class Statistics {
        private int totalFlashcards;
        private int flashcardsForReview;
        private int learnedFlashcards;
        private int totalReviews;
    }
} 