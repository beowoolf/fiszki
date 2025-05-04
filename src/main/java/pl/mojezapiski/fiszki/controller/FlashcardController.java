package pl.mojezapiski.fiszki.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.mojezapiski.fiszki.model.Flashcard;
import pl.mojezapiski.fiszki.service.FlashcardService;

@Controller
@RequestMapping("/flashcards")
@RequiredArgsConstructor
public class FlashcardController {
    private final FlashcardService flashcardService;

    @GetMapping
    public String listFlashcards(Model model) {
        model.addAttribute("flashcards", flashcardService.getUserFlashcards());
        model.addAttribute("statistics", flashcardService.getStatistics());
        model.addAttribute("currentPage", "flashcards");
        model.addAttribute("title", "flashcards/list");
        model.addAttribute("content", "flashcards/list");
        return "layout/main";
    }

    @GetMapping("/review")
    public String reviewFlashcards(Model model) {
        model.addAttribute("flashcards", flashcardService.getFlashcardsForReview());
        model.addAttribute("currentPage", "review");
        model.addAttribute("title", "flashcards/review");
        model.addAttribute("content", "flashcards/review");
        return "layout/main";
    }

    @GetMapping("/learned")
    public String listLearnedFlashcards(Model model) {
        model.addAttribute("flashcards", flashcardService.getLearnedFlashcards());
        model.addAttribute("currentPage", "learned");
        model.addAttribute("title", "flashcards/learned");
        model.addAttribute("content", "flashcards/learned");
        return "layout/main";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flashcard", new Flashcard());
        model.addAttribute("currentPage", "flashcards");
        model.addAttribute("title", "flashcards/form");
        model.addAttribute("content", "flashcards/form");
        return "layout/main";
    }

    @PostMapping
    public String createFlashcard(@ModelAttribute Flashcard flashcard) {
        flashcardService.createFlashcard(flashcard);
        return "redirect:/flashcards";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Flashcard flashcard = flashcardService.getFlashcard(id);
        model.addAttribute("flashcard", flashcard);
        model.addAttribute("currentPage", "flashcards");
        model.addAttribute("title", "flashcards/form");
        model.addAttribute("content", "flashcards/form");
        return "layout/main";
    }

    @PostMapping("/{id}")
    public String updateFlashcard(@PathVariable Long id, @ModelAttribute Flashcard flashcard) {
        flashcardService.updateFlashcard(id, flashcard);
        return "redirect:/flashcards";
    }

    @PostMapping("/{id}/delete")
    public String deleteFlashcard(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
        return "redirect:/flashcards";
    }

    @PostMapping("/{id}/learned")
    public String markAsLearned(@PathVariable Long id) {
        flashcardService.markAsLearned(id);
        return "redirect:/flashcards";
    }

    @PostMapping("/{id}/review")
    public String reviewFlashcard(@PathVariable Long id, @RequestParam int quality) {
        flashcardService.reviewFlashcard(id, quality);
        return "redirect:/flashcards/review";
    }

    private boolean learned;
} 