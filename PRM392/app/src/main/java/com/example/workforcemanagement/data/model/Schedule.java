package com.example.workforcemanagement.data.model;

public class Schedule {
    private int id;
    private int employee_id;
    private String date;
    private String shift_start;
    private String shift_end;
    private int break_duration;
    private String status;
    private int created_by;
    private String employee_name;
    private String department_name;
    private String creator_name;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEmployee_id() { return employee_id; }
    public void setEmployee_id(int employee_id) { this.employee_id = employee_id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getShift_start() { return shift_start; }
    public void setShift_start(String shift_start) { this.shift_start = shift_start; }
    public String getShift_end() { return shift_end; }
    public void setShift_end(String shift_end) { this.shift_end = shift_end; }
    public int getBreak_duration() { return break_duration; }
    public void setBreak_duration(int break_duration) { this.break_duration = break_duration; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getCreated_by() { return created_by; }
    public void setCreated_by(int created_by) { this.created_by = created_by; }
    public String getEmployee_name() { return employee_name; }
    public void setEmployee_name(String employee_name) { this.employee_name = employee_name; }
    public String getDepartment_name() { return department_name; }
    public void setDepartment_name(String department_name) { this.department_name = department_name; }
    public String getCreator_name() { return creator_name; }
    public void setCreator_name(String creator_name) { this.creator_name = creator_name; }
}