package com.example.workforcemanagement.ui.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Schedule;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<Schedule> schedules;
    private OnRequestLeaveClickListener leaveClickListener;

    public interface OnRequestLeaveClickListener {
        void onRequestLeave(Schedule schedule);
    }

    public ScheduleAdapter(List<Schedule> schedules, OnRequestLeaveClickListener listener) {
        this.schedules = schedules;
        this.leaveClickListener = listener;
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
        // Định dạng ngày
        holder.tvScheduleDate.setText(formatDate(schedule.getDate()));
        // Định dạng giờ
        holder.tvScheduleTime.setText(formatTime(schedule.getShiftStart(), schedule.getShiftEnd()));
        // Hiển thị trạng thái với màu sắc và icon
        String status = schedule.getStatus();
        holder.tvScheduleStatus.setText(getStatusText(status));
        holder.tvScheduleStatus.setTextColor(getStatusColor(holder, status));
        Drawable icon = ContextCompat.getDrawable(holder.itemView.getContext(), getStatusIcon(status));
        holder.tvScheduleStatus.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        // Hiện nút đăng ký nghỉ nếu là hôm nay hoặc tuần này (tuỳ logic)
        holder.btnRequestLeave.setVisibility(View.GONE); // Có thể show theo logic
        holder.btnRequestLeave.setOnClickListener(v -> {
            if (leaveClickListener != null) leaveClickListener.onRequestLeave(schedule);
        });
    }

    private String formatDate(String isoDate) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDate);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi"));
            return outFormat.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }
    private String formatTime(String start, String end) {
        // start: "08:00:00", end: "17:00:00" => "08:00 - 17:00"
        try {
            return start.substring(0,5) + " - " + end.substring(0,5);
        } catch (Exception e) {
            return start + " - " + end;
        }
    }
    private int getStatusColor(ScheduleViewHolder holder, String status) {
        if ("confirmed".equals(status)) return ContextCompat.getColor(holder.itemView.getContext(), R.color.status_completed);
        if ("completed".equals(status)) return ContextCompat.getColor(holder.itemView.getContext(), R.color.status_in_progress);
        return ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending);
    }
    private int getStatusIcon(String status) {
        if ("confirmed".equals(status)) return R.drawable.ic_check_circle;
        if ("completed".equals(status)) return R.drawable.ic_check_circle;
        return R.drawable.ic_warning;
    }
    private String getStatusText(String status) {
        if ("confirmed".equals(status)) return "Đã xác nhận";
        if ("completed".equals(status)) return "Đã hoàn thành";
        return "Chờ xác nhận";
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvScheduleDate, tvScheduleTime, tvScheduleStatus;
        Button btnRequestLeave;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScheduleDate = itemView.findViewById(R.id.tvScheduleDate);
            tvScheduleTime = itemView.findViewById(R.id.tvScheduleTime);
            tvScheduleStatus = itemView.findViewById(R.id.tvScheduleStatus);
            btnRequestLeave = itemView.findViewById(R.id.btnRequestLeave);
        }
    }
}