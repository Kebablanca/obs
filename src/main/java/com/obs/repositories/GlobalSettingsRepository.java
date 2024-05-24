package com.obs.repositories;

import com.obs.entities.GlobalSettingsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends MongoRepository<GlobalSettingsEntity, String> {
}
