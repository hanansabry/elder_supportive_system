package com.android.patienttrackingsystem.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.User;
import com.android.patienttrackingsystem.datasource.SharedPreferencesDataSource;
import com.android.patienttrackingsystem.di.ViewModelProviderFactory;
import com.android.patienttrackingsystem.presentation.admin.AdminActivity;
import com.android.patienttrackingsystem.presentation.user.UserHomeActivity;
import com.android.patienttrackingsystem.presentation.viewmodels.AuthenticationViewModel;

import javax.inject.Inject;

public class SignInActivity extends DaggerAppCompatActivity {

    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.sign_in)
    Button signInButton;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    AuthenticationViewModel authenticationViewModel;
    private boolean loginAsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        authenticationViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AuthenticationViewModel.class);
        authenticationViewModel.observeUserState().observe(this, user -> {
            if (user != null) {
                sharedPreferencesDataSource.saveUserId(user.getId());
                if (user.getRole().equals("admin")) {
                    sharedPreferencesDataSource.setIsAdmin(true);
                    startAdminFlow();
                } else {
                    startUserFlow();
                }
            } else {
                Toast.makeText(this, R.string.sign_in_general_error_msg, Toast.LENGTH_SHORT).show();
            }
        });

        authenticationViewModel.observeErrorState().observe(this, error -> {
            Toast.makeText(this, R.string.sign_in_general_error_msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void startUserFlow() {
        startActivity(new Intent(this, UserHomeActivity.class));
    }

    private void startAdminFlow() {
        startActivity(new Intent(this, AdminActivity.class));
    }

    @OnClick(R.id.sign_up)
    public void onSignUpClicked() {

    }

    @OnClick(R.id.sign_in)
    public void onSignInClicked() {
        loginAsAdmin = false;
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()) {
            authenticationViewModel.signIn(email, password, loginAsAdmin);
        } else {
            Toast.makeText(this, R.string.email_password_required, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.login_as_admin)
    public void onLoginAsAdminClicked() {
        loginAsAdmin = true;
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()) {
            authenticationViewModel.signIn(email, password, loginAsAdmin);
        } else {
            Toast.makeText(this, R.string.email_password_required, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.scan_qr_code)
    public void onScanQrCodeClicked() {

    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}