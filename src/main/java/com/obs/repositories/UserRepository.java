package com.obs.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.obs.entities.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity,String> {
	Optional<UserEntity> findByNumber(Long number);
	Optional<UserEntity> findByMail(String mail);
	List<UserEntity> findByRole(UserEntity.UserRole role);
	void delete(UserEntity user);
}
