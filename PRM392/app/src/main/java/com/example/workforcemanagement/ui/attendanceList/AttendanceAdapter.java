package com.example.workforcemanagement.ui.attendanceList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Attendance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    private List<Attendance> attendances;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Attendance attendance);
    }

    public AttendanceAdapter(List<Attendance> attendances, OnItemClickListener listener) {
        this.attendances = attendances != null ? attendances : new ArrayList<>();
        this.listener = listener;
    }

    public void updateAttendances(List<Attendance> newAttendances) {
        this.attendances = newAttendances != null ? newAttendances : new ArrayList<>();
        notifyDataSetChanged(); // Đảm bảo gọi phương thức này
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_hr, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendances.get(position);
        // Định dạng date (UTC to local time +07:00)
        String originalDate = attendance.getDate();
        String formattedDate = originalDate;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            formattedDate = outputFormat.format(inputFormat.parse(originalDate));
        } catch (Exception e) {
            // Giữ nguyên ngày gốc nếu parsing thất bại
        }
        holder.tvDate.setText(formattedDate);

        // Gán employee_name
        holder.tvEmployee.setText(attendance.getEmployeeName() != null ? attendance.getEmployeeName() : "-");

        // Định dạng check_in_time (UTC to local time +07:00)
        String checkInTime = attendance.getCheckInTime();
        String formattedCheckIn = checkInTime;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            formattedCheckIn = checkInTime != null ? outputFormat.format(inputFormat.parse(checkInTime)) : "-";
        } catch (Exception e) {
            formattedCheckIn = checkInTime != null ? checkInTime : "-";
        }
        holder.tvCheckin.setText(formattedCheckIn);

        // Định dạng check_out_time (UTC to local time +07:00)
        String checkOutTime = attendance.getCheckOutTime();
        String formattedCheckOut = checkOutTime;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            formattedCheckOut = checkOutTime != null ? outputFormat.format(inputFormat.parse(checkOutTime)) : "-";
        } catch (Exception e) {
            formattedCheckOut = checkOutTime != null ? checkOutTime : "-";
        }
        holder.tvCheckout.setText(formattedCheckOut);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(attendance));
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvEmployee, tvCheckin, tvCheckout;

        AttendanceViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvEmployee = itemView.findViewById(R.id.tvEmployee);
            tvCheckin = itemView.findViewById(R.id.tvCheckin);
            tvCheckout = itemView.findViewById(R.id.tvCheckout);
        }
    }
}