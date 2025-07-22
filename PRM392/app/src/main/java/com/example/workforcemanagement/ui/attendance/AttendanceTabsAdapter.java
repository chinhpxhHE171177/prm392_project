package com.example.workforcemanagement.ui.attendance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AttendanceTabsAdapter extends FragmentStateAdapter {
    public AttendanceTabsAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new AttendanceTodayFragment();
        else return new AttendanceHistoryFragment();
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}