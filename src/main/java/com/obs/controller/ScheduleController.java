package com.obs.controller;

import com.obs.entities.CourseEntity;
import com.obs.entities.UserEntity;
import com.obs.services.EnrollmentService;
import com.obs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ScheduleController {

    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public ScheduleController(EnrollmentService enrollmentService, UserService userService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);

        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            List<CourseEntity> courses = enrollmentService.findCoursesByUserNumber(user.getNumber());
            model.addAttribute("courses", courses);
        }

        return "schedule";
    }
}
