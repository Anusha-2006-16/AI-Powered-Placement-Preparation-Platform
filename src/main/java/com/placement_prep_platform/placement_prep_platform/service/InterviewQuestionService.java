package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.model.InterviewQuestion;
import com.placement_prep_platform.placement_prep_platform.model.Resume;
import com.placement_prep_platform.placement_prep_platform.repository.InterviewQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewQuestionService {
    private final InterviewQuestionRepository interviewQuestionRepository;
    public InterviewQuestionService(InterviewQuestionRepository interviewQuestionRepository) {
        this.interviewQuestionRepository = interviewQuestionRepository;
    }
    public void saveQuestion(Resume resume, List<String> questions) {
        for (String q:questions){
            InterviewQuestion iq=new InterviewQuestion();
            iq.setQuestion(q);
            iq.setResume(resume);
            interviewQuestionRepository.save(iq);
        }
    }
    public List<InterviewQuestion> getQuestions(Resume resume) {
        return interviewQuestionRepository.findByResume(resume);
    }

}
