package com.android.patienttrackingsystem.datasource;

import android.content.SharedPreferences;

import com.android.patienttrackingsystem.di.Constants;

import javax.inject.Inject;

public class SharedPreferencesDataSource {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    @Inject
    public SharedPreferencesDataSource(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public void saveUserId(String userId) {
        editor.putString(Constants.USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(Constants.USER_ID, null);
    }

    public void setIsAdmin(boolean isAdmin) {
        editor.putBoolean(Constants.IS_ADMIN, isAdmin);
        editor.apply();
    }

    public boolean isAdmin() {
        return sharedPreferences.getBoolean(Constants.IS_ADMIN, false);
    }
}
