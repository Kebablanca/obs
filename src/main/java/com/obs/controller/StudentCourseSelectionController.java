package com.obs.controller;

import com.obs.entities.CourseEntity;
import com.obs.entities.CourseSelectionSettingEntity;
import com.obs.entities.EnrollmentEntity;
import com.obs.entities.UserEntity;
import com.obs.services.CourseSelectionSettingService;
import com.obs.services.CourseService;
import com.obs.services.EnrollmentService;
import com.obs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/course-selection")
public class StudentCourseSelectionController {

    private final CourseSelectionSettingService courseSelectionSettingService;
    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public StudentCourseSelectionController(CourseSelectionSettingService courseSelectionSettingService, EnrollmentService enrollmentService, UserService userService, CourseService courseService) {
        this.courseSelectionSettingService = courseSelectionSettingService;
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/select")
    public String showCourseSelectionForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity student = userService.findByMail(userEmail);
        
        if (student != null) {
            List<CourseSelectionSettingEntity> settings = courseSelectionSettingService.getSettingsByDepartment(student.getDepartment());
            List<CourseEntity> availableCourses = settings.stream()
                .flatMap(setting -> courseService.findCoursesByIds(setting.getCourseIds()).stream())
                .distinct()
                .collect(Collectors.toList());
            model.addAttribute("courses", availableCourses);
            model.addAttribute("selectedCourses", new EnrollmentEntity());
        }
        
        return "studentCourseSelection";
    }

    @PostMapping("/select")
    public String saveCourseSelection(@ModelAttribute EnrollmentEntity selectedCourses, Model model) {
        // Kredi kontrolü ve kayıt işlemi burada yapılacak
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity student = userService.findByMail(userEmail);
        
        if (student != null) {
            List<EnrollmentEntity> currentEnrollments = enrollmentService.findEnrollmentsByUserNumber(student.getNumber());
            int currentCredits = currentEnrollments.stream()
                    .flatMap(enrollment -> enrollment.getCourseIds().stream())
                    .map(courseService::findCourseById)
                    .filter(course -> course != null)
                    .mapToInt(CourseEntity::getCredits)
                    .sum();
            
            List<CourseEntity> newCourses = courseService.findCoursesByIds(selectedCourses.getCourseIds());
            int newCredits = newCourses.stream().mapToInt(CourseEntity::getCredits).sum();
            
            if (currentCredits + newCredits <= 45) {
                selectedCourses.setUserNumber(student.getNumber());
                enrollmentService.saveEnrollments(selectedCourses);
                return "redirect:/student/course-selection/select";
            } else {
                model.addAttribute("error", "Toplam kredi 45'i geçemez.");
                return "studentCourseSelection";
            }
        }
        
        return "redirect:/login";
    }
}
