package com.example.workforcemanagement.ui.Shedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<Schedule> schedules;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(Schedule schedule);
    }

    public ScheduleAdapter(List<Schedule> schedules, OnItemClickListener listener) {
        this.schedules = schedules != null ? schedules : new ArrayList<>();
        this.listener = listener;
    }

    public void updateSchedules(List<Schedule> newSchedules) {
        this.schedules = newSchedules != null ? newSchedules : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        // Chuyển đổi định dạng ngày từ "YYYY-MM-DD" thành "DD/MM/YYYY"
        String originalDate = schedule.getDate();
        String formattedDate = originalDate; // Mặc định giữ nguyên nếu không hợp lệ
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formattedDate = outputFormat.format(inputFormat.parse(originalDate));
        } catch (Exception e) {
            // Nếu định dạng không hợp lệ, giữ nguyên hoặc log lỗi
        }
        holder.tvDate.setText(formattedDate);
        holder.tvEmployee.setText(schedule.getEmployee_name()); // Sử dụng getEmployee_name() theo code
        holder.tvShift.setText(schedule.getShift_start() + " - " + schedule.getShift_end()); // Sử dụng getShift_start() và getShift_end()
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(schedule));
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvEmployee, tvShift;
        ImageButton btnDelete; // Thay Button bằng ImageButton để khớp với XML

        ScheduleViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvEmployee = itemView.findViewById(R.id.tvEmployee);
            tvShift = itemView.findViewById(R.id.tvShift);
            btnDelete = itemView.findViewById(R.id.btnDelete); // Cập nhật tham chiếu
        }
    }
}