package com.iiui.studentapp.backend.model;

public class StudentInfo {
    private final String email;
    private final String regNo;
    private final String name;
    private final String degreeTitle;
    private final String batch;

    public StudentInfo(String email, String regNo, String name, String degreeTitle, String batch) {
        this.email = email;
        this.regNo = regNo;
        this.name = name;
        this.degreeTitle = degreeTitle;
        this.batch = batch;
    }

    public String getEmail() {
        return email;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getName() {
        return name;
    }

    public String getDegreeTitle() {
        return degreeTitle;
    }

    public String getBatch() {
        return batch;
    }

    public String getFormattedRegNo() {
        return regNo + "/FOC/" + degreeTitle.toUpperCase() + "/" + batch;
    }
}
