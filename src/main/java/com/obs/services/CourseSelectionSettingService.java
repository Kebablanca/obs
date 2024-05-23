package com.obs.services;

import com.obs.entities.CourseSelectionSettingEntity;
import com.obs.repositories.CourseSelectionSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CourseSelectionSettingEntity> getSettingsByDepartment(String department) {
        return courseSelectionSettingRepository.findByDepartment(department);
    }

    public boolean isCourseAlreadySelected(String courseId) {
        return courseSelectionSettingRepository.findByCourseIdsContains(courseId).isPresent();
    }
}
