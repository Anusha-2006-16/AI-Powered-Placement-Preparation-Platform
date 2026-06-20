package com.placement_prep_platform.placement_prep_platform.controller;

import com.placement_prep_platform.placement_prep_platform.dto.InterviewEvaluationResponse;
import com.placement_prep_platform.placement_prep_platform.model.InterviewAnswer;
import com.placement_prep_platform.placement_prep_platform.model.InterviewResult;
import com.placement_prep_platform.placement_prep_platform.model.Resume;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.security.JwtUtil;
import com.placement_prep_platform.placement_prep_platform.service.GeminiService;
import com.placement_prep_platform.placement_prep_platform.service.InterviewQuestionService;
import com.placement_prep_platform.placement_prep_platform.service.ResumeService;
import com.placement_prep_platform.placement_prep_platform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {
    private final ResumeService resumeService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final GeminiService geminiService;
    private final InterviewQuestionService interviewQuestionService;

    public ResumeController(ResumeService resumeService, JwtUtil jwtUtil, UserService userService, GeminiService geminiService, InterviewQuestionService interviewQuestionService) {
        this.resumeService = resumeService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.geminiService = geminiService;
        this.interviewQuestionService = interviewQuestionService;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload-resume";
    }

    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file,
                               HttpSession session) throws IOException {
        String token = (String) session.getAttribute("jwt");
        if (token == null) {
            return "redirect:/auth/login";
        }
        String email = jwtUtil.extractEmail(token);
        resumeService.uploadResume(file, email);
        return "redirect:/resume/my";
    }


    @GetMapping("/text/{id}")
//    @ResponseBody
    public String getResumeText(Model model, @PathVariable Long id) {
        Resume resume = resumeService.findById(id);
        model.addAttribute("resume", resume);
        return "resume-view";

    }

    @GetMapping("/analyze/{id}")
    public String analyzeResume(Model model,
                                @PathVariable Long id) {
        resumeService.analyzeResume(id);
        return "redirect:/resume/my";
//        Resume resume=resumeService.findById(id);
//        model.addAttribute("resume",resume);
//        return "resume-analysis";
    }

    @GetMapping("/analysis/{id}")
    public String showAnalysis(@PathVariable Long id,
                               Model model) {
        Resume resume = resumeService.findById(id);
        model.addAttribute("resume", resume);
        return "resume-analysis";
    }

    //    User Resumes
    @GetMapping("/my")
    public String userResumes(Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        if (token == null) {
            return "redirect:/auth/login";
        }
        String email = jwtUtil.extractEmail(token);
        User user = userService.getUserByEmail(email);
        List<Resume> resumes = user.getResumes();
        model.addAttribute("user", user);
        model.addAttribute("resumes", resumes);
        return "user-resumes";

    }

    @GetMapping("/analytics")
    public String analytics(Model model,
                            HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        String email = jwtUtil.extractEmail(token);
        model.addAttribute("analytics", userService.getAnalytics(email));
        return "analytics";
    }

    @GetMapping("/questions/{id}")
    public String generateQuestions(
            @PathVariable Long id,
            Model model
    ) {
        Resume resume = resumeService.findById(id);

        model.addAttribute("resume", resume);
        model.addAttribute("questions", interviewQuestionService.getQuestions(resume));
        return "interview-questions";
    }

    @GetMapping("/questions/generate/{id}")
    public String generateQuestions(@PathVariable Long id) {
        resumeService.generateInterviewQuestions(id);
        return "redirect:/resume/questions/" + id;
    }

    @GetMapping("/answer")
    public String answerPage(@RequestParam String question,
                             Model model) {
        model.addAttribute("question", question);
        return "answer-question";
    }

    @PostMapping("/evaluate")
    public String evaluateAnswer(@RequestParam String question,
                                 @RequestParam String answer,
                                 Model model) {
        InterviewEvaluationResponse result =
                geminiService.evaluateAnswer(question, answer);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        model.addAttribute("result", result);
        return "answer-result";

    }

    @PostMapping("/interview/submit")
    public String submitInterview(HttpServletRequest request,
                                  Model model){
        List<String> questions=new ArrayList<>();
        List<String> answers=new ArrayList<>();
        for (int i=0;i<10;i++){
            String question=request.getParameter("question"+i);
            String answer=request.getParameter("answer"+i);
            if(question == null || answer == null){
                continue;
            }
            questions.add(question);
            answers.add(answer);
        }
        String report=
                geminiService.evaluateFullInterview(
                        questions,answers
                );
        model.addAttribute("report", report);
        return "interview-summary";
    }

}