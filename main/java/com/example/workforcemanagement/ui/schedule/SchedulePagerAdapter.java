package com.example.workforcemanagement.ui.schedule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.workforcemanagement.data.model.Schedule;
import java.util.ArrayList;

public class SchedulePagerAdapter extends FragmentStateAdapter {
    private ArrayList<Schedule> todaySchedules, weekSchedules, allSchedules;

    public SchedulePagerAdapter(@NonNull FragmentActivity fa, ArrayList<Schedule> today, ArrayList<Schedule> week, ArrayList<Schedule> all) {
        super(fa);
        this.todaySchedules = today;
        this.weekSchedules = week;
        this.allSchedules = all;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return ScheduleListFragment.newInstance(todaySchedules);
        else if (position == 1) return ScheduleListFragment.newInstance(weekSchedules);
        else return ScheduleListFragment.newInstance(allSchedules);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
} 