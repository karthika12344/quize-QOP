package com.example.quizonline.project.service;


import com.example.quizonline.project.entity.Question;
import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.entity.QuizAttempt;
import com.example.quizonline.project.entity.User;
import com.example.quizonline.project.entity.UserAnswer;
import com.example.quizonline.project.repository.QuestionRepository;
import com.example.quizonline.project.repository.QuizAttemptRepository; 
import com.example.quizonline.project.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizAttemptService {

    @Autowired
    private QuizAttemptRepository attemptRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Evaluate quiz answers, save attempt, and send result to Gmail
     */
    public QuizAttempt evaluateAndSaveAttempt(QuizAttempt attempt) {
        int score = 0;

        if (attempt.getAnswers() != null) {
            for (UserAnswer answer : attempt.getAnswers()) {
                Question question = questionRepository.findById(answer.getQuestionId())
                        .orElseThrow(() -> new RuntimeException("Question not found"));

                if (question.getCorrectAnswer().equalsIgnoreCase(answer.getSelectedOption())) {
                    score++;
                }

                answer.setQuizAttempt(attempt);
            }
        }

        attempt.setScore(score);
        attempt.setAttemptedAt(LocalDateTime.now());
        QuizAttempt saved = attemptRepository.save(attempt);

        // ✅ Send email with score
        sendScoreEmail(saved);

        return saved;
    }

    private void sendScoreEmail(QuizAttempt attempt) {
        User user = attempt.getUser();
        Quiz quiz = attempt.getQuiz();

        String subject = "Quiz Results - " + quiz.getTitle();
        String message = "Hi " + user.getName() + ",\n\n"
                + "You have completed the quiz: " + quiz.getTitle() + ".\n"
                + "Your score: " + attempt.getScore() + " / " + quiz.getQuestions().size() + "\n\n"
                + "Keep practicing and good luck!\n\n"
                + "- Quiz Platform Team";

        try {
            emailService.sendEmail(user.getEmail(), subject, message);
            System.out.println("✅ Email sent successfully to " + user.getEmail());
        } catch (Exception e) {
            System.err.println("❌ Email sending failed: " + e.getMessage());
        }
    }

    public QuizAttempt saveAttempt(User user, Quiz quiz, int score) {
        QuizAttempt attempt = QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .score(score)
                .attemptedAt(LocalDateTime.now())
                .build();

        return attemptRepository.save(attempt);
    }

    public List<QuizAttempt> getAttemptsByUser(User user) {
        return attemptRepository.findByUser(user);
    }

    public List<QuizAttempt> getAllAttempts() {
        return attemptRepository.findAll();
    }

    public QuizAttempt saveAttempt(QuizAttempt attempt) {
    return attemptRepository.save(attempt);
}

}
