package com.example.workforcemanagement.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Schedule implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("employee_id")
    private int employeeId;
    @SerializedName("date")
    private String date;
    @SerializedName("shift_start")
    private String shiftStart;
    @SerializedName("shift_end")
    private String shiftEnd;
    @SerializedName("break_duration")
    private int breakDuration;
    @SerializedName("status")
    private String status;
    @SerializedName("created_by")
    private Integer createdBy;
    // ... getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getShiftStart() { return shiftStart; }
    public void setShiftStart(String shiftStart) { this.shiftStart = shiftStart; }
    public String getShiftEnd() { return shiftEnd; }
    public void setShiftEnd(String shiftEnd) { this.shiftEnd = shiftEnd; }
    public int getBreakDuration() { return breakDuration; }
    public void setBreakDuration(int breakDuration) { this.breakDuration = breakDuration; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }
} 