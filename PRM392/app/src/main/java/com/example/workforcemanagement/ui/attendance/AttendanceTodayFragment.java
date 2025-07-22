package com.example.workforcemanagement.ui.attendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Attendance;
import com.example.workforcemanagement.data.repository.AttendanceRepository;
import com.example.workforcemanagement.util.PreferenceManager;
import com.example.workforcemanagement.util.TokenManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceTodayFragment extends Fragment {
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private TextView tvEmptyAttendance;
    private List<Attendance> attendances = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_attendance_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAttendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttendanceAdapter(attendances);
        recyclerView.setAdapter(adapter);
        tvEmptyAttendance = view.findViewById(R.id.tvEmptyAttendance);
        loadTodayAttendance();
        return view;
    }

    private void loadTodayAttendance() {
        int employeeId = new PreferenceManager(requireContext()).getEmployeeId();
        AttendanceRepository repo = new AttendanceRepository(new TokenManager(requireContext()));
        repo.getAttendance(employeeId, 1, 20).observe(getViewLifecycleOwner(), response -> {
            attendances.clear();
            if (response != null && response.getRecords() != null) {
                for (Attendance att : response.getRecords()) {
                    if (att.getDate() != null) {
                        try {
                            java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault());
                            isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                            java.util.Date date = isoFormat.parse(att.getDate());
                            java.util.Calendar cal1 = java.util.Calendar.getInstance();
                            cal1.setTime(date);
                            java.util.Calendar cal2 = java.util.Calendar.getInstance();
                            if (cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR)
                                    && cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH)
                                    && cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH)) {
                                attendances.add(att);
                            }
                        } catch (Exception e) { }
                    }
                }
            }
            adapter.notifyDataSetChanged();
            if (attendances.isEmpty()) {
                tvEmptyAttendance.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmptyAttendance.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
}