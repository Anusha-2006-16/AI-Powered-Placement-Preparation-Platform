package com.placement_prep_platform.placement_prep_platform.controller;

import com.placement_prep_platform.placement_prep_platform.model.Question;
import com.placement_prep_platform.placement_prep_platform.model.TestResult;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.security.JwtUtil;
import com.placement_prep_platform.placement_prep_platform.service.GeminiService;
import com.placement_prep_platform.placement_prep_platform.service.QuestionService;
import com.placement_prep_platform.placement_prep_platform.service.TestResultService;
import com.placement_prep_platform.placement_prep_platform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    private final UserService userService;
    private final QuestionService questionService;
    private final TestResultService  testResultService;
    private final JwtUtil jwtUtil;
    public TestController(QuestionService questionService, TestResultService testResultService, JwtUtil jwtUtil, UserService userService,
                          GeminiService geminiService) {
        this.questionService = questionService;
        this.testResultService = testResultService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }
    @GetMapping("/start")
    public String startTest(Model model) {
        List<Question> questions=questionService.getAllQuestions();
        model.addAttribute("questions",questions);
        return "mock-test";
    }
    @PostMapping("/submit")
    public String submitTest(Model model,
                             HttpSession session,
                             HttpServletRequest request){
        List<Question> questions=questionService.getAllQuestions();
        int score=0;
        for(Question q:questions){
            String answer=request.getParameter("q"+q.getId());
            if(answer != null && answer.equals(q.getCorrectAnswer())){
                score++;
            }
        }
        String token=(String) session.getAttribute("jwt");
        if(token == null){
            return "redirect:/auth/login";
        }
        String email=jwtUtil.extractEmail(token);
        User user=userService.getUserByEmail(email);
        TestResult testResult=new TestResult();
        testResult.setScore(score);
        testResult.setUser(user);

        testResult.setTestDate(LocalDateTime.now());

        testResultService.saveResult(testResult);

        model.addAttribute("score",score);
        model.addAttribute("total",questions.size());
        return "test-result";
    }

    @GetMapping("/history")
    public String testHistory(Model model,HttpSession session){
        String token=(String) session.getAttribute("jwt");
        if(token == null){
            return "redirect:/auth/login";
        }
        String email=jwtUtil.extractEmail(token);
        User user=userService.getUserByEmail(email);
        List<TestResult> results=testResultService.getResultsByUser(user);
        model.addAttribute("results",results);

        int totalTests=results.size();
        int highestScore=0;
        float avgScore=0   ;
        int latestScore=0;

        if(!results.isEmpty()){
            int sum=0;
            for(TestResult result:results){
                highestScore=Math.max(highestScore,result.getScore());
                sum+=result.getScore();
            }
            avgScore=(float)sum/totalTests;
            latestScore=results.get(results.size()-1).getScore();
        }
    Integer score=latestScore;
        String status;
        if(score>=80){
            status="Excellent";
        }else if(score>=60){
            status="Good";
        }
        else{
            status="Needs Improvement";
        }
        model.addAttribute("highestScore",highestScore);
        model.addAttribute("avgScore",avgScore);
        model.addAttribute("latestScore",latestScore);
        model.addAttribute("totalTests",totalTests);
        model.addAttribute("status",status);
        return "test-history";
    }


}
