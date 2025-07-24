package com.example.workforcemanagement.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ManagerDashboardStats implements Serializable {
    @SerializedName("employee_count_active")
    private int employeeCount;

    @SerializedName("employee_count_total")
    private int employeeTotal;

    @SerializedName("dept_performance")
    private int deptPerformance;

    @SerializedName("tasks_in_progress")
    private int tasksInProgress;

    @SerializedName("tasks_completed_this_week")
    private int tasksCompletedThisWeek;

    // Constructor mặc định
    public ManagerDashboardStats() {
        this.employeeCount = 0;
        this.employeeTotal = 0;
        this.deptPerformance = 0;
        this.tasksInProgress = 0;
        this.tasksCompletedThisWeek = 0;
    }

    // Constructor với tham số
    public ManagerDashboardStats(int employeeCount, int employeeTotal,
                                 int deptPerformance, int tasksInProgress,
                                 int tasksCompletedThisWeek) {
        this.employeeCount = employeeCount;
        this.employeeTotal = employeeTotal;
        this.deptPerformance = deptPerformance;
        this.tasksInProgress = tasksInProgress;
        this.tasksCompletedThisWeek = tasksCompletedThisWeek;
    }

    // Getters
    public int getEmployeeCount() {
        return employeeCount >= 0 ? employeeCount : 0;
    }

    public int getEmployeeTotal() {
        return employeeTotal >= 0 ? employeeTotal : 0;
    }

    public int getDeptPerformance() {
        return Math.max(0, Math.min(100, deptPerformance)); // Giới hạn 0-100%
    }

    public int getTasksInProgress() {
        return tasksInProgress >= 0 ? tasksInProgress : 0;
    }

    public int getTasksCompletedThisWeek() {
        return tasksCompletedThisWeek >= 0 ? tasksCompletedThisWeek : 0;
    }

    // Setters
    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = Math.max(0, employeeCount);
    }

    public void setEmployeeTotal(int employeeTotal) {
        this.employeeTotal = Math.max(0, employeeTotal);
    }

    public void setDeptPerformance(int deptPerformance) {
        this.deptPerformance = Math.max(0, Math.min(100, deptPerformance));
    }

    public void setTasksInProgress(int tasksInProgress) {
        this.tasksInProgress = Math.max(0, tasksInProgress);
    }

    public void setTasksCompletedThisWeek(int tasksCompletedThisWeek) {
        this.tasksCompletedThisWeek = Math.max(0, tasksCompletedThisWeek);
    }

    @Override
    public String toString() {
        return "ManagerDashboardStats{" +
                "employeeCount=" + employeeCount +
                ", employeeTotal=" + employeeTotal +
                ", deptPerformance=" + deptPerformance +
                ", tasksInProgress=" + tasksInProgress +
                ", tasksCompletedThisWeek=" + tasksCompletedThisWeek +
                '}';
    }
}