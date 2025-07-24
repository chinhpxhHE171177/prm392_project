package com.example.workforcemanagement.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Department;
import com.example.workforcemanagement.data.model.ManagerDashboardStats;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.ui.dep_manager.ActivityConTrackingTask;
import com.example.workforcemanagement.ui.profile.UserProfileActivity;

public class ManagerDashboardActivity extends AppCompatActivity {
    private TextView tvManagerName, tvDeptName, tvDeptEmployees, tvDeptPerformance;
    private TextView tvDeptTaskInProgress, tvDeptCompletedThisWeek, tvDeptTitle;
    private ManagerDashboardStats stats;
    private User user;
    private Department department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        user = (User) getIntent().getSerializableExtra("user");
        stats = (ManagerDashboardStats) getIntent().getSerializableExtra("managerStats");
        department = (Department) getIntent().getSerializableExtra("department");

        initViews();
        setupUserInfo();
        setupDepartmentStats();
        setupClickListeners();
    }

    private void initViews() {
        tvManagerName = findViewById(R.id.tvManagerName);
        tvDeptName = findViewById(R.id.tvDeptName);
        tvDeptEmployees = findViewById(R.id.tvDeptEmployees);
        tvDeptPerformance = findViewById(R.id.tvDeptPerformance);
        tvDeptTaskInProgress = findViewById(R.id.tvDeptTaskInProgress);
        tvDeptCompletedThisWeek = findViewById(R.id.tvDeptCompletedThisWeek);
        tvDeptTitle = findViewById(R.id.tvDeptTitle);
    }

    private void setupUserInfo() {
        // Lấy tên phòng ban ưu tiên từ department, nếu không có lấy từ user
        String deptName = getDepartmentName();

        // Set tên manager
        String managerName = getManagerName();
        tvManagerName.setText("Chào " + managerName);
        tvDeptName.setText(deptName);
        tvDeptTitle.setText("📊 PHÒNG BAN: " + deptName);
    }

    private String getDepartmentName() {
        if (department != null && department.getName() != null && !department.getName().isEmpty()) {
            return department.getName();
        } else if (user != null && user.getDepartment() != null && user.getDepartment().getName() != null) {
            return user.getDepartment().getName();
        } else {
            return "[Tên Phòng Ban]";
        }
    }

    private String getManagerName() {
        if (user != null) {
            if (user.getFullName() != null && !user.getFullName().isEmpty()) {
                return user.getFullName();
            } else if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                return user.getUsername();
            }
        }
        return "[Tên Quản Lý]";
    }

    private void setupDepartmentStats() {
        Log.d("DASHBOARD_STATS", "Stats object: " + stats);

        if (stats != null) {
            Log.d("DASHBOARD_STATS", "Employee Active: " + stats.getEmployeeCount() +
                    ", Total: " + stats.getEmployeeTotal() +
                    ", Performance: " + stats.getDeptPerformance() + "%" +
                    ", Tasks in Progress: " + stats.getTasksInProgress() +
                    ", Completed this week: " + stats.getTasksCompletedThisWeek());

            // Hiển thị số nhân viên active/tổng số nhân viên
            tvDeptEmployees.setText(stats.getEmployeeCount() + "/" + stats.getEmployeeTotal());

            // Hiển thị hiệu suất phòng ban
            tvDeptPerformance.setText(stats.getDeptPerformance() + "%");

            // Hiển thị số task đang thực hiện (không có trạng thái complete hay cancel)
            tvDeptTaskInProgress.setText("Công việc đang thực hiện: " + stats.getTasksInProgress());

            // Hiển thị số task đã hoàn thành tuần này
            tvDeptCompletedThisWeek.setText("Hoàn thành tuần này: " + stats.getTasksCompletedThisWeek());

        } else {
            Log.w("DASHBOARD_STATS", "Stats is null - using default values");
            // Hiển thị giá trị mặc định khi không có dữ liệu
            tvDeptEmployees.setText("0/0");
            tvDeptPerformance.setText("0%");
            tvDeptTaskInProgress.setText("Công việc đang thực hiện: 0");
            tvDeptCompletedThisWeek.setText("Hoàn thành tuần này: 0");
        }
    }

    private void setupClickListeners() {
        // Avatar click mở Profile
        ImageView ivManagerAvatar = findViewById(R.id.ivManagerAvatar);
        ivManagerAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        // Team card click
        findViewById(R.id.cardTeam).setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.workforcemanagement.ui.dep_manager.ActivityEmListDep.class);
            if (department != null) {
                intent.putExtra("department", department);
            }
            startActivity(intent);
        });

        // Track Task card click
        findViewById(R.id.cardTrackTask).setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityConTrackingTask.class);
            startActivity(intent);
        });
    }
}