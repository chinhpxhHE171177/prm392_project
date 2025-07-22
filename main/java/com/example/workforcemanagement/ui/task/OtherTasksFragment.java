package com.example.workforcemanagement.ui.task;

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
import com.example.workforcemanagement.data.model.Task;
import java.util.ArrayList;
import java.util.List;

public class OtherTasksFragment extends Fragment {
    private static final String ARG_TASKS = "tasks";
    private List<Task> tasks = new ArrayList<>();

    public static OtherTasksFragment newInstance(ArrayList<Task> tasks) {
        OtherTasksFragment fragment = new OtherTasksFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASKS, tasks);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            tasks = (ArrayList<Task>) getArguments().getSerializable(ARG_TASKS);
        }
        TaskTodayAdapter adapter = new TaskTodayAdapter(tasks);
        recyclerView.setAdapter(adapter);
        return view;
    }
} 