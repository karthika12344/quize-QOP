package com.example.quizonline.project.controller;

import com.example.quizonline.project.entity.UserAnswer;
import com.example.quizonline.project.service.UserAnswerService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/user-answers")
public class UserAnswerController {

    @Autowired private UserAnswerService userAnswerService;

    @PostMapping
    public ResponseEntity<List<UserAnswer>> saveAll(@RequestBody List<UserAnswer> answers) {
        return ResponseEntity.ok(userAnswerService.saveAllAnswers(answers));
    }

    @GetMapping
    public ResponseEntity<List<UserAnswer>> getAll() {
        return ResponseEntity.ok(userAnswerService.getAllAnswers());
    }
}

