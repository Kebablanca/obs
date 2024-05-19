package com.obs.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("courses")
public class CourseEntity {
    @Id
    private String id;
    private String courseName;
    private String courseCode;
    private String instructor;
    private List<Long> enrolledUserNumbers; // Kullanıcı numaralarını tutmak için
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public List<Long> getEnrolledUserNumbers() {
		return enrolledUserNumbers;
	}
	public void setEnrolledUserNumbers(List<Long> enrolledUserNumbers) {
		this.enrolledUserNumbers = enrolledUserNumbers;
	}

    // Getters and Setters
    // ...
}



