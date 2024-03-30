package com.obs.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Document("users")
public class UserEntity {
	
	@Id
	private String id;
	@Indexed
	private String mail;
	private Long number; 
	private String firstName;
	private String lastName;
	private String role;
	private String password;
}
