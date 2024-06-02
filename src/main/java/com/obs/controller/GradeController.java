package com.obs.controller;

import com.obs.entities.CourseEntity;
import com.obs.entities.EnrollmentEntity;
import com.obs.entities.UserEntity;
import com.obs.services.CourseService;
import com.obs.services.EnrollmentService;
import com.obs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GradeController {

    private final CourseService courseService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public GradeController(CourseService courseService, UserService userService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/instructorCourses")
    public String instructorCourses(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }

        if (user != null) {
            List<CourseEntity> courses = courseService.findCoursesByInstructorNumber(user.getNumber());
            model.addAttribute("courses", courses);
        }

        return "instructorCourses";
    }

    @GetMapping("/instructor-course-{courseId}")
    public String getCourseStudents(@PathVariable String courseId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }

        if (user != null) {
            CourseEntity course = courseService.findCourseById(courseId);
            if (course != null && course.getInstructorNumber().equals(user.getNumber())) {
                List<EnrollmentEntity> enrollments = enrollmentService.findEnrollmentsByCourseId(courseId);
                List<UserEntity> students = enrollments.stream()
                        .map(enrollment -> userService.findByNumber(enrollment.getUserNumber()))
                        .collect(Collectors.toList());

                model.addAttribute("course", course);
                model.addAttribute("students", students);
                model.addAttribute("enrollments", enrollments);
            }
        }

        return "courseStudents";
    }

    @PostMapping("/instructor/course/{courseId}/students/grades")
    public String updateStudentGrades(@PathVariable String courseId,
                                      @RequestParam List<Long> studentNumbers,
                                      @RequestParam List<Integer> midtermGrades,
                                      @RequestParam List<Integer> finalGrades) {
        for (int i = 0; i < studentNumbers.size(); i++) {
            EnrollmentEntity enrollment = enrollmentService.findByUserNumberAndCourseId(studentNumbers.get(i), courseId);
            if (enrollment != null) {
                enrollment.getMidtermGrades().put(courseId, midtermGrades.get(i));
                enrollment.getFinalGrades().put(courseId, finalGrades.get(i));
                enrollmentService.saveEnrollment(enrollment);
            }
        }

        return "redirect:/instructor-course-" + courseId ;
    }
}
