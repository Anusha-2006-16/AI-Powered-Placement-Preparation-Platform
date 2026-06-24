package com.placement_prep_platform.placement_prep_platform.service;

import com.placement_prep_platform.placement_prep_platform.dto.ResumeAnalysisResponse;
import com.placement_prep_platform.placement_prep_platform.model.InterviewQuestion;
import com.placement_prep_platform.placement_prep_platform.model.Resume;
import com.placement_prep_platform.placement_prep_platform.model.SkillGap;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.repository.InterviewQuestionRepository;
import com.placement_prep_platform.placement_prep_platform.repository.ResumeRepository;
import com.placement_prep_platform.placement_prep_platform.repository.SkillGapRepository;
import com.placement_prep_platform.placement_prep_platform.repository.UserRepository;
import com.placement_prep_platform.placement_prep_platform.utils.PdfUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    private ResumeRepository resumeRepository;
    private UserRepository userRepository;
    private  AiService aiService;
    private  GeminiService geminiService;
    private final InterviewQuestionService interviewQuestionService;
    private InterviewQuestionRepository interviewQuestionRepository;
   private final SkillGapRepository skillGapRepository;
    public ResumeService(ResumeRepository resumeRepository,UserRepository userRepository,AiService aiService
                   ,InterviewQuestionService interviewQuestionService,      GeminiService geminiService,InterviewQuestionRepository interviewQuestionRepository,
                         SkillGapRepository skillGapRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.aiService = aiService;
        this.geminiService = geminiService;
        this.interviewQuestionService = interviewQuestionService;
        this.interviewQuestionRepository = interviewQuestionRepository;
        this.skillGapRepository = skillGapRepository;
    }

    public void uploadResume(MultipartFile file,String email)
        throws IOException {
        String uploadDir="uploads/";
        String fileName=file.getOriginalFilename();
        Path path= Paths.get(uploadDir+fileName);
        Files.copy(
                file.getInputStream(),
                path,
                StandardCopyOption.REPLACE_EXISTING
        );
        User user=userRepository.findByEmail(email).orElseThrow();
        String extractedText= PdfUtil.extractText(path.toString());
        Resume resume=new Resume();
        resume.setFileName(fileName);
        resume.setFilePath(path.toString());
        resume.setUploadDate(LocalDateTime.now());
        resume.setExtractedText(extractedText);
        resume.setUser(user);

    resumeRepository.save(resume);

    }

    public Resume findById(Long id) {
        return resumeRepository.findById(id).orElseThrow();
    }

    public void analyzeResume(Long id){
        Resume resume=resumeRepository.findById(id).orElseThrow();
        String text=resume.getExtractedText();
        ResumeAnalysisResponse result =aiService.analyze(text);
        resume.setAtsScore(result.getAtsScore());
        resume.setMissingSkills(result.getMissingSkills());
        String missingSkills=result.getMissingSkills();
        String[] skills=missingSkills.split(",");
        for(String skill:skills){
            SkillGap gap=new SkillGap();
            gap.setSkillName(skill.trim());
            gap.setUser(resume.getUser());
            skillGapRepository.save(gap);
        }
        resume.setLatestFeedback(result.getFeedback());
          resumeRepository.save(resume);
        System.out.println("ATS SAVED = " + result.getAtsScore());
    }


    public List<InterviewQuestion> getQuestions(Long resumeId){
        Resume resume=resumeRepository.findById(resumeId).orElseThrow();

        return interviewQuestionRepository.findByResume(resume);
    }

    @Transactional
    public void generateInterviewQuestions(Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId).orElseThrow();
List<InterviewQuestion> existingQuestions=
        interviewQuestionRepository.findByResume(resume);
if(!existingQuestions.isEmpty()){
    System.out.println("Question already exist for resume "+resumeId);
    return ;
}
        List<String> questions =
                geminiService.generateQuestions(resume.getExtractedText());
        System.out.println("Questions generated = " + questions.size());
        if(questions.isEmpty()){
            throw new RuntimeException(
                    "Unable to generate questions.Gemini service unavaiable."
            );
        }
        interviewQuestionService.saveQuestion(resume, questions);
    }








}
