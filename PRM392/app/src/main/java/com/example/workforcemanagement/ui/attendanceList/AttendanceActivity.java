package com.example.workforcemanagement.ui.attendanceList;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Attendance;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.util.TokenManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    private EditText etEmployeeId, etDate;
    private Button btnSearch;
    private RecyclerView rvAttendance;
    private ProgressBar progressBar;
    private LinearLayout layoutEmptyState;

    private AttendanceAdapter adapter;
    private AttendanceManagementViewModel viewModel;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        tokenManager = new TokenManager(this);
        initViews();
        setupRecyclerView();
        setupViewModel();
        setupListeners();

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void initViews() {
        etEmployeeId = findViewById(R.id.etEmployeeId);
        etDate = findViewById(R.id.etDate);
        btnSearch = findViewById(R.id.btnSearch);
        rvAttendance = findViewById(R.id.rvAttendance);
        progressBar = findViewById(R.id.progressBar);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
    }

    private void setupRecyclerView() {
        adapter = new AttendanceAdapter(new ArrayList<>(), attendance -> {
            // Xử lý sự kiện click vào item nếu cần
        });
        rvAttendance.setLayoutManager(new LinearLayoutManager(this));
        rvAttendance.setAdapter(adapter);
    }

    private void setupViewModel() {
        User user = (User) getIntent().getSerializableExtra("user");
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
                return modelClass.cast(new AttendanceManagementViewModel(getApplication(), user));
            }
        }).get(AttendanceManagementViewModel.class);

        viewModel.getAttendanceLiveData().observe(this, attendances -> {
            progressBar.setVisibility(View.GONE);
            if (attendances != null && !attendances.isEmpty()) {
                adapter.updateAttendances(attendances);
                rvAttendance.setVisibility(View.VISIBLE);
                layoutEmptyState.setVisibility(View.GONE);
            } else {
                rvAttendance.setVisibility(View.GONE);
                layoutEmptyState.setVisibility(View.VISIBLE);
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                progressBar.setVisibility(View.GONE);
                rvAttendance.setVisibility(View.GONE);
                layoutEmptyState.setVisibility(View.VISIBLE);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        // Thêm DatePicker khi nhấn vào etDate
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AttendanceActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Định dạng ngày thành yyyy-MM-dd
                        String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        etDate.setText(formattedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        btnSearch.setOnClickListener(v -> {
            String employeeIdStr = etEmployeeId.getText().toString().trim();
            String dateStr = etDate.getText().toString().trim();

            if (employeeIdStr.isEmpty() && dateStr.isEmpty()) {
                Toast.makeText(this, "Please enter Employee ID or Date", Toast.LENGTH_SHORT).show();
                return;
            }

            int employeeId = 0;
            if (!employeeIdStr.isEmpty()) {
                try {
                    employeeId = Integer.parseInt(employeeIdStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid Employee ID", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String formattedDate = "";
            if (!dateStr.isEmpty()) {
                try {
                    // Chuyển định dạng ngày từ yyyy/MM/dd, dd/MM/yyyy, hoặc yyyy-MM-dd
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    if (dateStr.matches("\\d{4}/\\d{2}/\\d{2}")) {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                        formattedDate = outputFormat.format(inputFormat.parse(dateStr));
                    } else if (dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        formattedDate = outputFormat.format(inputFormat.parse(dateStr));
                    } else if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        // Đã đúng định dạng yyyy-MM-dd
                        formattedDate = dateStr;
                    } else {
                        Toast.makeText(this, "Invalid date format. Use yyyy/MM/dd, dd/MM/yyyy, or yyyy-MM-dd", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Kiểm tra năm hợp lệ
                    int year = Integer.parseInt(formattedDate.substring(0, 4));
                    if (year < 1000 || year > 9999) {
                        Toast.makeText(this, "Invalid year in date. Year must be between 1000 and 9999", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    Toast.makeText(this, "Invalid date format. Use yyyy/MM/dd, dd/MM/yyyy, or yyyy-MM-dd", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            progressBar.setVisibility(View.VISIBLE);
            rvAttendance.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.GONE);

            Map<String, Object> searchParams = new HashMap<>();
            if (employeeId > 0) {
                searchParams.put("employeeId", employeeId);
            }
            if (!formattedDate.isEmpty()) {
                searchParams.put("date", formattedDate);
            }

            viewModel.searchAttendance(searchParams);
        });
    }
}