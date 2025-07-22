package com.example.workforcemanagement.data.model;

import java.util.List;

public class EmployeeListResponse {
    private boolean success;
    private String message;
    private List<Employee> data;

    // Constructor mặc định (nếu cần cho Gson hoặc deserialization)
    public EmployeeListResponse() {
    }

    // Constructor với tham số
    public EmployeeListResponse(boolean success, String message, List<Employee> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }
}