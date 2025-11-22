package com.example.quizonline.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quizonline.project.entity.UserAnswer;

public interface UserAnswerRepository extends JpaRepository<UserAnswer,Long> {
    
}
