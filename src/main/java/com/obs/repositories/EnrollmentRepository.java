package com.obs.repositories;

import com.obs.entities.EnrollmentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends MongoRepository<EnrollmentEntity, String> {
    List<EnrollmentEntity> findByUserNumber(Long userNumber);
    List<EnrollmentEntity> findByCourseIdsContaining(String courseId);
    EnrollmentEntity findByUserNumberAndCourseIdsContaining(Long userNumber, String courseId);
}
