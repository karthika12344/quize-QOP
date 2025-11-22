package com.example.quizonline.project.testcontroller;

import com.example.quizonline.project.entity.Question;
import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.service.QuestionService;
import com.example.quizonline.project.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.AccessController;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccessController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Quiz quiz;
    private Question question;

    @BeforeEach
    void setUp() {
        quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Java Basics");

        question = new Question();
        question.setId(1L);
        question.setQuestionText("What is JVM?");
    }

    // ✅ Test create quiz
    @Test
    void testCreateQuiz() throws Exception {
        Mockito.when(quizService.saveQuiz(any(Quiz.class))).thenReturn(quiz);

        mockMvc.perform(post("/api/admin/quiz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quiz)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Basics"));
    }

    // ✅ Test get all quizzes
    @Test
    void testGetAllQuizzes() throws Exception {
        List<Quiz> quizzes = Arrays.asList(quiz);
        Mockito.when(quizService.getAllQuizzes()).thenReturn(quizzes);

        mockMvc.perform(get("/api/admin/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java Basics"));
    }

    // ✅ Test get quiz by ID
    @Test
    void testGetQuizById() throws Exception {
        Mockito.when(quizService.getQuizById(1L)).thenReturn(quiz);

        mockMvc.perform(get("/api/admin/quiz/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Basics"));
    }

    // ✅ Test delete quiz
    @Test
    void testDeleteQuiz() throws Exception {
        mockMvc.perform(delete("/api/admin/quiz/1"))
                .andExpect(status().isNoContent());
    }

    // ✅ Test add question to quiz
    @Test
    void testAddQuestionToQuiz() throws Exception {
        Mockito.when(questionService.addQuestionToQuiz(eq(1L), any(Question.class)))
                .thenReturn(question);

        mockMvc.perform(post("/api/admin/quiz/1/question")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Question added successfully")));
    }

    // ✅ Test get all questions for a quiz
    @Test
    void testGetQuestionsByQuiz() throws Exception {
        List<Question> questions = Arrays.asList(question);
        Mockito.when(questionService.getQuestionsByQuizId(1L)).thenReturn(questions);

        mockMvc.perform(get("/api/admin/quiz/1/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText").value("What is JVM?"));
    }

    // ✅ Test delete question by ID
    @Test
    void testDeleteQuestion() throws Exception {
        mockMvc.perform(delete("/api/admin/question/1"))
                .andExpect(status().isNoContent());
    }
}
