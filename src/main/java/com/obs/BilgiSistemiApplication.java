package com.obs;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.obs.entities.UserEntity;
import com.obs.repositories.UserRepository;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { "com.obs" })

public class BilgiSistemiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BilgiSistemiApplication.class, args);
	}

	@Bean
	public CommandLineRunner updatePasswords(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			List<UserEntity> users = userRepository.findAll();
			for (UserEntity user : users) {
				if (user.getPassword().length() < 25) {
					String encodedPassword = passwordEncoder.encode(user.getPassword());
					user.setPassword(encodedPassword);
					userRepository.save(user);
				}
			}
		};
	}

}
