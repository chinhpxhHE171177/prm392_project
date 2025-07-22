package com.example.workforcemanagement.ui.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    public interface OnEmployeeActionListener {
        void onCheckIn(Employee employee);
        void onCheckOut(Employee employee);
    }

    private List<Employee> employees = new ArrayList<>();
    private final Context context;
    private final OnEmployeeActionListener listener;

    private int expandedPosition = -1; // để xác định item nào đang hiển thị nút Check In/Out

    public EmployeeAdapter(Context context, OnEmployeeActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee_attendance, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employees.get(position);

        holder.tvName.setText(employee.getFullName());
        holder.tvEmail.setText(employee.getEmail());

        // Hiển thị/ẩn nút Check In/Out
        boolean isExpanded = (position == expandedPosition);
        holder.btnCheckIn.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.btnCheckOut.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Xử lý khi click vào item để hiện/ẩn nút
        holder.itemView.setOnClickListener(v -> {
            if (expandedPosition == position) {
                expandedPosition = -1;
            } else {
                expandedPosition = position;
            }
            notifyDataSetChanged();
        });

        // Xử lý click vào các nút
        holder.btnCheckIn.setOnClickListener(v -> listener.onCheckIn(employee));
        holder.btnCheckOut.setOnClickListener(v -> listener.onCheckOut(employee));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail;
        ImageView imgAvatar;
        Button btnCheckIn, btnCheckOut;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserName);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
            btnCheckOut = itemView.findViewById(R.id.btnCheckOut);
        }
    }
}
