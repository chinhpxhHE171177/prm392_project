package com.example.workforcemanagement.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workforcemanagement.R;
import com.example.workforcemanagement.data.model.UpdateProfileResponse;
import com.example.workforcemanagement.data.model.User;
import com.example.workforcemanagement.data.api.ApiClient;
import com.example.workforcemanagement.data.api.ApiService;
import com.example.workforcemanagement.util.TokenManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeEditProfile extends AppCompatActivity {

    private TextInputEditText etEmployeeCode, etFirstName, etLastName, etEmail, etPhone;
    private Button btnSave;
    private ImageButton btnBack;
    private User user;
    private TokenManager tokenManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit_profile);

        // Initialize views
        etEmployeeCode = findViewById(R.id.etEmployeeCode);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Initialize services
        tokenManager = new TokenManager(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        // Get user from intent
        user = (User) getIntent().getSerializableExtra("user");

        // Debug logging
        if (user != null) {
            android.util.Log.d("EmployeeEditProfile", "User ID: " + user.getId());
            android.util.Log.d("EmployeeEditProfile", "User Role: " + user.getRole());
            android.util.Log.d("EmployeeEditProfile", "Profile Type: " + user.getProfileType());
            android.util.Log.d("EmployeeEditProfile", "First Name: " + user.getFirstName());
            android.util.Log.d("EmployeeEditProfile", "Last Name: " + user.getLastName());
            android.util.Log.d("EmployeeEditProfile", "Email: " + user.getEmail());
            android.util.Log.d("EmployeeEditProfile", "Phone: " + user.getPhone());
            android.util.Log.d("EmployeeEditProfile", "Employee Code: " + user.getEmployeeCode());
        } else {
            android.util.Log.e("EmployeeEditProfile", "User is null!");
        }

        // Fix: Use safer null checking for profileType
        if (user == null) {
            Toast.makeText(this, "Error: User data not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Allow access if user is employee OR if profileType is null but role is employee
        String profileType = user.getProfileType();
        String role = user.getRole();

        if (profileType != null && !"employee".equals(profileType)) {
            Toast.makeText(this, "Error: This screen is for employees only", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // If profileType is null but role is employee, we'll allow it
        if (profileType == null && (role == null || (!"employee".equals(role) && !"dep_manager".equals(role)))) {
            Toast.makeText(this, "Error: Invalid user profile", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Populate fields with null safety
        etEmployeeCode.setText(user.getEmployeeCode() != null ? user.getEmployeeCode() : "");
        etFirstName.setText(user.getFirstName() != null ? user.getFirstName() : "");
        etLastName.setText(user.getLastName() != null ? user.getLastName() : "");
        etEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        etPhone.setText(user.getPhone() != null ? user.getPhone() : "");

        // Disable Save button by default
        btnSave.setEnabled(false);

        // Add TextWatchers to enable Save button on changes
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                // Safer comparison with null safety
                boolean hasChanges = !firstName.equals(user.getFirstName() != null ? user.getFirstName() : "") ||
                        !lastName.equals(user.getLastName() != null ? user.getLastName() : "") ||
                        !email.equals(user.getEmail() != null ? user.getEmail() : "") ||
                        !phone.equals(user.getPhone() != null ? user.getPhone() : "");

                btnSave.setEnabled(hasChanges);
            }
        };

        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        etPhone.addTextChangedListener(textWatcher);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Save button
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String position = user.getPosition(); // Keep existing position

        // Validate inputs
        if (!TextUtils.isEmpty(email) && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare update data
        Map<String, Object> updateFields = new HashMap<>();
        if (!firstName.equals(user.getFirstName() != null ? user.getFirstName() : "")) updateFields.put("first_name", firstName);
        if (!lastName.equals(user.getLastName() != null ? user.getLastName() : "")) updateFields.put("last_name", lastName);
        if (!email.equals(user.getEmail() != null ? user.getEmail() : "")) updateFields.put("email", email);
        if (!phone.equals(user.getPhone() != null ? user.getPhone() : "")) updateFields.put("phone", phone);
        if (!TextUtils.isEmpty(position)) updateFields.put("position", position);

        String token = tokenManager.getToken();
        if (token == null) {
            Toast.makeText(this, "No authentication token found", Toast.LENGTH_SHORT).show();
            return;
        }

        // API call
        apiService.updateProfile("Bearer " + token, updateFields).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(EmployeeEditProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    // Update local user object with new values
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    user.setPhone(phone);

                    // Return updated user to UserProfileActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updated_user", user);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    String errorMessage = response.body() != null ? response.body().getMessage() : "Failed to update profile";
                    Toast.makeText(EmployeeEditProfile.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Toast.makeText(EmployeeEditProfile.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}