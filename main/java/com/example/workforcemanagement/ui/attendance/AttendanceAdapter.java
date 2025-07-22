package com.example.workforcemanagement.ui.attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Attendance;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    private List<Attendance> attendances;

    public AttendanceAdapter(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance att = attendances.get(position);
        try {
            holder.tvDate.setText(formatDate(att.getDate()));
            holder.tvCheckIn.setText("Check-in: " + formatTime(att.getCheckInTime()));
            holder.tvCheckOut.setText("Check-out: " + (att.getCheckOutTime() != null ? formatTime(att.getCheckOutTime()) : "--"));
            holder.tvStatus.setText("Trạng thái: " + getStatusText(att.getStatus()));
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(getStatusColor(att.getStatus())));
            if (att.getNotes() != null && !att.getNotes().isEmpty()) {
                holder.tvNotes.setVisibility(View.VISIBLE);
                holder.tvNotes.setText("Ghi chú: " + att.getNotes());
            } else {
                holder.tvNotes.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            holder.tvDate.setText("--");
            holder.tvCheckIn.setText("Check-in: --");
            holder.tvCheckOut.setText("Check-out: --");
            holder.tvStatus.setText("Trạng thái: --");
            holder.tvNotes.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    private String formatDate(String isoDate) {
        if (isoDate == null) return "--";
        try {
            // Parse ISO 8601 với UTC
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDate);
            // Chuyển sang local time zone
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi"));
            outFormat.setTimeZone(java.util.TimeZone.getDefault());
            return outFormat.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }
    private String formatTime(String dateTime) {
        if (dateTime == null) return "--";
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(dateTime);
            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            outFormat.setTimeZone(java.util.TimeZone.getDefault());
            return outFormat.format(date);
        } catch (Exception e) {
            return dateTime;
        }
    }
    private int getStatusColor(String status) {
        if ("present".equals(status)) return R.color.status_completed;
        if ("late".equals(status)) return R.color.status_in_progress;
        if ("early_leave".equals(status)) return R.color.status_pending;
        if ("absent".equals(status)) return R.color.status_inactive;
        return R.color.status_pending;
    }
    private String getStatusText(String status) {
        if ("present".equals(status)) return "Đúng giờ";
        if ("late".equals(status)) return "Đến muộn";
        if ("early_leave".equals(status)) return "Về sớm";
        if ("absent".equals(status)) return "Vắng mặt";
        return status;
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvCheckIn, tvCheckOut, tvStatus, tvNotes;
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvAttendanceDate);
            tvCheckIn = itemView.findViewById(R.id.tvAttendanceCheckIn);
            tvCheckOut = itemView.findViewById(R.id.tvAttendanceCheckOut);
            tvStatus = itemView.findViewById(R.id.tvAttendanceStatus);
            tvNotes = itemView.findViewById(R.id.tvAttendanceNotes);
        }
    }
} 