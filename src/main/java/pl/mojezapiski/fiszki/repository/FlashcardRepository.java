package pl.mojezapiski.fiszki.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mojezapiski.fiszki.model.Flashcard;
import pl.mojezapiski.fiszki.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByUser(User user);
    List<Flashcard> findByUserAndNextReviewDateLessThanEqual(User user, LocalDateTime date);
    List<Flashcard> findByUserAndLearned(User user, boolean learned);
} 