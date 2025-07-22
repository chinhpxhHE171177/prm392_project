package com.example.workforcemanagement.ui.task;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Task;
import com.example.workforcemanagement.data.repository.TaskRepository;
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
import android.util.Log;

public class MyTasksTodayActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskTodayAdapter adapter;
    private TextView tvEmpty;
    private ArrayList<Task> todayTasks = new ArrayList<>();
    private ArrayList<Task> otherTasks = new ArrayList<>();
    private TasksPagerAdapter pagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks_today);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        loadAllTasksAndSetupTabs();
    }

    private void loadAllTasksAndSetupTabs() {
        int employeeId = new com.example.workforcemanagement.util.PreferenceManager(this).getEmployeeId();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        TaskRepository repo = new TaskRepository(new com.example.workforcemanagement.util.TokenManager(this));
        // Lấy tất cả task của user này (page=1, limit=100)
        repo.getTasks("", "", "", 1, 100).observeForever(tasksResponse -> {
            todayTasks.clear();
            otherTasks.clear();
            if (tasksResponse != null && tasksResponse.isSuccess() && tasksResponse.getData() != null) {
                for (Task task : tasksResponse.getData().getTasks()) {
                    if (task.getAssigneeId() != null && task.getAssigneeId() == employeeId) {
                        String startDate = task.getStartDate();
                        String deadline = task.getDeadline();
                        boolean isToday = false;
                        if ((startDate != null && startDate.startsWith(today)) || (deadline != null && deadline.startsWith(today))) {
                            isToday = true;
                        }
                        if (isToday) todayTasks.add(task);
                        else otherTasks.add(task);
                    }
                }
            }
            pagerAdapter = new TasksPagerAdapter(this, todayTasks, otherTasks);
            viewPager.setAdapter(pagerAdapter);
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                if (position == 0) tab.setText("Hôm nay");
                else tab.setText("Tất cả");
            }).attach();
        });
    }
} 