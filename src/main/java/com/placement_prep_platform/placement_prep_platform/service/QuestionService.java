package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.model.Question;
import com.placement_prep_platform.placement_prep_platform.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow();
    }
}
