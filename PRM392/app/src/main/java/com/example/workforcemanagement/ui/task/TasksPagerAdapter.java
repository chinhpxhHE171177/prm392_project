package com.example.workforcemanagement.ui.task;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.workforcemanagement.data.model.Task;
import java.util.ArrayList;

public class TasksPagerAdapter extends FragmentStateAdapter {
    private ArrayList<Task> todayTasks, otherTasks;

    public TasksPagerAdapter(@NonNull FragmentActivity fa, ArrayList<Task> todayTasks, ArrayList<Task> otherTasks) {
        super(fa);
        this.todayTasks = todayTasks;
        this.otherTasks = otherTasks;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return TodayTasksFragment.newInstance(todayTasks);
        else return OtherTasksFragment.newInstance(otherTasks);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}