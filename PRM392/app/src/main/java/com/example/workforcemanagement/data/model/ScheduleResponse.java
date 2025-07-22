package com.example.workforcemanagement.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ScheduleResponse {
    @SerializedName("records")
    private List<Schedule> records;
    @SerializedName("total")
    private int total;
    @SerializedName("page")
    private int page;
    @SerializedName("limit")
    private int limit;

    // Getters
    public List<Schedule> getRecords() { return records; }
    public int getTotal() { return total; }
    public int getPage() { return page; }
    public int getLimit() { return limit; }

    // Setters (nếu cần)
    public void setRecords(List<Schedule> records) { this.records = records; }
    public void setTotal(int total) { this.total = total; }
    public void setPage(int page) { this.page = page; }
    public void setLimit(int limit) { this.limit = limit; }
}