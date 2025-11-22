package com.example.quizonline.project.testservice;


import com.example.quizonline.project.entity.UserAnswer;
import com.example.quizonline.project.repository.UserAnswerRepository;
import com.example.quizonline.project.service.UserAnswerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAnswerServiceTest {

    @Mock
    private UserAnswerRepository userAnswerRepository;

    @InjectMocks
    private UserAnswerService userAnswerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAllAnswers() {
        UserAnswer ans1 = new UserAnswer();
        UserAnswer ans2 = new UserAnswer();
        List<UserAnswer> answers = List.of(ans1, ans2);

        when(userAnswerRepository.saveAll(answers)).thenReturn(answers);

        List<UserAnswer> result = userAnswerService.saveAllAnswers(answers);

        assertEquals(2, result.size());
        verify(userAnswerRepository, times(1)).saveAll(answers);
    }

    @Test
    void testGetAllAnswers() {
        when(userAnswerRepository.findAll()).thenReturn(List.of(new UserAnswer(), new UserAnswer()));

        List<UserAnswer> result = userAnswerService.getAllAnswers();

        assertEquals(2, result.size());
        verify(userAnswerRepository, times(1)).findAll();
    }
}
