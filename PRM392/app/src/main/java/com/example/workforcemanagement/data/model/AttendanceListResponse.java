package com.example.workforcemanagement.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceListResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("records")
    private List<Attendance> records;

    // Default constructor (required for Gson)
    public AttendanceListResponse() {
    }

    // Parameterized constructor
    public AttendanceListResponse(String message, String status, List<Attendance> records) {
        this.message = message;
        this.status = status;
        this.records = records;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Attendance> getRecords() {
        return records;
    }

    public void setRecords(List<Attendance> records) {
        this.records = records;
    }
}