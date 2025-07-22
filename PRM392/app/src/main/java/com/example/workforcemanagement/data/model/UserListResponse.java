package com.example.workforcemanagement.data.model;

import java.util.List;

public class UserListResponse {
    private boolean success;
    private String message;
    private List<User> data;

    public UserListResponse() {
    }

    public UserListResponse(boolean success, String message, List<User> data) {
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

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}