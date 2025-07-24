package com.example.workforcemanagement.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.Employee;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.ui.login.LoginActivity;
import com.example.workforcemanagement.ui.main.AdminDashboardActivity;
import com.example.workforcemanagement.ui.main.EmployeeDashboardActivity;
import com.example.workforcemanagement.ui.main.HRDashboardActivity;
import com.example.workforcemanagement.ui.main.MainActivity;
import com.example.workforcemanagement.ui.main.ManagerDashboardActivity;
import com.example.workforcemanagement.util.TokenManager;
import com.example.workforcemanagement.util.ProfileHelper;

public class UserProfileActivity extends AppCompatActivity {
    private User currentUser;
    private TextView tvFullName, tvUserPosition, tvUserEmail, tvUserPhone, tvUserDepartment, tvUserRole;

    // ActivityResultLauncher để nhận kết quả từ EditProfile
    private ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Nhận user đã được update từ EditProfile
                    User updatedUser = (User) result.getData().getSerializableExtra("updated_user");
                    if (updatedUser != null) {
                        currentUser = updatedUser;
                        displayUserInfo(currentUser);
                        android.util.Log.d("UserProfileActivity", "Profile updated successfully, refreshing display");
                    } else {
                        // Nếu không nhận được updated user, load lại từ API
                        android.util.Log.d("UserProfileActivity", "No updated user received, reloading from API");
                        loadFreshProfileData();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Khởi tạo các view
        tvFullName = findViewById(R.id.tvFullName);
        tvUserPosition = findViewById(R.id.tvUserPosition);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvUserDepartment = findViewById(R.id.tvUserDepartment);
        tvUserRole = findViewById(R.id.tvUserRole);
        LinearLayout quickCheckIn = findViewById(R.id.quickCheckIn);
        LinearLayout ivLogout = findViewById(R.id.ivLogout);

        // Lấy user từ Intent trước
        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            currentUser = user;
            displayUserInfo(user);

            // Load fresh data từ API để có đầy đủ thông tin
            loadFreshProfileData();
        } else {
            // Nếu không có user từ intent, load từ API
            loadFreshProfileData();
        }

