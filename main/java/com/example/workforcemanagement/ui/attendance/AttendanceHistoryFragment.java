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
import java.util.ArrayList;
import java.util.List;

public class AttendanceHistoryFragment extends Fragment {
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
        loadAttendanceHistory();
        return view;
    }

    private void loadAttendanceHistory() {
        int employeeId = new PreferenceManager(requireContext()).getEmployeeId();
        AttendanceRepository repo = new AttendanceRepository(new TokenManager(requireContext()));
        repo.getAttendance(employeeId, 1, 100).observe(getViewLifecycleOwner(), response -> {
            attendances.clear();
            if (response != null && response.getRecords() != null) {
                attendances.addAll(response.getRecords());
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