package com.example.quizonline.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quizonline.project.entity.Question;

public interface QuestionRepository extends JpaRepository<Question,Long> {

     List<Question> findByQuizId(Long quizId);
    
}
