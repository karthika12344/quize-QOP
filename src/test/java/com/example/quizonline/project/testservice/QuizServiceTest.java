package com.example.quizonline.project.testservice;


import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.repository.QuizRepository;
import com.example.quizonline.project.service.QuizService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveQuiz() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Java Basics");

        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

        Quiz savedQuiz = quizService.saveQuiz(quiz);

        assertNotNull(savedQuiz);
        assertEquals("Java Basics", savedQuiz.getTitle());
        verify(quizRepository, times(1)).save(quiz);
    }

    @Test
    void testGetAllQuizzes() {
        when(quizRepository.findAll()).thenReturn(List.of(new Quiz(), new Quiz()));

        List<Quiz> quizzes = quizService.getAllQuizzes();

        assertEquals(2, quizzes.size());
        verify(quizRepository, times(1)).findAll();
    }

    @Test
    void testGetQuizById_Found() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Spring Boot");

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));

        Quiz found = quizService.getQuizById(1L);

        assertEquals("Spring Boot", found.getTitle());
        verify(quizRepository, times(1)).findById(1L);
    }

    @Test
    void testGetQuizById_NotFound() {
        when(quizRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> quizService.getQuizById(1L));
    }

    @Test
    void testDeleteQuiz() {
        quizService.deleteQuiz(1L);
        verify(quizRepository, times(1)).deleteById(1L);
    }
}
