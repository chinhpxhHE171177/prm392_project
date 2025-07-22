package com.example.workforcemanagement.ui.attendanceHR;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Employee;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.util.TokenManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {
    private static final String TAG = "EmployeeActivity";
    private RecyclerView rvEmployees;
    private FloatingActionButton fabCreateEmployee;
    private EmployeeAdapter employeeAdapter;
    private AttendanceViewModel viewModel;
    private LinearLayout layoutEmptyState;
    private List<UserItem> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        TokenManager tokenManager = new TokenManager(this);
        if (tokenManager.getToken() == null) {
            Toast.makeText(this, "Chưa đăng nhập, vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        rvEmployees = findViewById(R.id.rvEmployees);
        fabCreateEmployee = findViewById(R.id.fabCreateEmployee);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);

        employeeAdapter = new EmployeeAdapter(this, new EmployeeAdapter.OnEmployeeActionListener() {
            @Override
            public void onEdit(Employee employee) {
                showEditEmployeeDialog(employee);
            }

            @Override
            public void onDelete(Employee employee) {
                // Sử dụng departmentId hiện tại của employee
                int departmentId = 2;
                viewModel.deleteEmployee(employee, departmentId).observe(EmployeeActivity.this, success -> {
                    if (success) {
                        Toast.makeText(EmployeeActivity.this, "Nhân viên đã bị terminated", Toast.LENGTH_SHORT).show();
                        viewModel.loadEmployees();
                    } else {
                        Toast.makeText(EmployeeActivity.this, "Xóa nhân viên thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        rvEmployees.setLayoutManager(new LinearLayoutManager(this));
        rvEmployees.setAdapter(employeeAdapter);

        viewModel.getEmployeesLiveData().observe(this, employees -> {
            if (employees != null) {
                employeeAdapter.setEmployees(employees);
                rvEmployees.setVisibility(employees.isEmpty() ? View.GONE : View.VISIBLE);
                layoutEmptyState.setVisibility(employees.isEmpty() ? View.VISIBLE : View.GONE);
                if (employees.isEmpty()) {
                    Toast.makeText(this, "Danh sách nhân viên rỗng", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Không thể tải danh sách nhân viên", Toast.LENGTH_SHORT).show();
            }
        });



        viewModel.loadEmployees();
        fabCreateEmployee.setOnClickListener(v -> showCreateEmployeeDialog());
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
    }

    private void showCreateEmployeeDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_employee);

        final Spinner spinnerUsers = dialog.findViewById(R.id.spinnerUsers);
        final EditText etEmployeeCode = dialog.findViewById(R.id.etEmployeeCode);
        final EditText etFirstName = dialog.findViewById(R.id.etFirstName);
        final EditText etLastName = dialog.findViewById(R.id.etLastName);
        final EditText etPhone = dialog.findViewById(R.id.etPhone);
        final EditText etPosition = dialog.findViewById(R.id.etPosition);
        final EditText etHireDate = dialog.findViewById(R.id.etHireDate);
        final EditText etDepartmentId = dialog.findViewById(R.id.etDepartmentId);
        Button btnCreate = dialog.findViewById(R.id.btnCreate);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCreate.setEnabled(false);

        viewModel.getUsersWithoutEmployee().observe(this, response -> {
            if (response != null && response.isSuccess() && response.getData() != null) {
                userList.clear();
                List<String> usernames = new ArrayList<>();
                for (User user : response.getData()) {
                    if (user != null) {
                        userList.add(new UserItem(user.getId(), user.getUsername(), user.getEmail()));
                        usernames.add(user.getUsername());
                        Log.d(TAG, "User added: id=" + user.getId() + ", name=" + user.getUsername());
                    }
                }
                if (!usernames.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usernames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerUsers.setAdapter(adapter);
                    btnCreate.setEnabled(true);
                } else {
                    Toast.makeText(this, "Không có user nào khả dụng để tạo employee", Toast.LENGTH_SHORT).show();
                }
            } else {
                String errorMessage = response != null ? response.getMessage() : "No data";
                Toast.makeText(this, "Lỗi khi lấy danh sách user: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to get users without employee: " + errorMessage);
            }
        });

        btnCreate.setOnClickListener(v -> {
            int selectedPosition = spinnerUsers.getSelectedItemPosition();
            if (selectedPosition < 0 || selectedPosition >= userList.size()) {
                Toast.makeText(this, "Vui lòng chọn user", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer userId = userList.get(selectedPosition).id;
            String employeeCode = etEmployeeCode.getText().toString().trim();
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = userList.get(selectedPosition).email;
            String phone = etPhone.getText().toString().trim();
            String position = etPosition.getText().toString().trim();
            String hireDateInput = etHireDate.getText().toString().trim();
            String departmentIdStr = etDepartmentId.getText().toString().trim();

            int departmentId;
            try {
                departmentId = Integer.parseInt(departmentIdStr);
                if (departmentId <= 0) {
                    Toast.makeText(this, "Department ID phải là số dương", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Department ID phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            String hireDate = convertDateFormat(hireDateInput);
            if (userId == null || employeeCode.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                    phone.isEmpty() || position.isEmpty() || hireDate == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Employee newEmployee = new Employee();
            newEmployee.setId(userId);
            newEmployee.setEmployeeCode(employeeCode);
            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setEmail(email);
            newEmployee.setPhone(phone);
            newEmployee.setPosition(position);
            newEmployee.setHireDate(hireDate);

            Log.d(TAG, "Creating employee with userId: " + userId + ", " + newEmployee.toString());
            viewModel.createEmployee(newEmployee, departmentId).observe(this, success -> {
                if (success) {
                    dialog.dismiss();
                    viewModel.loadEmployees();
                    Toast.makeText(this, "Tạo nhân viên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi tạo nhân viên", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showEditEmployeeDialog(Employee employee) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_employee);

        final EditText etEmployeeCode = dialog.findViewById(R.id.etEmployeeCode);
        final EditText etFirstName = dialog.findViewById(R.id.etFirstName);
        final EditText etLastName = dialog.findViewById(R.id.etLastName);
        final EditText etPhone = dialog.findViewById(R.id.etPhone);
        final EditText etPosition = dialog.findViewById(R.id.etPosition);
        final EditText etHireDate = dialog.findViewById(R.id.etHireDate);
        final EditText etDepartmentId = dialog.findViewById(R.id.etDepartmentId);
        Button btnSave = dialog.findViewById(R.id.btnCreate);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnSave.setText("Lưu");

        // Điền thông tin hiện tại của nhân viên
        etEmployeeCode.setText(employee.getEmployeeCode());
        etFirstName.setText(employee.getFirstName());
        etLastName.setText(employee.getLastName());
        etPhone.setText(employee.getPhone());
        etPosition.setText(employee.getPosition());
        etHireDate.setText(employee.getHireDate() != null ? employee.getHireDate().split("T")[0].replace("-", "/") : "");
        //etDepartmentId.setText(String.valueOf(employee.getDepartmentId()));

        btnSave.setOnClickListener(v -> {
            String employeeCode = etEmployeeCode.getText().toString().trim();
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String position = etPosition.getText().toString().trim();
            String hireDateInput = etHireDate.getText().toString().trim();
            String departmentIdStr = etDepartmentId.getText().toString().trim();

            int departmentId;
            try {
                departmentId = Integer.parseInt(departmentIdStr);
                if (departmentId <= 0) {
                    Toast.makeText(this, "Department ID phải là số dương", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Department ID phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            String hireDate = convertDateFormat(hireDateInput);
            if (employeeCode.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                    phone.isEmpty() || position.isEmpty() ) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            employee.setId(employee.getId());
            employee.setEmployeeCode(employeeCode);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setPhone(phone);
            employee.setPosition(position);
            employee.setHireDate(hireDate);
            //employee.setDepartmentId(departmentId);

            viewModel.updateEmployee(employee, departmentId).observe(this, success -> {
                if (success) {
                    dialog.dismiss();
                    viewModel.loadEmployees();
                    Toast.makeText(this, "Cập nhật nhân viên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cập nhật nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private String convertDateFormat(String inputDate) {
        if (inputDate == null || !inputDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return null;
        }
        try {
            String[] parts = inputDate.split("/");
            return parts[2] + "-" + parts[1] + "-" + parts[0];
        } catch (Exception e) {
            return null;
        }
    }

    private static class UserItem {
        int id;
        String username;
        String email;

        UserItem(int id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }
    }

    private static class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
        private List<Employee> employees = new ArrayList<>();
        private Context context;
        private OnEmployeeActionListener listener;

        public EmployeeAdapter(Context context, OnEmployeeActionListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public void setEmployees(List<Employee> employees) {
            this.employees = employees != null ? employees : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
            Employee employee = employees.get(position);
            holder.tvName.setText(employee.getFullName());
            holder.tvUsername.setText(employee.getEmail() != null ? employee.getEmail() : "N/A");
            holder.tvStatus.setText(employee.getPhone() != null ? employee.getPhone() : "N/A");

            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(v -> {
                if (holder.btnEdit.getVisibility() == View.GONE) {
                    holder.btnEdit.setVisibility(View.VISIBLE);
                    holder.btnDelete.setVisibility(View.VISIBLE);
                } else {
                    holder.btnEdit.setVisibility(View.GONE);
                    holder.btnDelete.setVisibility(View.GONE);
                }
            });

            if (listener != null) {
                holder.btnEdit.setOnClickListener(v -> listener.onEdit(employee));
                holder.btnDelete.setOnClickListener(v -> listener.onDelete(employee));
            }
        }

        @Override
        public int getItemCount() {
            return employees.size();
        }

        static class EmployeeViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvUsername, tvStatus;
            Button btnEdit, btnDelete;

            EmployeeViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvEmployeeName);
                tvUsername = itemView.findViewById(R.id.tvEmployeeUsername);
                tvStatus = itemView.findViewById(R.id.tvEmployeeStatus);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }

        interface OnEmployeeActionListener {
            void onEdit(Employee employee);
            void onDelete(Employee employee);
        }
    }
}