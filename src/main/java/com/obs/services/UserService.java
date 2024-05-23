package com.obs.services;

import com.obs.entities.UserEntity;
import com.obs.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    public UserEntity findByNumber(Long number) {
        Optional<UserEntity> userOptional = userRepository.findByNumber(number);
        return userOptional.orElse(null);
    }
    
    public UserEntity findByMail(String mail) {
        Optional<UserEntity> userOptional = userRepository.findByMail(mail);
        return userOptional.orElse(null);
    }
    
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(String mail, String password, String firstName, String lastName, Long number, UserEntity.UserRole role, String department) {
    	
        if (userRepository.findByNumber(number).isPresent()) {
            throw new IllegalArgumentException("Number must be unique");
        }
    	
    	
        UserEntity user = new UserEntity();
        user.setMail(mail);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setNumber(number);
        user.setRole(role);
        user.setDepartment(department);
        userRepository.save(user);
    }

    public void deleteUserByNumber(Long number) {
        Optional<UserEntity> user = userRepository.findByNumber(number);
        user.ifPresent(userRepository::delete);
    }
    
    public List<UserEntity> findUsersByRole(UserEntity.UserRole role) {
        return userRepository.findByRole(role);
    }
}
