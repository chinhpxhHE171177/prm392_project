package com.example.workforcemanagement.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Attendance implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("employee_id")
    private int employeeId;
    @SerializedName("date")
    private String date;
    @SerializedName("email")
    private String email;
    @SerializedName("employee_name")
    private String employeeName;
    @SerializedName("check_in_time")
    private String checkInTime;
    @SerializedName("check_out_time")
    private String checkOutTime;
    @SerializedName("status")
    private String status;
    @SerializedName("notes")
    private String notes;

    // Default constructor (required for Gson)
    public Attendance() {
    }

    // Parameterized constructor
    public Attendance(int id, int employeeId, String date, String email, String employeeName,
                      String checkInTime, String checkOutTime) {
        this.id = id;
        this.employeeId = employeeId;
        this.date = date;
        this.email = email;
        this.employeeName = employeeName;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}