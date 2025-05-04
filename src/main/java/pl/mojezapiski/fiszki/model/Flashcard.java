package pl.mojezapiski.fiszki.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "flashcards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String front;

    @Column(nullable = false)
    private String back;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_learned")
    private boolean learned = false;

    @Column(name = "next_review_date")
    private LocalDateTime nextReviewDate = LocalDateTime.now();

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "last_review_date")
    private LocalDateTime lastReviewDate;

    @Column(name = "ease_factor")
    private Double easeFactor = 2.5;

    @Column(name = "interval_days")
    private Integer intervalDays = 0;
} 