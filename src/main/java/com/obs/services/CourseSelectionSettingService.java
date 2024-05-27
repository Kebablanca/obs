package com.obs.services;

import com.obs.entities.CourseSelectionSettingEntity;
import com.obs.repositories.CourseSelectionSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CourseSelectionSettingService {

    private final CourseSelectionSettingRepository courseSelectionSettingRepository;

    @Autowired
    public CourseSelectionSettingService(CourseSelectionSettingRepository courseSelectionSettingRepository) {
        this.courseSelectionSettingRepository = courseSelectionSettingRepository;
    }

    public void saveCourseSelectionSetting(CourseSelectionSettingEntity courseSelectionSetting) {
        courseSelectionSettingRepository.save(courseSelectionSetting);
    }

    public Optional<CourseSelectionSettingEntity> getSettingsByDepartment(String department) {
        return courseSelectionSettingRepository.findByDepartment(department);
    }

    public boolean isCourseAlreadySelected(String courseId) {
        return courseSelectionSettingRepository.findByCourseIdsContains(courseId).isPresent();
    }

    public void addCourseToDepartment(String department, String courseId) {
        Optional<CourseSelectionSettingEntity> optionalSetting = courseSelectionSettingRepository.findByDepartment(department);
        if (optionalSetting.isPresent()) {
            CourseSelectionSettingEntity setting = optionalSetting.get();
            List<String> courseIds = setting.getCourseIds();
            if (!courseIds.contains(courseId)) {
                courseIds.add(courseId);
                setting.setCourseIds(courseIds);
                courseSelectionSettingRepository.save(setting);
            }
        } else {
            CourseSelectionSettingEntity newSetting = new CourseSelectionSettingEntity();
            newSetting.setDepartment(department);
            newSetting.setCourseIds(new ArrayList<>(Collections.singletonList(courseId)));
            courseSelectionSettingRepository.save(newSetting);
        }
    }

    public void removeCourseFromDepartment(String department, String courseId) {
        Optional<CourseSelectionSettingEntity> optionalSetting = courseSelectionSettingRepository.findByDepartment(department);
        if (optionalSetting.isPresent()) {
            CourseSelectionSettingEntity setting = optionalSetting.get();
            List<String> courseIds = setting.getCourseIds();
            courseIds.remove(courseId);
            setting.setCourseIds(courseIds);
            courseSelectionSettingRepository.save(setting);
        }
    }
}
