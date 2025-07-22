package com.example.workforcemanagement.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workforcemanagement.data.api.ApiClient;
import com.example.workforcemanagement.data.model.Schedule;
import com.example.workforcemanagement.data.model.ScheduleResponse;
import com.example.workforcemanagement.util.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRepositoryHR {
    private final TokenManager tokenManager;

    public ScheduleRepositoryHR(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public LiveData<List<Schedule>> getSchedules(int page, int limit) {
        MutableLiveData<List<Schedule>> schedules = new MutableLiveData<>();
        String token = tokenManager.getToken();
        if (token == null) {
            schedules.setValue(new ArrayList<>());
            return schedules;
        }

        ApiClient.getApiService().getSchedules("Bearer " + token, page, limit).enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getRecords() != null) {
                        schedules.setValue(response.body().getRecords());
                    } else {
                        schedules.setValue(new ArrayList<>());
                    }
                } else {
                    schedules.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                schedules.setValue(new ArrayList<>());
            }
        });
        return schedules;
    }

    public LiveData<Void> createSchedule(Map<String, Object> scheduleData) {
        MutableLiveData<Void> result = new MutableLiveData<>();
        String token = tokenManager.getToken();
        if (token == null) {
            return result;
        }

        ApiClient.getApiService().createSchedule("Bearer " + token, scheduleData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue(null);
                } else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(null);
            }
        });
        return result;
    }

    public LiveData<Void> deleteSchedule(int id) {
        MutableLiveData<Void> result = new MutableLiveData<>();
        String token = tokenManager.getToken();
        if (token == null) {
            return result;
        }

        ApiClient.getApiService().deleteSchedule("Bearer " + token, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue(null);
                } else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(null);
            }
        });
        return result;
    }
}