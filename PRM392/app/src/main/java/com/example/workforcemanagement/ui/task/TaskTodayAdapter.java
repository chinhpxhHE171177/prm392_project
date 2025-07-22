package com.example.workforcemanagement.ui.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Task;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskTodayAdapter extends RecyclerView.Adapter<TaskTodayAdapter.TaskViewHolder> {
    private List<Task> tasks;

    public TaskTodayAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_today, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());
        holder.tvStatus.setText(task.getStatus());
        holder.tvStatus.setBackgroundTintList(holder.itemView.getContext().getResources().getColorStateList(getStatusBg(task.getStatus())));
        holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(getStatusColor(task.getStatus())));
        holder.ivPriority.setImageResource(getPriorityIcon(task.getPriority()));
        holder.progressBar.setProgress(task.getProgress());
        // Thời gian
        String time = "";
        String startTime = "--";
        String endTime = "--";
        String deadlineFull = "--";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        if (task.getStartDate() != null) {
            try {
                Date startDate = inputFormat.parse(task.getStartDate().replace(".000Z", ""));
                startTime = timeFormat.format(startDate);
            } catch (ParseException | NullPointerException e) {
                startTime = "--";
            }
        }

        if (task.getDeadline() != null) {
            try {
                Date deadlineDate = inputFormat.parse(task.getDeadline().replace(".000Z", ""));
                endTime = timeFormat.format(deadlineDate);
                deadlineFull = fullDateFormat.format(deadlineDate);
            } catch (ParseException | NullPointerException e) {
                endTime = "--";
                deadlineFull = "--";
            }
        }

        time = startTime + " - " + endTime;
        holder.tvTime.setText(time);
        holder.tvDeadline.setText("| Deadline: " + deadlineFull);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPriority;
        TextView tvTitle, tvDescription, tvStatus, tvTime, tvDeadline;
        ProgressBar progressBar;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPriority = itemView.findViewById(R.id.ivPriority);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private int getPriorityIcon(String priority) {
        switch (priority) {
            case "high": return R.drawable.ic_priority;
            case "urgent": return R.drawable.ic_warning;
            case "medium": return R.drawable.ic_assignment;
            case "low": return R.drawable.ic_check_circle;
            default: return R.drawable.ic_priority;
        }
    }
    private int getStatusBg(String status) {
        switch (status) {
            case "pending": return R.color.status_pending_bg;
            case "in_progress": return R.color.status_in_progress_bg;
            case "review": return R.color.status_review_bg;
            case "completed": return R.color.status_completed_bg;
            case "cancelled": return R.color.status_cancelled_bg;
            default: return R.color.status_pending_bg;
        }
    }
    private int getStatusColor(String status) {
        switch (status) {
            case "pending": return R.color.status_pending;
            case "in_progress": return R.color.status_in_progress;
            case "review": return R.color.status_review;
            case "completed": return R.color.status_completed;
            case "cancelled": return R.color.status_cancelled;
            default: return R.color.status_pending;
        }
    }
}