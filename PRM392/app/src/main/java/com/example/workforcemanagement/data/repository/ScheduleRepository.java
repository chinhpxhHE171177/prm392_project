package com.example.workforcemanagement.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.workforcemanagement.data.api.ApiClient;
import com.example.workforcemanagement.data.api.ApiService;
import com.example.workforcemanagement.data.model.SchedulesResponse;
import com.example.workforcemanagement.util.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRepository {
    private final ApiService apiService;
    private final TokenManager tokenManager;

    public ScheduleRepository(TokenManager tokenManager) {
        this.apiService = ApiClient.getClient().create(ApiService.class);
        this.tokenManager = tokenManager;
    }

    public LiveData<SchedulesResponse> getSchedules(int employeeId, int page, int limit) {
        MutableLiveData<SchedulesResponse> result = new MutableLiveData<>();
        String token = tokenManager.getToken();
        if (token == null) {
            SchedulesResponse errorResponse = new SchedulesResponse();
            errorResponse.setMessage("No token available");
            result.setValue(errorResponse);
            return result;
        }
        apiService.getSchedules("Bearer " + token, employeeId, null, null, null, null, page, limit).enqueue(new Callback<SchedulesResponse>() {
            @Override
            public void onResponse(Call<SchedulesResponse> call, Response<SchedulesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    SchedulesResponse errorResponse = new SchedulesResponse();
                    errorResponse.setMessage("Failed to fetch schedules: " + response.code());
                    result.setValue(errorResponse);
                }
            }
            @Override
            public void onFailure(Call<SchedulesResponse> call, Throwable t) {
                SchedulesResponse errorResponse = new SchedulesResponse();
                errorResponse.setMessage("Network error: " + t.getMessage());
                result.setValue(errorResponse);
            }
        });
        return result;
    }
}