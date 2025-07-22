package com.example.workforcemanagement.ui.attendanceList;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workforcemanagement.data.model.Attendance;
import com.example.workforcemanagement.data.model.AttendanceListResponse;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.data.repository.AttendanceRepositoryHR;
import com.example.workforcemanagement.util.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceManagementViewModel extends AndroidViewModel {
    private static final String TAG = "AttendanceManagementViewModel";
    private final AttendanceRepositoryHR repository;
    private final MutableLiveData<List<Attendance>> attendanceLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AttendanceManagementViewModel(@NonNull Application application, User user) {
        super(application);
        TokenManager tokenManager = new TokenManager(application.getApplicationContext());
        this.repository = new AttendanceRepositoryHR(tokenManager);
    }

    public LiveData<List<Attendance>> getAttendanceLiveData() {
        return attendanceLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void searchAttendance(Map<String, Object> searchParams) {
        String token = new TokenManager(getApplication().getApplicationContext()).getToken();
        if (token == null || token.isEmpty()) {
            errorMessage.setValue("No valid token available");
            Log.e(TAG, "No valid token available");
            return;
        }

        int employeeId = searchParams.containsKey("employeeId") ? ((Number) searchParams.get("employeeId")).intValue() : 0;
        String date = (String) searchParams.getOrDefault("date", "");

        LiveData<AttendanceListResponse> responseLiveData = repository.getAttendance(
                token,
                employeeId > 0 ? String.valueOf(employeeId) : null,
                date.isEmpty() ? null : date
        );

        responseLiveData.observeForever(response -> {
            if (response != null && response.getRecords() != null && !response.getRecords().isEmpty()) {
                attendanceLiveData.setValue(response.getRecords());
                Log.d(TAG, "Attendance records loaded: " + response.getRecords().size());
            } else {
                attendanceLiveData.setValue(new ArrayList<>());
                errorMessage.setValue(response != null ? response.getMessage() : "No attendance records found");
                Log.w(TAG, "No attendance records found");
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        attendanceLiveData.removeObservers(null);
        errorMessage.removeObservers(null);
    }
}