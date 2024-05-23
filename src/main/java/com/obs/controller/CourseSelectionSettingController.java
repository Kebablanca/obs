package com.obs.controller;

import com.obs.entities.CourseEntity;
import com.obs.entities.CourseSelectionSettingEntity;
import com.obs.services.CourseService;
import com.obs.services.CourseSelectionSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/course-selection")
public class CourseSelectionSettingController {

    private final CourseSelectionSettingService courseSelectionSettingService;
    private final CourseService courseService;

    @Autowired
    public CourseSelectionSettingController(CourseSelectionSettingService courseSelectionSettingService, CourseService courseService) {
        this.courseSelectionSettingService = courseSelectionSettingService;
        this.courseService = courseService;
    }

    @GetMapping("/settings")
    public String showSettingsForm(Model model) {
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
                if (courseSelectionSettingService.isCourseAlreadySelected(courseId)) {
                    model.addAttribute("error", "Ders zaten seçildi: " + courseService.findCourseById(courseId).getCourseName());
                    return "courseSelectionSettings";
                }
            }
            courseSelectionSetting.setCourseIds(courseIds);
        } else {
            courseSelectionSetting.setCourseIds(Collections.emptyList());
        }
        courseSelectionSettingService.saveCourseSelectionSetting(courseSelectionSetting);
        return "redirect:/admin/course-selection/settings";
    }

    @GetMapping("/courses")
    @ResponseBody
    public Map<String, Object> getCoursesByDepartment(@RequestParam String department) {
        List<CourseEntity> courses = courseService.findCoursesByDepartment(department);
        List<CourseSelectionSettingEntity> settings = courseSelectionSettingService.getSettingsByDepartment(department);
        List<String> selectedCourseIds = settings.stream()
                .flatMap(setting -> setting.getCourseIds().stream())
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("courses", courses);
        response.put("selectedCourseIds", selectedCourseIds);
        return response;
    }

}
