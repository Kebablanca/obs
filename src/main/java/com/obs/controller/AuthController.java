package com.obs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.obs.entities.UserEntity;
import com.obs.repositories.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    @GetMapping("/success")
    public String loginSuccess() {
        return "success"; 
    }

    @PostMapping("/login")
    public String loginProcess(Model model, long Number, String password) {
        UserEntity user = userRepository.findByNumber(Number).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/success"; 
        } else {
            model.addAttribute("error", true);
            return "login"; 
        }
    }
}