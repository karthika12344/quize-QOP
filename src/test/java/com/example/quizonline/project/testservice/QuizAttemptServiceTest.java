package com.example.quizonline.project.testservice;


import com.example.quizonline.project.entity.Question;
import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.entity.QuizAttempt;
import com.example.quizonline.project.entity.User;
import com.example.quizonline.project.entity.UserAnswer;
import com.example.quizonline.project.repository.QuestionRepository;
import com.example.quizonline.project.repository.QuizAttemptRepository;
import com.example.quizonline.project.repository.UserAnswerRepository;
import com.example.quizonline.project.service.EmailService;
import com.example.quizonline.project.service.QuizAttemptService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizAttemptServiceTest {

    @Mock private QuizAttemptRepository attemptRepository;
    @Mock private QuestionRepository questionRepository;
    @Mock private UserAnswerRepository userAnswerRepository;
    @Mock private EmailService emailService;

    @InjectMocks
    private QuizAttemptService quizAttemptService;

    private Quiz quiz;
    private User user;
    private Question question;
    private UserAnswer correctAnswer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("Athish")
                .email("athish@example.com")
                .build();

        quiz = Quiz.builder()
                .id(1L)
                .title("Java Basics")
                .questions(new ArrayList<>())
                .build();

        question = Question.builder()
                .id(10L)
                .correctAnswer("A")
                .build();

        correctAnswer = UserAnswer.builder()
                .questionId(10L)
                .selectedOption("A")
                .build();

        when(questionRepository.findById(10L)).thenReturn(Optional.of(question));
        when(attemptRepository.save(any(QuizAttempt.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testEvaluateAndSaveAttempt_CorrectAnswer() {
        QuizAttempt attempt = QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .answers(List.of(correctAnswer))
                .build();

        QuizAttempt savedAttempt = quizAttemptService.evaluateAndSaveAttempt(attempt);

        assertEquals(1, savedAttempt.getScore());
        assertNotNull(savedAttempt.getAttemptedAt());
        verify(attemptRepository, times(1)).save(any(QuizAttempt.class));
        verify(emailService, times(1))
                .sendEmail(eq("athish@example.com"), anyString(), contains("Java Basics"));
    }

    @Test
    void testSaveAttempt_ByUserAndQuiz() {
        when(attemptRepository.save(any(QuizAttempt.class)))
                .thenAnswer(i -> i.getArgument(0));

        QuizAttempt result = quizAttemptService.saveAttempt(user, quiz, 5);

        assertEquals(5, result.getScore());
        assertEquals(user, result.getUser());
        assertEquals(quiz, result.getQuiz());
        verify(attemptRepository, times(1)).save(any(QuizAttempt.class));
    }

    @Test
    void testGetAttemptsByUser() {
        when(attemptRepository.findByUser(user)).thenReturn(List.of(new QuizAttempt()));

        List<QuizAttempt> attempts = quizAttemptService.getAttemptsByUser(user);

        assertEquals(1, attempts.size());
    }

    @Test
    void testGetAllAttempts() {
        when(attemptRepository.findAll()).thenReturn(List.of(new QuizAttempt()));

        List<QuizAttempt> attempts = quizAttemptService.getAllAttempts();

        assertEquals(1, attempts.size());
        verify(attemptRepository, times(1)).findAll();
    }
}
