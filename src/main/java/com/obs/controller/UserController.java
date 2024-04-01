package com.obs.controller;

import com.obs.entities.UserEntity;
import com.obs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /*
    @GetMapping("/byNumber/{number}")
    public void getUserByNumber(@PathVariable Long number) {
        userService.printUserInfoByNumber(number);
    }
    */
    
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "search"; 
    }

    @PostMapping("/search")
    public String searchUser(@RequestParam("number") Long number, Model model) {

        UserEntity user = userService.findByNumber(number);
        model.addAttribute("user", user);
        return "search"; 
    }

    
    
    
    
}
