package com.example.workforcemanagement.ui.attendance;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Attendance;
import com.example.workforcemanagement.data.model.AttendanceResponse;
import com.example.workforcemanagement.data.repository.AttendanceRepository;
import com.example.workforcemanagement.util.PreferenceManager;
import com.example.workforcemanagement.util.TokenManager;
import java.util.ArrayList;

public class AttendanceHistoryActivity extends AppCompatActivity {
    private ArrayList<Attendance> attendances = new ArrayList<>();
    private AttendanceAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvEmptyAttendance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewAttendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttendanceAdapter(attendances);
        recyclerView.setAdapter(adapter);
        tvEmptyAttendance = findViewById(R.id.tvEmptyAttendance);

        loadAttendanceHistory();
    }

    private void loadAttendanceHistory() {
        int employeeId = new PreferenceManager(this).getEmployeeId();
        AttendanceRepository repo = new AttendanceRepository(new TokenManager(this));
        repo.getAttendance(employeeId, 1, 100).observe(this, response -> {
            attendances.clear();
            if (response != null && response.getRecords() != null) {
                // Log dữ liệu trả về để debug
                for (Attendance att : response.getRecords()) {
                    android.util.Log.d("ATTENDANCE_DEBUG", "date=" + att.getDate() + ", checkIn=" + att.getCheckInTime() + ", checkOut=" + att.getCheckOutTime());
                }
                attendances.addAll(response.getRecords());
            }
            adapter.notifyDataSetChanged();
            if (attendances.isEmpty()) {
                tvEmptyAttendance.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmptyAttendance.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
} 