package com.obs.controller;

import com.obs.entities.CourseEntity;
import com.obs.entities.EnrollmentEntity;
import com.obs.entities.UserEntity;
import com.obs.services.CourseService;
import com.obs.services.EnrollmentService;
import com.obs.services.UserService;
import com.obs.utils.GradeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TranscriptController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public TranscriptController(EnrollmentService enrollmentService, UserService userService, CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/transcript")
    public String getTranscript(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }

        if (user != null) {
            List<EnrollmentEntity> enrollments = enrollmentService.findEnrollmentsByUserNumber(user.getNumber());
            List<CourseWithGrades> courseWithGradesList = new ArrayList<>();

            double totalCredits = 0;
            double weightedGradesSum = 0;

            for (EnrollmentEntity enrollment : enrollments) {
                for (String courseId : enrollment.getCourseIds()) {
                    CourseEntity course = courseService.findCourseById(courseId);
                    if (course != null) {
                        Integer midtermGrade = enrollment.getMidtermGrades().get(courseId);
                        Integer finalGrade = enrollment.getFinalGrades().get(courseId);
                        if (midtermGrade == null) midtermGrade = 0;
                        if (finalGrade == null) finalGrade = 0;

                        int overallGrade = (midtermGrade * course.getMidtermWeight() + finalGrade * course.getFinalWeight()) / 100;
                        String letterGrade = GradeUtils.getLetterGrade(overallGrade);
                        double gradePoint = GradeUtils.getGradePoint(letterGrade);

                        totalCredits += course.getCredits();
                        weightedGradesSum += gradePoint * course.getCredits();

                        courseWithGradesList.add(new CourseWithGrades(course, midtermGrade, finalGrade, letterGrade));
                    }
                }
            }

            double gpa = weightedGradesSum / totalCredits;

            model.addAttribute("student", user);
            model.addAttribute("courses", courseWithGradesList);
            model.addAttribute("totalCredits", totalCredits);
            model.addAttribute("gpa", gpa);
        }

        return "transcript";
    }

    private static class CourseWithGrades {
        private final CourseEntity course;
        private final int midtermGrade;
        private final int finalGrade;
        private final String letterGrade;

        public CourseWithGrades(CourseEntity course, int midtermGrade, int finalGrade, String letterGrade) {
            this.course = course;
            this.midtermGrade = midtermGrade;
            this.finalGrade = finalGrade;
            this.letterGrade = letterGrade;
        }

        public CourseEntity getCourse() {
            return course;
        }

        public int getMidtermGrade() {
            return midtermGrade;
        }

        public int getFinalGrade() {
            return finalGrade;
        }

        public String getLetterGrade() {
            return letterGrade;
        }
    }
}
