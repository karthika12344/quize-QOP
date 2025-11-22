package com.example.quizonline.project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quizonline.project.entity.UserAnswer;
import com.example.quizonline.project.repository.UserAnswerRepository;

import java.util.List;

@Service
public class UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public List<UserAnswer> saveAllAnswers(List<UserAnswer> answers) {
        return userAnswerRepository.saveAll(answers);
    }

    public List<UserAnswer> getAllAnswers() {
        return userAnswerRepository.findAll();
    }
}

