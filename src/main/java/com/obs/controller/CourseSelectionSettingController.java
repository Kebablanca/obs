package com.obs.controller;

import com.obs.entities.GlobalSettingsEntity;
import com.obs.entities.UserEntity;
import com.obs.entities.CourseEntity;
import com.obs.entities.CourseSelectionSettingEntity;
import com.obs.services.GlobalSettingsService;
import com.obs.services.UserService;
import com.obs.services.CourseService;
import com.obs.services.CourseSelectionSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/course-selection")
public class CourseSelectionSettingController {

    private final CourseSelectionSettingService courseSelectionSettingService;
    private final CourseService courseService;
    private final GlobalSettingsService globalSettingsService;
    private final UserService userService;

    @Autowired
    public CourseSelectionSettingController(CourseSelectionSettingService courseSelectionSettingService, CourseService courseService, GlobalSettingsService globalSettingsService, UserService userService) {
        this.courseSelectionSettingService = courseSelectionSettingService;
        this.courseService = courseService;
        this.globalSettingsService = globalSettingsService;
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String showSettingsForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity user = userService.findByMail(userEmail);
        if (user != null) {
            model.addAttribute("userName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
        }
        GlobalSettingsEntity globalSettings = globalSettingsService.getGlobalSettings();
        if (globalSettings != null) {
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = LocalDate.parse(globalSettings.getStartDate());
            LocalDate endDate = LocalDate.parse(globalSettings.getEndDate());

            model.addAttribute("startDate", globalSettings.getStartDate());
            model.addAttribute("endDate", globalSettings.getEndDate());

            if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
                model.addAttribute("error", "Güncel tarih, belirlenen tarih aralığında değil. Lütfen tarihleri kontrol ediniz.");
                model.addAttribute("courseSelectionSetting", new CourseSelectionSettingEntity());
                model.addAttribute("departments", courseService.findAllCourses().stream()
                        .map(CourseEntity::getDepartment)
                        .distinct()
                        .collect(Collectors.toList()));
                return "courseSelectionSettings";
            }
        } else {
            model.addAttribute("error", "Global tarih ayarları yapılmamış. Lütfen önce tarih ayarlarını yapınız.");
            model.addAttribute("courseSelectionSetting", new CourseSelectionSettingEntity());
            model.addAttribute("departments", courseService.findAllCourses().stream()
                    .map(CourseEntity::getDepartment)
                    .distinct()
                    .collect(Collectors.toList()));
            return "courseSelectionSettings";
        }

        List<String> departments = courseService.findAllCourses().stream()
                .map(CourseEntity::getDepartment)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("departments", departments);
        model.addAttribute("courseSelectionSetting", new CourseSelectionSettingEntity());

        return "courseSelectionSettings";
    }

    @PostMapping("/settings")
    public String saveSettings(@ModelAttribute CourseSelectionSettingEntity courseSelectionSetting, @RequestParam(required = false) List<String> courseIds, Model model) {
        if (courseIds != null) {
            for (String courseId : courseIds) {
                courseSelectionSettingService.addCourseToDepartment(courseSelectionSetting.getDepartment(), courseId);
            }
        }
        return "redirect:/admin/course-selection/settings";
    }

    @PostMapping("/settings/remove")
    public String removeCourseFromSettings(@RequestParam String courseId, @RequestParam String department, Model model) {
        courseSelectionSettingService.removeCourseFromDepartment(department, courseId);
        return "redirect:/admin/course-selection/settings";
    }

    @GetMapping("/courses")
    @ResponseBody
    public Map<String, Object> getCoursesByDepartment(@RequestParam String department) {
        List<CourseEntity> courses = courseService.findCoursesByDepartment(department);
        Optional<CourseSelectionSettingEntity> setting = courseSelectionSettingService.getSettingsByDepartment(department);
        List<String> selectedCourseIds = setting.map(CourseSelectionSettingEntity::getCourseIds).orElse(Collections.emptyList());

        // Mevcut dersleri filtrele
        List<CourseEntity> availableCourses = courses.stream()
                .filter(course -> !selectedCourseIds.contains(course.getId()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("courses", availableCourses); 
        response.put("selectedCourseIds", selectedCourseIds);
        return response;
    }

    @GetMapping("/selected-courses")
    @ResponseBody
    public List<CourseEntity> getSelectedCoursesByDepartment(@RequestParam String department) {
        Optional<CourseSelectionSettingEntity> setting = courseSelectionSettingService.getSettingsByDepartment(department);
        if (setting.isPresent()) {
            List<String> courseIds = setting.get().getCourseIds();
            return courseService.findCoursesByIds(courseIds);
        }
        return Collections.emptyList();
    }
}






