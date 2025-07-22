package com.example.workforcemanagement.data.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceRequest {
    @SerializedName("employee_id")
    private int employeeId;
    @SerializedName("type")
    private String type;

    public AttendanceRequest(int employeeId, String type) {
        this.employeeId = employeeId;
        this.type = type;
    }
}