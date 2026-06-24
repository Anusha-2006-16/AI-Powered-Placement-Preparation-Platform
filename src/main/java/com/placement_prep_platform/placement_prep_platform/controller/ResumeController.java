package com.placement_prep_platform.placement_prep_platform.controller;

import com.placement_prep_platform.placement_prep_platform.dto.InterviewEvaluationResponse;
import com.placement_prep_platform.placement_prep_platform.model.InterviewAnswer;
import com.placement_prep_platform.placement_prep_platform.model.InterviewResult;
import com.placement_prep_platform.placement_prep_platform.model.Resume;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.repository.InterviewAnswerRepository;
import com.placement_prep_platform.placement_prep_platform.repository.InterviewResultRepository;
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
import java.time.LocalDateTime;
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
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final InterviewResultRepository interviewResultRepository;
    public ResumeController(ResumeService resumeService, JwtUtil jwtUtil, UserService userService, GeminiService geminiService, InterviewQuestionService interviewQuestionService,
                            InterviewAnswerRepository interviewAnswerRepository,InterviewResultRepository interviewResultRepository) {
        this.resumeService = resumeService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.geminiService = geminiService;
        this.interviewQuestionService = interviewQuestionService;
        this.interviewAnswerRepository = interviewAnswerRepository;
        this.interviewResultRepository = interviewResultRepository;
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
                                  Model model,
                                  HttpSession session) {

        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String q = request.getParameter("question" + i);
            String a = request.getParameter("answer" + i);

            if (q != null && a != null) {
                questions.add(q);
                answers.add(a);
            }
        }

        String token = (String) session.getAttribute("jwt");
        String email = jwtUtil.extractEmail(token);
        User user = userService.getUserByEmail(email);

        int totalScore = 0;

        for (int i = 0; i < questions.size(); i++) {

            InterviewEvaluationResponse evaluation =
                    geminiService.evaluateAnswer(
                            questions.get(i),
                            answers.get(i)
                    );

            InterviewAnswer ia = new InterviewAnswer();
            ia.setQuestion(questions.get(i));
            ia.setAnswer(answers.get(i));
            ia.setScore(evaluation.getScore());
            ia.setFeedback(evaluation.getFeedback());
            ia.setUser(user);

            totalScore += evaluation.getScore();

            interviewAnswerRepository.save(ia);
        }

        String report =
                geminiService.evaluateFullInterview(
                        questions,
                        answers
                );

        double percentage = questions.isEmpty()
                ? 0
                : ((double) totalScore / (questions.size() * 10)) * 100;

        InterviewResult interviewResult = new InterviewResult();
        interviewResult.setUser(user);
        interviewResult.setTotalScore(totalScore);
        interviewResult.setPercentage(percentage);
        interviewResult.setOverallFeedback(report);
        interviewResult.setTakenAt(LocalDateTime.now());

        interviewResultRepository.save(interviewResult);

        model.addAttribute("report", report);
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("percentage", percentage);

        return "interview-summary";
    }

@GetMapping("/interview/history")
    public String interviewHistory(Model model,HttpSession session){
    String token=(String) session.getAttribute("jwt");
    String email=jwtUtil.extractEmail(token);
    User user=userService.getUserByEmail(email);
    model.addAttribute("results",interviewResultRepository.findByUserOrderByTakenAtDesc(user));
    return "interview-history";
}

}