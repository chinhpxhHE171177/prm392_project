package com.example.workforcemanagement.data.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceRequest {
    private int employee_id;
    @SerializedName("type")
    private String type;
    private String date; // yyyy-MM-dd
    private String check_in_time; // HH:mm:ss
    private String check_out_time; // HH:mm:ss (optional)
    private String notes; // optional

    public AttendanceRequest(int employeeId, String type) {
        this.employee_id = employeeId;
        this.type = type;
    }

    public AttendanceRequest(int employee_id, String date, String check_in_time, String check_out_time, String notes) {
        this.employee_id = employee_id;
        this.date = date;
        this.check_in_time = check_in_time;
        this.check_out_time = check_out_time;
        this.notes = notes;
    }
    public int getEmployee_id() { return employee_id; }
    public void setEmployee_id(int employee_id) { this.employee_id = employee_id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getCheck_in_time() { return check_in_time; }
    public void setCheck_in_time(String check_in_time) { this.check_in_time = check_in_time; }
    public String getCheck_out_time() { return check_out_time; }
    public void setCheck_out_time(String check_out_time) { this.check_out_time = check_out_time; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}