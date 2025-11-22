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
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    // ✅ Create quiz
    @PostMapping("/quiz")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz savedQuiz = quizService.saveQuiz(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    // ✅ Get all quizzes
    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    // ✅ Get quiz by ID
    @GetMapping("/quiz/{id}")
    public ResponseEntity<?> getQuiz(@PathVariable Long id) {
        try {
            Quiz quiz = quizService.getQuizById(id);
            return ResponseEntity.ok(quiz);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Delete quiz
    @DeleteMapping("/quiz/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Add question to a quiz
   @PostMapping("/quiz/{quizId}/question")
public ResponseEntity<String> addQuestionToQuiz(@PathVariable Long quizId, @RequestBody Question question) {
    Question savedQuestion = questionService.addQuestionToQuiz(quizId, question);
    String message = "✅ Question added successfully to quiz " 
                     + quizId + ": " + savedQuestion.getQuestionText();
    return ResponseEntity.ok(message);
}


    // ✅ Get all questions for a quiz
    @GetMapping("/quiz/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable Long quizId) {
        List<Question> questions = questionService.getQuestionsByQuizId(quizId);
        return ResponseEntity.ok(questions);
    }

    // ✅ Delete question by ID
    @DeleteMapping("/question/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
