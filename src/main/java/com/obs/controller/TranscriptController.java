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
        UserEntity student = userService.findByMail(userEmail);

        if (student != null) {
            List<EnrollmentEntity> enrollments = enrollmentService.findEnrollmentsByUserNumber(student.getNumber());
            List<CourseEntity> courses = enrollments.stream()
                    .flatMap(enrollment -> enrollment.getCourseIds().stream().map(courseService::findCourseById))
                    .collect(Collectors.toList());

            double totalCredits = 0;
            double weightedGradesSum = 0;

            for (CourseEntity course : courses) {
                int midtermGrade = enrollments.stream()
                        .map(e -> e.getMidtermGrades().get(course.getId()))
                        .filter(g -> g != null)
                        .findFirst()
                        .orElse(0);

                int finalGrade = enrollments.stream()
                        .map(e -> e.getFinalGrades().get(course.getId()))
                        .filter(g -> g != null)
                        .findFirst()
                        .orElse(0);

                int overallGrade = (midtermGrade * course.getMidtermWeight() + finalGrade * course.getFinalWeight()) / 100;
                String letterGrade = GradeUtils.getLetterGrade(overallGrade);
                double gradePoint = GradeUtils.getGradePoint(letterGrade);

                totalCredits += course.getCredits();
                weightedGradesSum += gradePoint * course.getCredits();

                course.setCourseName(course.getCourseName() + " (" + letterGrade + ")");
            }

            double gpa = weightedGradesSum / totalCredits;

            model.addAttribute("student", student);
            model.addAttribute("courses", courses);
            model.addAttribute("totalCredits", totalCredits);
            model.addAttribute("gpa", gpa);
        }

        return "transcript";
    }
}
