package com.obs.services;

import com.obs.entities.CourseEntity;
import com.obs.entities.EnrollmentEntity;
import com.obs.repositories.CourseRepository;
import com.obs.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<EnrollmentEntity> findEnrollmentsByCourseId(String courseId) {
        return enrollmentRepository.findByCourseIdsContaining(courseId);
    }

    public EnrollmentEntity findByUserNumberAndCourseId(Long userNumber, String courseId) {
        return enrollmentRepository.findByUserNumberAndCourseIdsContaining(userNumber, courseId);
    }

    public void saveEnrollment(EnrollmentEntity enrollment) {
        enrollmentRepository.save(enrollment);
    }

    public List<CourseEntity> findCoursesByUserNumber(Long userNumber) {
        List<EnrollmentEntity> enrollments = findEnrollmentsByUserNumber(userNumber);
        return enrollments.stream()
                .flatMap(enrollment -> enrollment.getCourseIds().stream())
                .map(courseRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void saveEnrollments(EnrollmentEntity enrollment) {
        enrollmentRepository.save(enrollment);
    }
}
