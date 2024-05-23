package com.obs.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("courseSelectionSettings")
public class CourseSelectionSettingEntity {
    @Id
    private String id;
    private String department;
    private String startDate;
    private String endDate;
    private List<String> courseIds; // Adminin öğrenci seçimine sunduğu derslerin ID'leri

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }
}
