package com.obs.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document("enrollments")
public class EnrollmentEntity {
    @Id
    private String id;
    private Long userNumber; // Kullanıcı numarası
    private List<String> courseIds; // Ders ID'leri listesi
    private Map<String, Integer> midtermGrades = new HashMap<>(); // Ders ID'si ve vize notu
    private Map<String, Integer> finalGrades = new HashMap<>(); // Ders ID'si ve final notu

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

    public Map<String, Integer> getMidtermGrades() {
        return midtermGrades;
    }

    public void setMidtermGrades(Map<String, Integer> midtermGrades) {
        this.midtermGrades = midtermGrades;
    }

    public Map<String, Integer> getFinalGrades() {
        return finalGrades;
    }

    public void setFinalGrades(Map<String, Integer> finalGrades) {
        this.finalGrades = finalGrades;
    }
}
