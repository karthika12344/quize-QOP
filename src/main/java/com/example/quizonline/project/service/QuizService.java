package com.example.quizonline.project.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.repository.QuizRepository;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    // ✅ Create or Save a Quiz
    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    // ✅ Get all quizzes
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // ✅ Get quiz by ID (throws error if not found)
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
    }

    // ✅ Update quiz
    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    // ✅ Delete quiz by ID
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public <Quiz> Quiz saveQuiz(Quiz quiz) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveQuiz'");
    }
}
