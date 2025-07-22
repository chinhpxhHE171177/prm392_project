package com.example.workforcemanagement.ui.attendanceHR;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workforcemanagement.data.model.Employee;
import com.example.workforcemanagement.data.model.EmployeeListResponse;
import com.example.workforcemanagement.data.model.UserListResponse;
import com.example.workforcemanagement.data.repository.EmployeeRepository;
import com.example.workforcemanagement.util.TokenManager;

import java.util.ArrayList;
import java.util.List;

public class AttendanceViewModel extends AndroidViewModel {
    private final EmployeeRepository repository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<List<Employee>> employees = new MutableLiveData<>(new ArrayList<>());

    public AttendanceViewModel(@NonNull Application application) {
        super(application);
        TokenManager tokenManager = new TokenManager(application.getApplicationContext());
        this.repository = new EmployeeRepository(tokenManager);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
        loadEmployees();
    }

    public void loadEmployees() {
        repository.getEmployees(searchQuery.getValue(), "", 1, 100).observeForever(response -> {
            if (response != null && response.isSuccess() && response.getData() != null) {
                employees.postValue(response.getData().getEmployees());
            } else {
                employees.postValue(new ArrayList<>());
            }
        });
    }

    public LiveData<List<Employee>> getEmployeesLiveData() {
        return employees;
    }

    public LiveData<Boolean> checkIn(int employeeId) {
        return repository.checkIn(employeeId);
    }

    public LiveData<Boolean> checkOut(int employeeId) {
        return repository.checkOut(employeeId);
    }

    public LiveData<UserListResponse> getUsersWithoutEmployee() {
        return repository.getUsersWithoutEmployee();
    }

    public LiveData<Boolean> createEmployee(Employee employee, int departmentId) {
        return repository.createEmployee(employee, departmentId);
    }
    public LiveData<Boolean> updateEmployee(Employee employee , int departmentId) {
        return repository.updateEmployee(employee,  departmentId);
    }

    public LiveData<Boolean> deleteEmployee(Employee employee, int departmentId) {
        // Cập nhật status thành "terminated" trước khi gửi
        employee.setStatus("terminated");
        return repository.deleteEmployee(employee, departmentId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Hủy quan sát nếu cần (nếu sử dụng observeForever)
    }
}