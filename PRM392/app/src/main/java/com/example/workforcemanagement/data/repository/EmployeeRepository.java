package com.example.workforcemanagement.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workforcemanagement.data.api.ApiClient;
import com.example.workforcemanagement.data.api.ApiService;
import com.example.workforcemanagement.data.model.AttendanceRequest;
import com.example.workforcemanagement.data.model.AttendanceResponse;
import com.example.workforcemanagement.data.model.Employee;
import com.example.workforcemanagement.data.model.EmployeeListResponse;
import com.example.workforcemanagement.data.model.EmployeesResponse;
import com.example.workforcemanagement.data.model.UserListResponse;
import com.example.workforcemanagement.data.model.UsersResponse;
import com.example.workforcemanagement.util.TokenManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository {
    private static final String TAG = "EmployeeRepository";
    private final ApiService apiService;
    private final TokenManager tokenManager;

    public EmployeeRepository(TokenManager tokenManager) {
        this.apiService = ApiClient.getClient().create(ApiService.class);
        this.tokenManager = tokenManager;
    }

    public LiveData<EmployeesResponse> getEmployees(String search, String filter, int page, int limit) {
        MutableLiveData<EmployeesResponse> employeesResult = new MutableLiveData<>();
        String token = tokenManager.getToken();

        if (token == null) {
            EmployeesResponse errorResponse = new EmployeesResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("No token available");
            employeesResult.setValue(errorResponse);
            return employeesResult;
        }

        apiService.getEmployees("Bearer " + token, search, filter, page, limit)
                .enqueue(new Callback<EmployeesResponse>() {
                    @Override
                    public void onResponse(Call<EmployeesResponse> call, Response<EmployeesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            employeesResult.setValue(response.body());
                        } else {
                            EmployeesResponse errorResponse = new EmployeesResponse();
                            errorResponse.setSuccess(false);
                            String errorMessage = "Failed to fetch employees: " + response.code();
                            if (response.errorBody() != null) {
                                try {
                                    errorMessage = response.errorBody().string();
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing error body", e);
                                }
                            }
                            errorResponse.setMessage(errorMessage);
                            employeesResult.setValue(errorResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<EmployeesResponse> call, Throwable t) {
                        EmployeesResponse errorResponse = new EmployeesResponse();
                        errorResponse.setSuccess(false);
                        errorResponse.setMessage("Network error: " + t.getMessage());
                        employeesResult.setValue(errorResponse);
                    }
                });

        return employeesResult;
    }

    public LiveData<Boolean> checkIn(int employeeId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        String token = tokenManager.getToken();

        if (token == null) {
            Log.e(TAG, "CheckIn Failed: No token available");
            result.postValue(false);
            return result;
        }

        AttendanceRequest request = new AttendanceRequest(employeeId, "checkin");
        Call<AttendanceResponse> call = apiService.checkIn("Bearer " + token, request);
        call.enqueue(new Callback<AttendanceResponse>() {
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String status = response.body().getStatus();
                        Log.d(TAG, "CheckIn Response: Status = " + status + ", Body = " + response.body().toString());
                        // Kiểm tra các giá trị thành công có thể có
                        if ("success".equalsIgnoreCase(status) || "ok".equalsIgnoreCase(status)) {
                            result.postValue(true);
                        } else {
                            Log.w(TAG, "CheckIn Failed: Unexpected status = " + status);
                            result.postValue(false);
                        }
                    } else {
                        Log.e(TAG, "CheckIn Failed: Response body is null");
                        result.postValue(false);
                    }
                } else {
                    Log.e(TAG, "CheckIn Failed: Response code " + response.code() + ", message: " + response.message());
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<AttendanceResponse> call, Throwable t) {
                Log.e(TAG, "CheckIn Failed: " + t.getMessage());
                result.postValue(false);
            }
        });

        return result;
    }

    public LiveData<Boolean> checkOut(int employeeId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        String token = tokenManager.getToken();

        if (token == null) {
            Log.e(TAG, "CheckOut Failed: No token available");
            result.postValue(false);
            return result;
        }

        AttendanceRequest request = new AttendanceRequest(employeeId, "checkout");
        Call<AttendanceResponse> call = apiService.checkOut("Bearer " + token, request);
        call.enqueue(new Callback<AttendanceResponse>() {
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue("success".equalsIgnoreCase(response.body().getStatus()));
                } else {
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<AttendanceResponse> call, Throwable t) {
                Log.e(TAG, "CheckOut Failed: " + t.getMessage());
                result.postValue(false);
            }
        });

        return result;
    }

    public LiveData<UserListResponse> getUsersWithoutEmployee() {
        MutableLiveData<UserListResponse> liveData = new MutableLiveData<>();
        String token = tokenManager.getToken();
        Log.d(TAG, "Token: " + token);
        if (token == null) {
            UserListResponse errorResponse = new UserListResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("No token available");
            liveData.setValue(errorResponse);
            return liveData;
        }
        apiService.getUsersWithoutEmployee("Bearer " + token)
                .enqueue(new Callback<UserListResponse>() {
                    @Override
                    public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                        Log.d(TAG, "Response code: " + response.code());
                        Log.d(TAG, "Response body: " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            liveData.setValue(response.body());
                        } else {
                            UserListResponse errorResponse = new UserListResponse();
                            errorResponse.setSuccess(false);
                            String errorMessage = "Failed to fetch users without employee: " + response.code();
                            if (response.errorBody() != null) {
                                try {
                                    errorMessage = response.errorBody().string();
                                    Log.e(TAG, "Error body: " + errorMessage);
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing error body", e);
                                }
                            }
                            errorResponse.setMessage(errorMessage);
                            liveData.setValue(errorResponse);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserListResponse> call, Throwable t) {
                        Log.e(TAG, "Network error: " + t.getMessage());
                        UserListResponse errorResponse = new UserListResponse();
                        errorResponse.setSuccess(false);
                        errorResponse.setMessage("Network error: " + t.getMessage());
                        liveData.setValue(errorResponse);
                    }
                });
        return liveData;
    }
    public LiveData<Boolean> createEmployee(Employee employee, int departmentId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", employee.getId());
        requestBody.put("employee_code", employee.getEmployeeCode());
        requestBody.put("first_name", employee.getFirstName());
        requestBody.put("last_name", employee.getLastName());
        requestBody.put("email", employee.getEmail());
        requestBody.put("phone", employee.getPhone());
        requestBody.put("department_id", departmentId);
        requestBody.put("position", employee.getPosition());
        requestBody.put("hire_date", employee.getHireDate());

        Log.d(TAG, "Sending employee to API: " + new Gson().toJson(requestBody));

        Call<Void> call = apiService.createEmployee("Bearer " + tokenManager.getToken(), requestBody);
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.postValue(true);
                } else {
                    try {
                        Log.e(TAG, "Create Employee Failed: Code=" + response.code() + ", Message=" + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "Create Employee Failed: Code=" + response.code() + ", Message=Unable to read error body");
                    }
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Create Employee Failed: " + t.getMessage());
                result.postValue(false);
            }
        });
        return result;
    }

    public LiveData<Boolean> updateEmployee(Employee employee ,  int departmentId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        String token = tokenManager.getToken();

        if (token == null) {
            result.postValue(false);
            return result;
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("employee_code", employee.getEmployeeCode());
        requestBody.put("first_name", employee.getFirstName());
        requestBody.put("last_name", employee.getLastName());
        requestBody.put("email", employee.getEmail());
        requestBody.put("phone", employee.getPhone());
        requestBody.put("department_id", departmentId);
        requestBody.put("position", employee.getPosition());
        requestBody.put("hire_date", employee.getHireDate());

        Call<Void> call = apiService.updateEmployee("Bearer " + token, employee.getId(), requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Update Employee Failed: " + t.getMessage());
                result.postValue(false);
            }
        });
        return result;
    }

    public LiveData<Boolean> deleteEmployee(Employee employee ,  int departmentId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        String token = tokenManager.getToken();

        if (token == null) {
            result.postValue(false);
            return result;
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("employee_code", employee.getEmployeeCode());
        requestBody.put("first_name", employee.getFirstName());
        requestBody.put("last_name", employee.getLastName());
        requestBody.put("email", employee.getEmail());
        requestBody.put("phone", employee.getPhone());
        requestBody.put("department_id", departmentId);
        requestBody.put("position", employee.getPosition());
        requestBody.put("hire_date", employee.getHireDate());
        requestBody.put("status", "terminated");
        Call<Void> call = apiService.updateEmployee("Bearer " + token, employee.getId(), requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Update Employee Failed: " + t.getMessage());
                result.postValue(false);
            }
        });
        return result;
    }
}