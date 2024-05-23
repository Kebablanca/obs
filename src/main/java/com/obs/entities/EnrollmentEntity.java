package com.obs.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("enrollments")
public class EnrollmentEntity {
    @Id
    private String id;
    private Long userNumber; // Kullanıcı numarası
    private List<String> courseIds; // Ders ID'leri listesi

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Long userNumber) {
        this.userNumber = userNumber;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }
}
