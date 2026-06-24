package com.placement_prep_platform.placement_prep_platform.controller;

import com.placement_prep_platform.placement_prep_platform.dto.DashboardDTO;
import com.placement_prep_platform.placement_prep_platform.dto.LoginRequest;
import com.placement_prep_platform.placement_prep_platform.model.User;
import com.placement_prep_platform.placement_prep_platform.security.JwtUtil;
import com.placement_prep_platform.placement_prep_platform.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/auth")

public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/register")

    public String registerUser(Model model, User user){
       model.addAttribute("user",new User());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user){
      userService.registerUser(user);
        return "redirect:/auth/login";
    }
    @GetMapping("/login")
    public String loginUser(Model model, User user){

        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute LoginRequest loginRequest,
            HttpSession session
    ){

        String token =
                userService.loginUser(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                );

        session.setAttribute(
                "jwt",
                token
        );

        return "redirect:/auth/dashboard";
    }

//   @GetMapping("/dashboard")
//    public String dashboard(Model model,HttpSession session){
//        String token=(String) session.getAttribute("jwt");
//        if(token == null){
//            return "redirect:/auth/login";
//        }
//        String email=jwtUtil.extractEmail(token);
//        User user=userService.getUserByEmail(email);
//        model.addAttribute("user",user);
//
//       DashboardDTO dashboard=userService.getDashboard(email);
//       model.addAttribute("dashboard",dashboard);
//        return "dashboard";
//   }



}
