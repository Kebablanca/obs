package com.obs.controller;

import org.springframework.stereotype.Controller;

import com.obs.entities.UserEntity;
import com.obs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class StaffController {
    private final UserService userService;

    @Autowired
    public StaffController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String listUsers(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        
        List<UserEntity> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        model.addAttribute("user", new UserEntity());
        return "addUser";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute UserEntity user, Model model) {
    	try {
        userService.createUser(user.getMail(), user.getPassword(), user.getFirstName(), user.getLastName(),user.getNumber(), user.getRole(), user.getDepartment());
        return "redirect:/users/";
    	}catch(IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "addUser";
    	}
    }

    @PostMapping("/delete/{number}")
    public String deleteUser(@PathVariable Long number) {
        userService.deleteUserByNumber(number);
        return "redirect:/users/";
    }
}
