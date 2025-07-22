package com.example.workforcemanagement.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workforcemanagement.data.api.ApiClient;
import com.example.workforcemanagement.data.api.ApiService;
import com.example.workforcemanagement.data.model.AttendanceListResponse;
import com.example.workforcemanagement.util.TokenManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceRepositoryHR {
    private static final String TAG = "AttendanceRepository";
    private final ApiService apiService;
    private final TokenManager tokenManager;

    public AttendanceRepositoryHR(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.apiService = ApiClient.getApiService();
    }

    public LiveData<AttendanceListResponse> getAttendance(String token, String employeeId, String date) {
        MutableLiveData<AttendanceListResponse> liveData = new MutableLiveData<>();
        String authToken = prepareAuthToken(token);

        if (authToken == null) {
            Log.e(TAG, "No valid token available");
            liveData.setValue(new AttendanceListResponse("No valid token", "error", null));
            return liveData;
        }

        Call<AttendanceListResponse> call = apiService.getAttendances(
                authToken,
                employeeId != null ? Integer.parseInt(employeeId) : 0,
                date
        );

        call.enqueue(new Callback<AttendanceListResponse>() {
            @Override
            public void onResponse(Call<AttendanceListResponse> call, Response<AttendanceListResponse> response) {
                if (response.isSuccessful()) {
                    AttendanceListResponse body = response.body();
                    if (body != null && body.getRecords() != null) {
                        liveData.setValue(body);
                        Log.d(TAG, "Attendance records loaded: " + body.getRecords().size());
                    } else {
                        liveData.setValue(new AttendanceListResponse("No records found", "success", new ArrayList<>()));
                        Log.w(TAG, "No attendance records in response");
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        liveData.setValue(new AttendanceListResponse(errorMessage, "error", new ArrayList<>()));
                        Log.e(TAG, "API error: " + errorMessage);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error body: " + e.getMessage());
                        liveData.setValue(new AttendanceListResponse("Error parsing response", "error", new ArrayList<>()));
                    }
                }
            }

            @Override
            public void onFailure(Call<AttendanceListResponse> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                liveData.setValue(new AttendanceListResponse("Network error: " + t.getMessage(), "error", new ArrayList<>()));
            }
        });

        return liveData;
    }

    private String prepareAuthToken(String token) {
        if (token != null && !token.isEmpty()) {
            return token.startsWith("Bearer ") ? token : "Bearer " + token;
        }
        String fallbackToken = tokenManager.getToken();
        return fallbackToken != null && !fallbackToken.isEmpty() ? "Bearer " + fallbackToken : null;
    }
}