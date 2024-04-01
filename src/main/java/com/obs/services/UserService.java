package com.obs.services;

import com.obs.entities.UserEntity;
import com.obs.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    public void printUserInfoByNumber(Long number) {
        Optional<UserEntity> userOptional = userRepository.findByNumber(number);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            System.out.println("User Info:");
            System.out.println("Mail: " + user.getMail());
            System.out.println("Number: " + user.getNumber());
            System.out.println("First Name: " + user.getFirstName());
            System.out.println("Last Name: " + user.getLastName());
            System.out.println("Role: " + user.getRole());
        } else {
            System.out.println("User with number " + number + " not found.");
        }
    }
    
    public UserEntity findByNumber(Long number) {
        Optional<UserEntity> userOptional = userRepository.findByNumber(number);
        return userOptional.orElse(null);
    }
    
    
}
