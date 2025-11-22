package com.example.quizonline.project.controller;

import com.example.quizonline.project.entity.Question;
import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.service.QuestionService;
import com.example.quizonline.project.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/participant")
public class ParticipantController {

    @Autowired private QuizService quizService;
    @Autowired private QuestionService questionService;

    // Get all available quizzes
    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    // Get quiz (summary) + questions separately via questions endpoint
    @GetMapping("/quiz/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    // Get questions for a quiz
    @GetMapping("/quiz/{id}/questions")
    public ResponseEntity<List<Question>> getQuizQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionsByQuizId(id));
    }
}
