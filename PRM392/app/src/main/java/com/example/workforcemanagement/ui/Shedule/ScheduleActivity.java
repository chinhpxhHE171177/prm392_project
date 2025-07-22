package com.example.workforcemanagement.ui.Shedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Schedule;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.ui.main.HRDashboardActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity implements ScheduleAdapter.OnItemClickListener {
    private static final String TAG = "ScheduleActivity";
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private ScheduleViewModel viewModel;
    private User user; // Nhận User từ Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerView = findViewById(R.id.recyclerView);
        Button btnAdd = findViewById(R.id.btnAdd);
        ImageView ivBack = findViewById(R.id.ivBack);

        // Nhận User từ Intent
        user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            Log.w(TAG, "User object is null from Intent");
        } else {
            Log.d(TAG, "User received: " + user.getUsername());
        }

        // Initialize ViewModel with User
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                //noinspection unchecked
                return (T) new ScheduleViewModel(getApplication(), user);
            }
        }).get(ScheduleViewModel.class);

        // Khởi tạo adapter với danh sách rỗng và gắn vào RecyclerView
        adapter = new ScheduleAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Observe LiveData
        viewModel.getSchedulesLiveData().observe(this, schedules -> {
            if (schedules != null) {
                adapter.updateSchedules(schedules);
                Log.d(TAG, "Schedules updated: " + schedules.size() + " items");
            } else {
                Log.w(TAG, "Schedules is null");
            }
        });

        viewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Log.e(TAG, "Error: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Load schedules
        viewModel.loadSchedules(1, 10);

        // Set click listeners
        btnAdd.setOnClickListener(v -> showFormDialog());
        ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, HRDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void showFormDialog() {
        ScheduleFormDialog.newInstance().show(getSupportFragmentManager(), "ScheduleForm");
    }

    @Override
    public void onDeleteClick(Schedule schedule) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this schedule?")
                .setPositiveButton("Yes", (dialog, which) -> viewModel.deleteSchedule(schedule.getId()))
                .setNegativeButton("No", null)
                .show();
    }

    // Gọi từ ScheduleFormDialog để tạo lịch
    public void createSchedule(Map<String, Object> scheduleData) {
        viewModel.createSchedule(scheduleData);
    }
}