package com.android.patienttrackingsystem.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.User;
import com.android.patienttrackingsystem.datasource.SharedPreferencesDataSource;
import com.android.patienttrackingsystem.di.ViewModelProviderFactory;
import com.android.patienttrackingsystem.presentation.user.MedicalProfileActivity;
import com.android.patienttrackingsystem.presentation.viewmodels.AuthenticationViewModel;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class SignUpActivity extends DaggerAppCompatActivity {

    @BindView(R.id.username_edit_text)
    EditText userNameEditText;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.civil_id_edit_text)
    EditText civilIdEditText;
    @BindView(R.id.phone_edit_text)
    EditText phoneEditText;
    @BindView(R.id.address_edit_text)
    EditText addressEditText;
    @BindView(R.id.age_edit_text)
    EditText ageEditText;
    @BindView(R.id.blood_type_spinner)
    Spinner bloodTypeSpinner;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.generate_qr_code)
    ImageView qrCode;

    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    AuthenticationViewModel authenticationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        authenticationViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(AuthenticationViewModel.class);
        authenticationViewModel.observeUserState().observe(this, user -> {
            if (user != null) {
                sharedPreferencesDataSource.saveUserId(user.getId());
                //generate qr code and save it to user data
                generateQrCode(user.getId());
                startActivity(new Intent(this, MedicalProfileActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Can't create new account, please try again later", Toast.LENGTH_SHORT).show();
            }
        });

        authenticationViewModel.observeErrorState().observe(this, error -> {
            Toast.makeText(this, "Can't create new account, please try again later", Toast.LENGTH_SHORT).show();
        });
    }

    private void generateQrCode(String id) {
        qrCode.setImageBitmap(authenticationViewModel.generateQrCode(id));
        //save image to firebase storage
        authenticationViewModel.saveQrCodeToFirebaseStorage(id, authenticationViewModel.generateQrCode(id));
    }

    @OnClick(R.id.sign_up)
    public void onSignUpClicked() {
        String userName = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String civilId = civilIdEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String age = ageEditText.getText().toString();
        int bloodTypeIndex = bloodTypeSpinner.getSelectedItemPosition();
        int genderIndex = genderSpinner.getSelectedItemPosition();

        if (userName.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || civilId.isEmpty()
                || phone.isEmpty()
                || address.isEmpty()
                || age.isEmpty()
                || bloodTypeIndex == 0
                || genderIndex == 0
        ) {
            Toast.makeText(this, "You must enter all values", Toast.LENGTH_SHORT).show();
        } else {
            User user = new User();
            user.setUserName(userName);
            user.setEmail(email);
            user.setPassword(password);
            user.setCivilId(civilId);
            user.setPhone(phone);
            user.setAddress(address);
            user.setAge(age);
            user.setBloodType(bloodTypeSpinner.getSelectedItem().toString());
            user.setGender(genderSpinner.getSelectedItem().toString());

            authenticationViewModel.signUp(user);
        }
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}