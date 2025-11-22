package com.example.quizonline.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quizonline.project.entity.QuizAttempt;
import com.example.quizonline.project.entity.User;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt,Long>{

    List<QuizAttempt> findByUser(User user);
    
}
