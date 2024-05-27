package com.obs.repositories;

import com.obs.entities.CourseSelectionSettingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSelectionSettingRepository extends MongoRepository<CourseSelectionSettingEntity, String> {
    Optional<CourseSelectionSettingEntity> findByDepartment(String department);
    Optional<CourseSelectionSettingEntity> findByCourseIdsContains(String courseId);
}

