package com.obs.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("enrollments")
public class EnrollmentEntity {
    @Id
    private String id;
    private Long userNumber; // Kullanıcı numarası
    private String courseId; // CourseEntity ID'si
	public Long getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(Long userNumber) {
		this.userNumber = userNumber;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
    
    
    
}
