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
    private Long instructorNumber;
    private List<Long> enrolledUserNumbers; 
    private int credits;
    private String department;
    private int midtermWeight;
    private int finalWeight;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Long getInstructorNumber() {
        return instructorNumber;
    }

    public void setInstructorNumber(Long instructorNumber) {
        this.instructorNumber = instructorNumber;
    }

    public List<Long> getEnrolledUserNumbers() {
        return enrolledUserNumbers;
    }

    public void setEnrolledUserNumbers(List<Long> enrolledUserNumbers) {
        this.enrolledUserNumbers = enrolledUserNumbers;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
    public int getMidtermWeight() {
        return midtermWeight;
    }

    public void setMidtermWeight(int midtermWeight) {
        this.midtermWeight = midtermWeight;
    }

    public int getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(int finalWeight) {
        this.finalWeight = finalWeight;
    }
}
