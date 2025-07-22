package com.example.workforcemanagement.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Employee;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.ui.attendanceHR.EmployeeActivity;
import com.example.workforcemanagement.ui.profile.UserProfileActivity;

public class HRDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_dashboard);

        User user = (User) getIntent().getSerializableExtra("user");

        ImageView ivHRAvatar = findViewById(R.id.ivHRAvatar);
        ivHRAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        findViewById(R.id.cardAttendance).setOnClickListener(v -> {
            Intent intent = new Intent(HRDashboardActivity.this, com.example.workforcemanagement.ui.attendanceHR.AttendanceCheckInActivity.class);
            intent.putExtra("user", user); // nếu bạn cần user bên kia
            startActivity(intent);
        });
        findViewById(R.id.cardEmployees).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HRDashboardActivity.this, com.example.workforcemanagement.ui.attendanceHR.EmployeeActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        findViewById(R.id.cardSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HRDashboardActivity.this, com.example.workforcemanagement.ui.scheduleHR.ScheduleActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        findViewById(R.id.cardEvaluation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HRDashboardActivity.this, com.example.workforcemanagement.ui.attendanceList.AttendanceActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
}