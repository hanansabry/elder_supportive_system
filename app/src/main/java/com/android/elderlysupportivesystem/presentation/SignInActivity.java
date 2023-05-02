package com.android.elderlysupportivesystem.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.datasource.SharedPreferencesDataSource;
import com.android.elderlysupportivesystem.di.ViewModelProviderFactory;
import com.android.elderlysupportivesystem.presentation.admin.AdminActivity;
import com.android.elderlysupportivesystem.presentation.user.MedicalProfileActivity;
import com.android.elderlysupportivesystem.presentation.viewmodels.AuthenticationViewModel;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

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
                if (user.getRole() != null && user.getRole().equals("admin")) {
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
        startActivity(new Intent(this, MedicalProfileActivity.class));
    }

    private void startAdminFlow() {
        startActivity(new Intent(this, AdminActivity.class));
    }

    @OnClick(R.id.sign_up)
    public void onSignUpClicked() {
        startActivity(new Intent(this, SignUpActivity.class));
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
        startActivity(new Intent(this, ScanQrCodeActivity.class));
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}