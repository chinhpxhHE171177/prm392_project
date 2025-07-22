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

    private String employee_name;
    private String department_name;
    private String creator_name;

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
    public String getEmployee_name() { return employee_name; }
    public void setEmployee_name(String employee_name) { this.employee_name = employee_name; }
    public String getDepartment_name() { return department_name; }
    public void setDepartment_name(String department_name) { this.department_name = department_name; }
    public String getCreator_name() { return creator_name; }
    public void setCreator_name(String creator_name) { this.creator_name = creator_name; }
}