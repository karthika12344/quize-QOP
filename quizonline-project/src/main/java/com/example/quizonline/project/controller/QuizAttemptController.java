package com.example.quizonline.project.controller;

import com.example.quizonline.project.entity.QuizAttempt;
import com.example.quizonline.project.entity.User;
import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.service.EmailService;
import com.example.quizonline.project.service.QuizAttemptService;
import com.example.quizonline.project.service.QuizService;
import com.example.quizonline.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attempts")
public class QuizAttemptController {

    @Autowired private QuizAttemptService attemptService;
    @Autowired private UserService userService;
    @Autowired private QuizService quizService;
    @Autowired private EmailService emailService;

    // Submit attempt (you can submit either full QuizAttempt JSON or use query params)
   @PostMapping("/submit")
  public ResponseEntity<?> submitAttempt(@RequestBody QuizAttempt attempt) {
    try {
        User user = userService.findByEmail(attempt.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Quiz quiz = quizService.getQuizById(attempt.getQuiz().getId());

        QuizAttempt newAttempt = QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .score(attempt.getScore())
                .attemptedAt(LocalDateTime.now())
                .build();

        QuizAttempt savedAttempt = attemptService.saveAttempt(newAttempt);

        // ✅ avoid NPE if quiz.getQuestions() is null
        int totalQuestions = 0;
        if (quiz.getQuestions() != null) {
            totalQuestions = quiz.getQuestions().size();
        }

        // ✅ safer email
        String subject = "Your Quiz Results - " + quiz.getTitle();
        String message = "Hi " + (user.getName() != null ? user.getName() : user.getEmail()) + ",\n\n"
                + "You’ve completed the quiz: " + quiz.getTitle() + " 🎉\n"
                + "Your score: " + attempt.getScore() + " / " + totalQuestions + " points.\n\n"
                + "Keep practicing and good luck for your next quiz!\n\n"
                + "- Quiz Platform Team";

        // ✅ wrap email in try-catch so email failure won’t crash backend
        try {
            emailService.sendEmail(user.getEmail(), subject, message);
        } catch (Exception e) {
            System.err.println("⚠️ Email sending failed but quiz saved: " + e.getMessage());
        }

        return ResponseEntity.ok(savedAttempt);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("❌ Error submitting quiz: " + e.getMessage());
    }
}


    // Alternative submit by params (email + quizId + score)
    @PostMapping("/submit/by")
    public ResponseEntity<?> submitByParams(@RequestParam String email,
                                            @RequestParam Long quizId,
                                            @RequestParam int score) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = quizService.getQuizById(quizId);
        QuizAttempt saved = attemptService.saveAttempt(user, quiz, score);
        return ResponseEntity.ok(saved);
    }

    // Get attempts for user (by email)
    @GetMapping("/user")
    public ResponseEntity<List<QuizAttempt>> getAttemptsByUser(@RequestParam String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(attemptService.getAttemptsByUser(user));
    }

    // Get all attempts
    @GetMapping("/all")
    public ResponseEntity<List<QuizAttempt>> getAllAttempts() {
        return ResponseEntity.ok(attemptService.getAllAttempts());
    }
}
