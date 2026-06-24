package com.placement_prep_platform.placement_prep_platform.controller;

import com.placement_prep_platform.placement_prep_platform.model.DashboardResponse;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.repository.UserRepository;
import com.placement_prep_platform.placement_prep_platform.service.DashboardService;
import com.placement_prep_platform.placement_prep_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user=userRepository.findByEmail(authentication.getName()).orElseThrow();
        DashboardResponse dashboard=dashboardService.getDashboard(user);
        model.addAttribute("dashboard",dashboard);
        model.addAttribute("user",user);
        return "dashboard";
    }
}
