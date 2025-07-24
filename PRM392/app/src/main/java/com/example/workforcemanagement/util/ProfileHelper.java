package com.example.workforcemanagement.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.workforcemanagement.data.api.ApiClient;
import com.example.workforcemanagement.data.api.ApiService;
import com.example.workforcemanagement.data.model.LoginResponse;
import com.example.workforcemanagement.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileHelper {
    private static final String TAG = "ProfileHelper";

    public interface ProfileCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public static void loadUserProfile(Context context, ProfileCallback callback) {
        TokenManager tokenManager = new TokenManager(context);
        String token = tokenManager.getToken();

        if (token == null) {
            callback.onError("No authentication token found");
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // FIXED: Use Callback<LoginResponse> to match the API service definition
        apiService.getProfile("Bearer " + token).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    User user = loginResponse.getData(); // Extract User from LoginResponse

                    if (user != null) {
                        // Debug log để xem dữ liệu từ API
                        Log.d(TAG, "API Response - User ID: " + user.getId());
                        Log.d(TAG, "API Response - Role: " + user.getRole());
                        Log.d(TAG, "API Response - Profile Type: " + user.getProfileType());
                        Log.d(TAG, "API Response - Position: " + user.getPosition());
                        Log.d(TAG, "API Response - Phone: " + user.getPhone());
                        Log.d(TAG, "API Response - Employee Code: " + user.getEmployeeCode());
                        Log.d(TAG, "API Response - Full Name: " + user.getFullName());
                        Log.d(TAG, "API Response - First Name: " + user.getFirstName());
                        Log.d(TAG, "API Response - Last Name: " + user.getLastName());

                        callback.onSuccess(user);
                    } else {
                        callback.onError("User data is null in the response");
                    }
                } else {
                    Log.e(TAG, "API Error: " + response.code() + " - " + response.message());
                    callback.onError("Failed to load profile: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Network Error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}