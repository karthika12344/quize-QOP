package com.example.quizonline.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quizonline.project.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    
}
