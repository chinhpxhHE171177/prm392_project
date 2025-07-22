package com.example.workforcemanagement.ui.schedule;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Schedule;
import com.example.workforcemanagement.data.repository.ScheduleRepository;
import com.example.workforcemanagement.util.PreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.viewpager2.widget.ViewPager2;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.workforcemanagement.data.model.SchedulesResponse;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ScheduleActivity extends AppCompatActivity {
    private ArrayList<Schedule> schedules = new ArrayList<>();
    private ScheduleAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvEmptySchedule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(schedules, null);
        recyclerView.setAdapter(adapter);
        tvEmptySchedule = findViewById(R.id.tvEmptySchedule);

        loadSchedules();
    }

    private void loadSchedules() {
        int employeeId = new PreferenceManager(this).getEmployeeId();
        ScheduleRepository repo = new ScheduleRepository(new com.example.workforcemanagement.util.TokenManager(this));
        repo.getSchedules(employeeId, 1, 100).observe(this, schedulesResponse -> {
            schedules.clear();
            if (schedulesResponse != null && schedulesResponse.getRecords() != null) {
                schedules.addAll(schedulesResponse.getRecords());
            }
            adapter.notifyDataSetChanged();
            if (schedules.isEmpty()) {
                tvEmptySchedule.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmptySchedule.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
}