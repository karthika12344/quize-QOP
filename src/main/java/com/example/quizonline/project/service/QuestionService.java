package com.example.quizonline.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quizonline.project.entity.Question;
import com.example.quizonline.project.entity.Quiz;
import com.example.quizonline.project.repository.QuestionRepository;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizService quizService;

    // ✅ Add a single question directly
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    // ✅ Add question to a specific quiz
    public Question addQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizService.getQuizById(quizId);
        question.setQuiz(quiz);
        return questionRepository.save(question);
    }

    // ✅ Get all questions for a quiz
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    // ✅ Delete a question by ID
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
