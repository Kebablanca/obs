package com.obs.controller;

import com.obs.entities.Announcement;
import com.obs.entities.UserEntity;
import com.obs.repositories.AnnouncementRepository;
import com.obs.repositories.UserRepository;
import com.obs.services.AnnouncementService;
import com.obs.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {

	private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AnnouncementService announcementService;
    
    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder , UserRepository userRepository,AnnouncementService announcementService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.announcementService = announcementService;
    }
    
    
    
    @GetMapping("/")
    public String defaultPage(HttpServletRequest request, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);

        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        
        

        if (request.isUserInRole("ROLE_STAFF")) {
            List<Announcement> announcements = announcementService.findAll();
            model.addAttribute("announcements", announcements);
            model.addAttribute("announcement", new Announcement());  // Ensure this line is present
            return "adminPage";
        } else if (request.isUserInRole("ROLE_STUDENT")) {
            List<Announcement> announcements = announcementService.findAll();
            model.addAttribute("announcements", announcements);
            return "userPage";
        }
        else if (request.isUserInRole("ROLE_TEACHER")) {
            List<Announcement> announcements = announcementService.findAll();
            model.addAttribute("announcements", announcements);
            return "teacherPage";
        }else {
            return "errorPage"; 
        }
    }
    
    @GetMapping("/academic")
    public String academicPage(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        return "academic"; 
    }

    
    @GetMapping("/search")
    public String showSearchForm(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        model.addAttribute("user", new UserEntity());
        return "search"; 
    }
    
    @GetMapping("/tsearch")
    public String showSearchFormt(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        model.addAttribute("user", new UserEntity());
        return "tsearch"; 
    }
    @PostMapping("/search")
    public String searchUser(@RequestParam("number") Long number, Model model) {

        UserEntity user = userService.findByNumber(number);
        model.addAttribute("user", user);
        return "search"; 
    }

    @PostMapping("/tsearch")
    public String searchUsert(@RequestParam("number") Long number, Model model) {

        UserEntity user = userService.findByNumber(number);
        model.addAttribute("user", user);
        return "tsearch"; 
    }
       
}