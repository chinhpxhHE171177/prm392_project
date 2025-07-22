package com.example.workforcemanagement.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AttendanceResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("records")
    private List<Attendance> records;
    @SerializedName("total")
    private int total;
    @SerializedName("page")
    private int page;
    @SerializedName("limit")
    private int limit;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<Attendance> getRecords() { return records; }
    public void setRecords(List<Attendance> records) { this.records = records; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
} 