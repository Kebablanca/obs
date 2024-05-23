package com.obs.services;

import com.obs.entities.CourseEntity;
import com.obs.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void saveCourse(CourseEntity course) {
        courseRepository.save(course);
    }

    public List<CourseEntity> findAllCourses() {
        return courseRepository.findAll();
    }

    public CourseEntity findCourseById(String courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    public List<CourseEntity> findCoursesByIds(List<String> courseIds) {
        return courseRepository.findAllById(courseIds);
    }

    public List<CourseEntity> findCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }
}
