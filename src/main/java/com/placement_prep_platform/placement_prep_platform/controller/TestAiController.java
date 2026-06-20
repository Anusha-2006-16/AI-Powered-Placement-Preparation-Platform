package com.placement_prep_platform.placement_prep_platform.controller;

import com.placement_prep_platform.placement_prep_platform.service.GeminiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestAiController {
    private final GeminiService geminiService;
    public TestAiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }
    @GetMapping("/test-ai")
    @ResponseBody
    public String test(){
        return geminiService.testGemini();
    }
}
