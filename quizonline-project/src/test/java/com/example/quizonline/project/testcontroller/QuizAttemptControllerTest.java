package com.example.quizonline.project.testcontroller;

import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.entity.QuizAttempt;
import com.example.quizonline.project.entity.User;
import com.example.quizonline.project.service.EmailService;
import com.example.quizonline.project.service.QuizAttemptService;
import com.example.quizonline.project.service.QuizService;
import com.example.quizonline.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(QuizAttempt.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ Disable security for tests
public class QuizAttemptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private QuizAttemptService attemptService;
    @MockBean private UserService userService;
    @MockBean private QuizService quizService;
    @MockBean private EmailService emailService;

    private User user;
    private Quiz quiz;
    private QuizAttempt attempt;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Athish");
        user.setEmail("user@example.com");

        quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Spring Boot Basics");

        attempt = new QuizAttempt();
        attempt.setId(1L);
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setScore(8);
    }

    // ✅ Test 1: Submit attempt (JSON body)
    @Test
    void testSubmitAttempt() throws Exception {
        Mockito.when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        Mockito.when(quizService.getQuizById(anyLong())).thenReturn(quiz);
        Mockito.when(attemptService.saveAttempt(any(QuizAttempt.class))).thenReturn(attempt);

        mockMvc.perform(post("/api/attempts/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user\":{\"email\":\"user@example.com\"},\"quiz\":{\"id\":1},\"score\":8}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(8))
                .andExpect(jsonPath("$.quiz.title").value("Spring Boot Basics"));
    }

    // ✅ Test 2: Submit by params
    @Test
    void testSubmitByParams() throws Exception {
        Mockito.when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        Mockito.when(quizService.getQuizById(anyLong())).thenReturn(quiz);
        Mockito.when(attemptService.saveAttempt(any(User.class), any(Quiz.class), anyInt())).thenReturn(attempt);

        mockMvc.perform(post("/api/attempts/submit/by")
                .param("email", "user@example.com")
                .param("quizId", "1")
                .param("score", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quiz.title").value("Spring Boot Basics"));
    }

    // ✅ Test 3: Get attempts by user
    @Test
    void testGetAttemptsByUser() throws Exception {
        Mockito.when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        Mockito.when(attemptService.getAttemptsByUser(any(User.class)))
                .thenReturn(Collections.singletonList(attempt));

        mockMvc.perform(get("/api/attempts/user")
                .param("email", "user@example.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(8));
    }

    // ✅ Test 4: Get all attempts
    @Test
    void testGetAllAttempts() throws Exception {
        Mockito.when(attemptService.getAllAttempts())
                .thenReturn(Collections.singletonList(attempt));

        mockMvc.perform(get("/api/attempts/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quiz.title").value("Spring Boot Basics"));
    }
}

