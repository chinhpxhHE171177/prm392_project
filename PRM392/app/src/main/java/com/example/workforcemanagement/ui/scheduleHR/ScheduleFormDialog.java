package com.example.workforcemanagement.ui.scheduleHR;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.ui.scheduleHR.ScheduleActivity;
import java.util.HashMap;
import java.util.Map;

public class ScheduleFormDialog extends DialogFragment {
    private EditText etEmployeeId, etDate, etShiftStart, etShiftEnd, etBreakDuration;
    private Button btnSave;

    public static ScheduleFormDialog newInstance() {
        return new ScheduleFormDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_schedule_form, container, false);
        etEmployeeId = view.findViewById(R.id.etEmployeeId);
        etDate = view.findViewById(R.id.etDate);
        etShiftStart = view.findViewById(R.id.etShiftStart);
        etShiftEnd = view.findViewById(R.id.etShiftEnd);
        etBreakDuration = view.findViewById(R.id.etBreakDuration);
        btnSave = view.findViewById(R.id.btnSave);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSave.setOnClickListener(v -> {
            String employeeIdStr = etEmployeeId.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String shiftStart = etShiftStart.getText().toString().trim();
            String shiftEnd = etShiftEnd.getText().toString().trim();
            String breakDurationStr = etBreakDuration.getText().toString().trim();

            if (employeeIdStr.isEmpty() || date.isEmpty() || shiftStart.isEmpty() || shiftEnd.isEmpty() || breakDurationStr.isEmpty()) {
                showError("All fields are required.");
                return;
            }

            int employeeId, breakDuration;
            try {
                employeeId = Integer.parseInt(employeeIdStr);
                breakDuration = Integer.parseInt(breakDurationStr);
            } catch (NumberFormatException e) {
                showError("Employee ID and Break Duration must be numbers.");
                return;
            }

            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                showError("Date must be in ISO format (YYYY-MM-DD, e.g., 2025-07-21).");
                return;
            }

            if (!shiftStart.matches("\\d{2}:\\d{2}:\\d{2}") || !shiftEnd.matches("\\d{2}:\\d{2}:\\d{2}")) {
                showError("Shift times must be in HH:mm:ss format (e.g., 08:00:00).");
                return;
            }

            Map<String, Object> scheduleData = new HashMap<>();
            scheduleData.put("employee_id", employeeId);
            scheduleData.put("date", date);
            scheduleData.put("shift_start", shiftStart);
            scheduleData.put("shift_end", shiftEnd);
            scheduleData.put("break_duration", breakDuration);
            scheduleData.put("status", "draft");

            Log.d("ScheduleFormDialog", "Sending schedule data: " + scheduleData.toString());

            ScheduleActivity activity = (ScheduleActivity) getActivity();
            if (activity == null) {
                Log.e("ScheduleFormDialog", "Activity is null, dialog not shown in ScheduleActivity");
                showError("Unable to process request. Please try again.");
                return;
            }
            activity.createSchedule(scheduleData);
            dismiss();
        });
    }

    private void showError(String message) {
        android.widget.Toast.makeText(getContext(), message, android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
