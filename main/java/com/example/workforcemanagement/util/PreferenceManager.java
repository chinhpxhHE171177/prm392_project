// PreferenceManager.java
package com.example.workforcemanagement.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private final SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "app_preferences";
    public static final String KEY_TOKEN = "user_token";

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setRememberMe(boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public boolean isRememberMeEnabled() {
        return sharedPreferences.getBoolean(Constants.KEY_REMEMBER_ME, false);
    }

    public void saveEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(Constants.KEY_EMAIL, "");
    }

    public void clearEmail() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.KEY_EMAIL);
        editor.apply();
    }

    public void setUserId(int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt("user_id", -1);
    }

    public void setEmployeeId(int employeeId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("employee_id", employeeId);
        editor.apply();
    }

    public int getEmployeeId() {
        return sharedPreferences.getInt("employee_id", -1);
    }
}