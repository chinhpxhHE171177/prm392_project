package com.example.workforcemanagement.ui.Shedule;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.workforcemanagement.data.model.Schedule;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.data.repository.ScheduleRepository;
import com.example.workforcemanagement.util.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleViewModel extends AndroidViewModel {
    private final ScheduleRepository repository;
    private final MutableLiveData<List<Schedule>> schedules = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ScheduleViewModel(@NonNull Application application, User user) {
        super(application);
        TokenManager tokenManager = new TokenManager(application.getApplicationContext());
        this.repository = new ScheduleRepository(tokenManager);
    }

    public void loadSchedules(int page, int limit) {
        repository.getSchedules(page, limit).observeForever(schedulesList -> {
            if (schedulesList != null) {
                schedules.postValue(schedulesList);
            } else {
                schedules.postValue(new ArrayList<>());
                errorMessage.postValue("Failed to load schedules.");
            }
        });
    }

    public LiveData<List<Schedule>> getSchedulesLiveData() {
        return schedules;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void createSchedule(Map<String, Object> scheduleData) {
        repository.createSchedule(scheduleData).observeForever(result -> {
            if (result == null) {
                errorMessage.postValue("oke create schedule.");
                loadSchedules(1, 10);
            } else {
                loadSchedules(1, 10); // Tải lại danh sách sau khi tạo
            }
        });
    }

    public void deleteSchedule(int id) {
        repository.deleteSchedule(id).observeForever(result -> {
            if (result == null) {
                errorMessage.postValue(" delete schedule.");
                loadSchedules(1, 10);
            } else {
                loadSchedules(1, 10); // Tải lại danh sách sau khi xóa
            }
        });
    }
}