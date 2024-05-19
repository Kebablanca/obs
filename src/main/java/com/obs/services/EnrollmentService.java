package com.obs.services;

import com.obs.entities.CourseEntity;
import com.obs.entities.EnrollmentEntity;
import com.obs.repositories.CourseRepository;
import com.obs.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    public List<EnrollmentEntity> findEnrollmentsByUserNumber(Long userNumber) {
        return enrollmentRepository.findByUserNumber(userNumber);
    }

    public List<CourseEntity> findCoursesByUserNumber(Long userNumber) {
        List<EnrollmentEntity> enrollments = findEnrollmentsByUserNumber(userNumber);
        List<CourseEntity> courses = enrollments.stream()
                .map(enrollment -> courseRepository.findById(enrollment.getCourseId()).orElse(null))
                .filter(course -> course != null)
                .toList();
        return courses;
    }
}
