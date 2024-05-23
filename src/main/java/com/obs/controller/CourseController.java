package com.obs.controller;

import com.obs.entities.CourseEntity;
import com.obs.entities.UserEntity;
import com.obs.services.CourseService;
import com.obs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping("/courses/add")
    public String showAddCourseForm(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        CourseEntity course = new CourseEntity();
        model.addAttribute("course", course);
        List<UserEntity> teachers = userService.findUsersByRole(UserEntity.UserRole.ROLE_TEACHER);
        model.addAttribute("teachers", teachers);
        return "createCourse";
    }

    
    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute CourseEntity course) {
    	UserEntity teacher = userService.findByMail(course.getInstructor());
        if (teacher != null) {
            course.setInstructor(teacher.getFirstName() + " " + teacher.getLastName());
            course.setInstructorNumber(teacher.getNumber());
        }
        courseService.saveCourse(course);
        return "redirect:/courses/add";
    }
}