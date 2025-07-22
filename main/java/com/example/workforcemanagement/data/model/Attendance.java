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
    @SerializedName("check_in_time")
    private String checkInTime;
    @SerializedName("check_out_time")
    private String checkOutTime;
    @SerializedName("status")
    private String status;
    @SerializedName("notes")
    private String notes;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 