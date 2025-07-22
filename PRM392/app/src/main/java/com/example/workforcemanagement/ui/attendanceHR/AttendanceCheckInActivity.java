package com.example.workforcemanagement.ui.attendanceHR;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Employee;

public class AttendanceCheckInActivity extends AppCompatActivity implements EmployeeAdapter.OnEmployeeActionListener {

    private AttendanceViewModel viewModel;
    private EmployeeAdapter adapter;

    private RecyclerView rvEmployees;
    private EditText etSearch;
    private LinearLayout layoutEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance_checkin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);

        rvEmployees = findViewById(R.id.rvEmployees);
        etSearch = findViewById(R.id.etSearchEmployee);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);

        adapter = new EmployeeAdapter(this, this);
        rvEmployees.setLayoutManager(new LinearLayoutManager(this));
        rvEmployees.setAdapter(adapter);

        viewModel.getEmployeesLiveData().observe(this, employees -> {
            if (employees != null) {
                adapter.setEmployees(employees);
                rvEmployees.setVisibility(employees.isEmpty() ? View.GONE : View.VISIBLE);
                layoutEmptyState.setVisibility(employees.isEmpty() ? View.VISIBLE : View.GONE);
                if (employees.isEmpty()) {
                    //Toast.makeText(this, "Danh sách nhân viên rỗng", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Không thể tải danh sách nhân viên", Toast.LENGTH_SHORT).show();
            }
        });

        loadEmployees();

        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
                loadEmployees();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadEmployees() {
        viewModel.loadEmployees();
    }

    @Override
    public void onCheckIn(Employee employee) {
        Log.d("AttendanceCheckIn", "CheckIn Employee ID: " + employee.getId());
        viewModel.checkIn(employee.getId()).observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Check-in thành công cho ID " + employee.getId(), Toast.LENGTH_SHORT).show();
                loadEmployees();
            } else {
                Toast.makeText(this, "Check-in thất bại cho ID " + employee.getId(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCheckOut(Employee employee) {
        Log.d("AttendanceCheckIn", "CheckOut Employee ID: " + employee.getId());
        viewModel.checkOut(employee.getId()).observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Check-out thành công cho ID " + employee.getId(), Toast.LENGTH_SHORT).show();
                loadEmployees();
            } else {
                Toast.makeText(this, "Check-out thất bại cho ID " + employee.getId(), Toast.LENGTH_LONG).show();
            }
        });
    }


}