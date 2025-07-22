package com.example.workforcemanagement.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.workforcemanagement.data.api.ApiClient;
import com.example.workforcemanagement.data.api.ApiService;
import com.example.workforcemanagement.data.model.AttendanceRequest;
import com.example.workforcemanagement.data.model.AttendanceResponse;
import com.example.workforcemanagement.util.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceRepository {
    private final ApiService apiService;
    private final TokenManager tokenManager;

    public AttendanceRepository(TokenManager tokenManager) {
        this.apiService = ApiClient.getClient().create(ApiService.class);
        this.tokenManager = tokenManager;
    }

    public LiveData<Boolean> createAttendance(AttendanceRequest request) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        String token = tokenManager.getToken();
        if (token == null) {
            result.setValue(false);
            return result;
        }
        apiService.createAttendance("Bearer " + token, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.setValue(response.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(false);
            }
        });
        return result;
    }

    public LiveData<AttendanceResponse> getAttendance(int employeeId, int page, int limit) {
        MutableLiveData<AttendanceResponse> result = new MutableLiveData<>();
        String token = tokenManager.getToken();
        if (token == null) {
            result.setValue(null);
            return result;
        }
        apiService.getAttendance("Bearer " + token, employeeId, page, limit).enqueue(new Callback<AttendanceResponse>() {
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<AttendanceResponse> call, Throwable t) {
                result.setValue(null);
            }
        });
        return result;
    }
} 