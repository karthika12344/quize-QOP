package com.example.quizonline.project.testcontroller;

import com.example.quizonline.project.entity.UserAnswer;
import com.example.quizonline.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAnswerControllerTest.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ Disable security for tests
public class UserAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userAnswerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveAllAnswers() throws Exception {
        // ✅ Create test data that matches your entity structure
        UserAnswer ans1 = new UserAnswer(1L, 101L, "A", null);
        UserAnswer ans2 = new UserAnswer(2L, 102L, "B", null);
        List<UserAnswer> inputAnswers = List.of(ans1, ans2);

        when(userAnswerService.saveAllAnswers(inputAnswers)).thenReturn(inputAnswers);

        mockMvc.perform(post("/api/user-answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputAnswers)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].selectedOption").value("A"))
                .andExpect(jsonPath("$[1].selectedOption").value("B"));
    }

    @Test
    void testGetAllAnswers() throws Exception {
        List<UserAnswer> answers = List.of(
                new UserAnswer(1L, 201L, "C", null),
                new UserAnswer(2L, 202L, "D", null)
        );

        when(userAnswerService.getAllAnswers()).thenReturn(answers);

        mockMvc.perform(get("/api/user-answers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].selectedOption").value("C"))
                .andExpect(jsonPath("$[1].selectedOption").value("D"));
    }
}
