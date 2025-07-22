package com.example.workforcemanagement.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Schedule;
import java.util.ArrayList;
import java.util.List;

public class ScheduleListFragment extends Fragment {
    private static final String ARG_SCHEDULES = "schedules";
    private List<Schedule> schedules = new ArrayList<>();

    public static ScheduleListFragment newInstance(ArrayList<Schedule> schedules) {
        ScheduleListFragment fragment = new ScheduleListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SCHEDULES, schedules);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSchedules);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            schedules = (ArrayList<Schedule>) getArguments().getSerializable(ARG_SCHEDULES);
        }
        ScheduleAdapter adapter = new ScheduleAdapter(schedules, null);
        recyclerView.setAdapter(adapter);
        return view;
    }
}