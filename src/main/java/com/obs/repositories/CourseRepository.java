package com.obs.repositories;


import com.obs.entities.CourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends MongoRepository<CourseEntity, String> {
}