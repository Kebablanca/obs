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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InstructorController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public InstructorController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping("/instructor/courses/update")
    public String updateCourseWeights(@RequestParam("courseId") List<String> courseIds,
                                      @RequestParam("midtermWeight") List<Integer> midtermWeights,
                                      @RequestParam("finalWeight") List<Integer> finalWeights) {
        for (int i = 0; i < courseIds.size(); i++) {
            CourseEntity course = courseService.findCourseById(courseIds.get(i));
            if (course != null) {
                course.setMidtermWeight(midtermWeights.get(i));
                course.setFinalWeight(finalWeights.get(i));
                courseService.saveCourse(course);
            }
        }
        return "redirect:/instructorCourses";
    }
}
