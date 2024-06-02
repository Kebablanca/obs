package com.obs.utils;

public class GradeUtils {

    public static String getLetterGrade(int score) {
        if (score >= 90) return "A";
        if (score >= 85) return "B1";
        if (score >= 80) return "B2";
        if (score >= 75) return "B3";
        if (score >= 70) return "C1";
        if (score >= 65) return "C2";
        if (score >= 60) return "C3";
        if (score >= 50) return "F1";
        return "F2";
    }

    public static double getGradePoint(String letterGrade) {
        switch (letterGrade) {
            case "A": return 4.0;
            case "B1": return 3.5;
            case "B2": return 3.25;
            case "B3": return 3.0;
            case "C1": return 2.75;
            case "C2": return 2.5;
            case "C3": return 2.0;
            case "F1": return 1.5;
            case "F2": return 0.0;
            default: return 0.0;
        }
    }
}

