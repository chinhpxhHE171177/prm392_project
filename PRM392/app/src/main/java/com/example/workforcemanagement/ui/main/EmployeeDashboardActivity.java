package com.example.workforcemanagement.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.AttendanceRequest;
import com.example.workforcemanagement.data.model.Employee;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.data.repository.AttendanceRepository;
import com.example.workforcemanagement.ui.profile.UserProfileActivity;
import com.example.workforcemanagement.util.PreferenceManager;
import com.example.workforcemanagement.util.TokenManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EmployeeDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        User user = (User) getIntent().getSerializableExtra("user");

        // Hiển thị tên nhân viên lên TextView
        TextView tvEmployeeName = findViewById(R.id.tvEmployeeName);
        if (user != null && user.getFullName() != null && !user.getFullName().isEmpty()) {
            tvEmployeeName.setText("Chào " + user.getFullName());
        } else if (user != null && user.getUsername() != null) {
            tvEmployeeName.setText("Chào " + user.getUsername());
        } else {
            tvEmployeeName.setText("Chào nhân viên");
        }

        ImageView ivEmployeeAvatar = findViewById(R.id.ivEmployeeAvatar);
        ivEmployeeAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        androidx.cardview.widget.CardView cardMyTasks = findViewById(R.id.cardMyTasksToday);
        if (cardMyTasks != null) {
            cardMyTasks.setOnClickListener(v -> {
                Intent intent = new Intent(this, com.example.workforcemanagement.ui.task.MyTasksTodayActivity.class);
                startActivity(intent);
            });
        }

        // Add click listener for Schedule card
        androidx.cardview.widget.CardView cardSchedule = findViewById(R.id.cardSchedule);
        if (cardSchedule != null) {
            cardSchedule.setOnClickListener(v -> {
                Intent intent = new Intent(this, com.example.workforcemanagement.ui.schedule.ScheduleActivity.class);
                startActivity(intent);
            });
        }

        androidx.cardview.widget.CardView cardAttendance = findViewById(R.id.cardAttendance);
        if (cardAttendance != null) {
            cardAttendance.setOnClickListener(v -> {
                startActivity(new android.content.Intent(this, com.example.workforcemanagement.ui.attendance.AttendanceTabsActivity.class));
            });
        }
    }
}