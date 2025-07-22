//package com.example.workforcemanagement.ui.attendance;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.workforcemanagement.R;
//import com.example.workforcemanagement.data.model.Attendance;
//import com.example.workforcemanagement.data.model.AttendanceResponse;
//
//import java.util.List;
//
//public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
//    private List<Attendance> attendanceList;
//
//    public AttendanceAdapter(List<Attendance> attendanceList) {
//        this.attendanceList = attendanceList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Attendance attendance = attendanceList.get(position);
//        //holder.tvEmployeeName.setText(attendance.getEmployeeName());
//        // Thêm các binding khác
//    }
//
//    @Override
//    public int getItemCount() {
//        return attendanceList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tvEmployeeName; // Thêm các view khác trong item_attendance.xml
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName); // Thêm ID tương ứng
//        }
//    }
//}