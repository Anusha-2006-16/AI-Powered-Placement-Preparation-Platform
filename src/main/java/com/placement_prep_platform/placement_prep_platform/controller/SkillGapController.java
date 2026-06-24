package com.placement_prep_platform.placement_prep_platform.controller;

import com.placement_prep_platform.placement_prep_platform.model.Resume;
import com.placement_prep_platform.placement_prep_platform.model.SkillGap;
import com.placement_prep_platform.placement_prep_platform.service.ResumeService;
import com.placement_prep_platform.placement_prep_platform.service.SkillGapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/skills")
public class SkillGapController {

    private final ResumeService resumeService;
    private final SkillGapService skillGapService;

    public SkillGapController(ResumeService resumeService, SkillGapService skillGapService) {
        this.resumeService = resumeService;
        this.skillGapService = skillGapService;
    }


    @GetMapping("/{id}")
    public String skillGapTracker(@PathVariable Long id, Model model) {

        Resume resume = resumeService.findById(id);

        List<SkillGap> skills = skillGapService.getSkills(resume.getUser());

        model.addAttribute("resume", resume);
        model.addAttribute("skills", skills);

        return "skills";
    }

    @PostMapping("/complete/{id}")
    public String completeSkill(@PathVariable Long id){
        SkillGap skill=skillGapService.findById(id);
        skill.setCompleted(true);
        skillGapService.save(skill);
        return "redirect:/skills/"+skill.getUser().getId();
    }
}