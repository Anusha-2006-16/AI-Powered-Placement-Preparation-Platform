package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.dto.AtsResponse;
import com.placement_prep_platform.placement_prep_platform.dto.ResumeAnalysisResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {

    private final GeminiService geminiService;
    public AiService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }
    public ResumeAnalysisResponse analyze(String resumeText){

        return  geminiService.analyzeResume(resumeText);
    }

    public List<String> generateQuestions(String resumeText){
        return geminiService.generateQuestions(resumeText);
    }
}
