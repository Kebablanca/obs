package com.obs.repositories;

import com.obs.entities.CourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<CourseEntity, String> {
    List<CourseEntity> findByDepartment(String department);
    List<CourseEntity> findByInstructorNumber(Long instructorNumber); // Yeni metod
}
