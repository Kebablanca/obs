package com.obs.controller;

import com.obs.entities.CourseEntity;
import com.obs.entities.EnrollmentEntity;
import com.obs.entities.GlobalSettingsEntity;
import com.obs.entities.UserEntity;
import com.obs.services.CourseService;
import com.obs.services.EnrollmentService;
import com.obs.services.GlobalSettingsService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/course-selection")
public class StudentCourseSelectionController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final CourseService courseService;
    private final GlobalSettingsService globalSettingsService;

    @Autowired
    public StudentCourseSelectionController(EnrollmentService enrollmentService, UserService userService, CourseService courseService, GlobalSettingsService globalSettingsService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.courseService = courseService;
        this.globalSettingsService = globalSettingsService;
    }

    @GetMapping("/select")
    public String showCourseSelectionForm(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }

        if (user != null) {
            GlobalSettingsEntity globalSettings = globalSettingsService.getGlobalSettings();
            LocalDate currentDate = LocalDate.now();
            boolean isWithinDateRange = currentDate.isAfter(LocalDate.parse(globalSettings.getStartDate())) 
                                        && currentDate.isBefore(LocalDate.parse(globalSettings.getEndDate()));

            model.addAttribute("isWithinDateRange", isWithinDateRange);

            List<EnrollmentEntity> enrollments = enrollmentService.findEnrollmentsByUserNumber(user.getNumber());
            Set<String> enrolledCourseIds = enrollments.stream()
                    .flatMap(enrollment -> enrollment.getCourseIds().stream())
                    .collect(Collectors.toSet());

            List<CourseEntity> availableCourses = courseService.findCoursesByDepartment(user.getDepartment()).stream()
                    .filter(course -> !enrolledCourseIds.contains(course.getId()))
                    .collect(Collectors.toList());

            List<CourseEntity> selectedCourses = enrollments.stream()
                    .flatMap(enrollment -> enrollment.getCourseIds().stream())
                    .map(courseService::findCourseById)
                    .collect(Collectors.toList());

            model.addAttribute("courses", availableCourses);
            model.addAttribute("selectedCourses", selectedCourses);
            model.addAttribute("selectedCoursesEntity", new EnrollmentEntity());
        }

        return "studentCourseSelection";
    }

    @PostMapping("/select")
    public String saveCourseSelection(@ModelAttribute("selectedCoursesEntity") EnrollmentEntity selectedCourses, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity student = userService.findByMail(userEmail);

        if (student != null) {
            List<EnrollmentEntity> currentEnrollments = enrollmentService.findEnrollmentsByUserNumber(student.getNumber());
            Set<String> enrolledCourseIds = currentEnrollments.stream()
                    .flatMap(enrollment -> enrollment.getCourseIds().stream())
                    .collect(Collectors.toSet());

            List<String> newCourseIds = selectedCourses.getCourseIds().stream()
                    .filter(courseId -> !enrolledCourseIds.contains(courseId))
                    .collect(Collectors.toList());

            int currentCredits = currentEnrollments.stream()
                    .flatMap(enrollment -> enrollment.getCourseIds().stream())
                    .map(courseService::findCourseById)
                    .filter(course -> course != null)
                    .mapToInt(CourseEntity::getCredits)
                    .sum();

            int newCredits = newCourseIds.stream()
                    .map(courseService::findCourseById)
                    .filter(course -> course != null)
                    .mapToInt(CourseEntity::getCredits)
                    .sum();

            if (currentCredits + newCredits <= 45) {
                if (!newCourseIds.isEmpty()) {
                    EnrollmentEntity existingEnrollment = currentEnrollments.isEmpty() ? new EnrollmentEntity() : currentEnrollments.get(0);
                    existingEnrollment.setUserNumber(student.getNumber());

                    Set<String> allCourseIds = new HashSet<>(existingEnrollment.getCourseIds() == null ? new ArrayList<>() : existingEnrollment.getCourseIds());
                    allCourseIds.addAll(newCourseIds);
                    existingEnrollment.setCourseIds(new ArrayList<>(allCourseIds));

                    enrollmentService.saveEnrollments(existingEnrollment);
                }
                return "redirect:/student/course-selection/select";
            } else {
                model.addAttribute("error", "Toplam kredi 45'i geçemez.");
                model.addAttribute("courses", courseService.findCoursesByDepartment(student.getDepartment()).stream()
                        .filter(course -> !enrolledCourseIds.contains(course.getId()))
                        .collect(Collectors.toList()));
                return "studentCourseSelection";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/remove")
    public String removeCourse(@RequestParam("courseId") String courseId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity student = userService.findByMail(userEmail);

        if (student != null) {
            List<EnrollmentEntity> enrollments = enrollmentService.findEnrollmentsByUserNumber(student.getNumber());
            if (!enrollments.isEmpty()) {
                EnrollmentEntity existingEnrollment = enrollments.get(0);
                existingEnrollment.getCourseIds().remove(courseId);
                enrollmentService.saveEnrollments(existingEnrollment);
            }
        }

        return "redirect:/student/course-selection/select";
    }
}