        // Setup click listeners
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh profile data when returning to this activity
        if (currentUser != null) {
            loadFreshProfileData();
        }
    }

    private void loadFreshProfileData() {
        ProfileHelper.loadUserProfile(this, new ProfileHelper.ProfileCallback() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                displayUserInfo(user);
                android.util.Log.d("UserProfileActivity", "Fresh profile data loaded successfully");
            }

            @Override
            public void onError(String message) {
                android.util.Log.e("UserProfileActivity", "Failed to load fresh profile: " + message);
                Toast.makeText(UserProfileActivity.this, "Failed to load profile: " + message, Toast.LENGTH_SHORT).show();
                // Nếu load fail và không có user ban đầu, về login
                if (currentUser == null) {
                    Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void displayUserInfo(User user) {
        // Debug logging
        if (user != null) {
            android.util.Log.d("UserProfileActivity", "Displaying user info:");
            android.util.Log.d("UserProfileActivity", "User ID: " + user.getId());
            android.util.Log.d("UserProfileActivity", "User Role: " + user.getRole());
            android.util.Log.d("UserProfileActivity", "Profile Type: " + user.getProfileType());
            android.util.Log.d("UserProfileActivity", "Full Name: " + user.getFullName());
            android.util.Log.d("UserProfileActivity", "First Name: " + user.getFirstName());
            android.util.Log.d("UserProfileActivity", "Last Name: " + user.getLastName());
            android.util.Log.d("UserProfileActivity", "Position: " + user.getPosition());
            android.util.Log.d("UserProfileActivity", "Phone: " + user.getPhone());
            android.util.Log.d("UserProfileActivity", "Employee Code: " + user.getEmployeeCode());
            android.util.Log.d("UserProfileActivity", "Email: " + user.getEmail());
            if (user.getDepartment() != null) {
                android.util.Log.d("UserProfileActivity", "Department: " + user.getDepartment().getName());
            } else {
                android.util.Log.d("UserProfileActivity", "Department: null");
            }
        } else {
            android.util.Log.e("UserProfileActivity", "User is null!");
        }

        // Hiển thị thông tin user với null safety
        if (user != null) {
            // Cố gắng hiển thị fullName, nếu không có thì ghép firstName + lastName
            String displayName = user.getFullName();
            if (displayName == null || displayName.trim().isEmpty()) {
                String firstName = user.getFirstName() != null ? user.getFirstName() : "";
                String lastName = user.getLastName() != null ? user.getLastName() : "";
                displayName = (firstName + " " + lastName).trim();
                if (displayName.isEmpty()) {
                    displayName = user.getUsername() != null ? user.getUsername() : "N/A";
                }
            }

            tvFullName.setText(displayName);
            tvUserEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
            tvUserRole.setText(user.getRole() != null ? user.getRole() : "N/A");
            tvUserPosition.setText(user.getPosition() != null ? user.getPosition() : "N/A");
            tvUserPhone.setText(user.getPhone() != null ? user.getPhone() : "N/A");
            tvUserDepartment.setText(user.getDepartment() != null && user.getDepartment().getName() != null
                    ? user.getDepartment().getName() : "N/A");

            android.util.Log.d("UserProfileActivity", "UI Updated - Name: " + displayName + ", Email: " + user.getEmail());
        } else {
            // User null
            tvFullName.setText("Unknown User");
            tvUserPosition.setText("N/A");
            tvUserEmail.setText("N/A");
            tvUserPhone.setText("N/A");
            tvUserDepartment.setText("N/A");
            tvUserRole.setText("N/A");
            Toast.makeText(this, "Error: User profile data not available", Toast.LENGTH_LONG).show();
        }
    }

    private void setupClickListeners() {
        LinearLayout quickCheckIn = findViewById(R.id.quickCheckIn);
        LinearLayout ivLogout = findViewById(R.id.ivLogout);

        // Xử lý nút Home
        quickCheckIn.setOnClickListener(v -> {
            Intent intent;
            if (currentUser != null && currentUser.getRole() != null) {
                switch (currentUser.getRole()) {
                    case "admin":
                        intent = new Intent(this, AdminDashboardActivity.class);
                        break;
                    case "hr":
                        intent = new Intent(this, HRDashboardActivity.class);
                        break;
                    case "dep_manager":
                        intent = new Intent(this, ManagerDashboardActivity.class);
                        break;
                    case "employee":
                        intent = new Intent(this, EmployeeDashboardActivity.class);
                        break;
                    default:
                        intent = new Intent(this, MainActivity.class);
                        break;
                }
                // Truyền user object để dashboard sử dụng nếu cần
                intent.putExtra("user", currentUser);
                startActivity(intent);
                finish(); // Kết thúc UserProfileActivity để không quay lại
            } else {
                Toast.makeText(this, "Error: User role not found", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý nút Edit Profile với null safety
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            if (currentUser != null) {
                Intent intent;
                String profileType = currentUser.getProfileType();
                String role = currentUser.getRole();

                // Debug log trước khi quyết định Activity nào
                android.util.Log.d("UserProfileActivity", "Edit Profile clicked - ProfileType: " + profileType + ", Role: " + role);

                // Quyết định Activity dựa trên profileType hoặc role
                if ("admin".equals(profileType) || ("admin".equals(role) && profileType == null)) {
                    intent = new Intent(this, AdminEditProfile.class);
                } else {
                    // Mặc định sử dụng EmployeeEditProfile cho employee, dep_manager và các role khác
                    intent = new Intent(this, EmployeeEditProfile.class);
                }

                intent.putExtra("user", currentUser);
                android.util.Log.d("UserProfileActivity", "Starting edit profile activity with user data");

                // Sử dụng ActivityResultLauncher thay vì startActivity
                editProfileLauncher.launch(intent);
            } else {
                Toast.makeText(this, "Error: User data not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý logout
        ivLogout.setOnClickListener(v -> {
            TokenManager tokenManager = new TokenManager(this);
            tokenManager.clearToken();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}