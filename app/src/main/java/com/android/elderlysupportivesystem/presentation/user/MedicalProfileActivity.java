package com.android.elderlysupportivesystem.presentation.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Disease;
import com.android.elderlysupportivesystem.data.models.Medicine;
import com.android.elderlysupportivesystem.data.models.Relative;
import com.android.elderlysupportivesystem.datasource.SharedPreferencesDataSource;
import com.android.elderlysupportivesystem.di.Constants;
import com.android.elderlysupportivesystem.di.ViewModelProviderFactory;
import com.android.elderlysupportivesystem.presentation.SignInActivity;
import com.android.elderlysupportivesystem.presentation.viewmodels.MedicalProfileViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class MedicalProfileActivity extends DaggerAppCompatActivity implements MedicalProfileCallback {

    private static final int REQUEST_READ_CONTACTS_PERMISSION = 1;
    private static final int REQUEST_CONTACT = 0;
    private static final int REQUEST_LOCATION_CODE_PERMISSIONS = 100;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private String userId;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.civil_id)
    TextView civilId;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.blood_type)
    TextView bloodType;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.user_diseases_recyclerview)
    RecyclerView userDiseasesRecyclerView;
    @BindView(R.id.diseases_empty_view)
    View diseasesEmptyView;
    @BindView(R.id.user_medicines_recyclerview)
    RecyclerView userMedicinesRecyclerView;
    @BindView(R.id.medicines_empty_view)
    View medicineEmptyView;
    @BindView(R.id.user_relatives_recyclerview)
    RecyclerView userRelativesRecyclerView;
    @BindView(R.id.relatives_empty_view)
    View relativesEmptyView;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    MedicalProfileViewModel medicalProfileViewModel;
    private ArrayList<Disease> allDiseasesList;
    private ArrayList<Medicine> allMedicineList;
    private String qrCodeUrl;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location currentLocation;
    private String userAddress;
    private List<Relative> userRelativesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_profile);
        ButterKnife.bind(this);

        //request user location
        getLastLocation();
        userId = sharedPreferencesDataSource.getUserId();
        medicalProfileViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(MedicalProfileViewModel.class);
        medicalProfileViewModel.retrieveUserData(userId);
        medicalProfileViewModel.retrieveAllDiseases();
        medicalProfileViewModel.retrieveAllMedicines();
        medicalProfileViewModel.retrieveUserDiseases(userId);
        medicalProfileViewModel.retrieveUserMedicines(userId);
        medicalProfileViewModel.retrieveUserRelatives(userId);
        observeUserData();
        observeRetrieveAllDiseases();
        observeRetrieveAllMedicines();
        observeRetrieveUserDiseases();
        observeRetrieveUserMedicines();
        observeRetrieveUserRelatives();
        observeUserQrCode();
        observeError();
        observeUserLocationAddress();
        //detectShakeEvent
        detectShakeEvent();
    }

    private void observeUserLocationAddress() {
        medicalProfileViewModel.observeUserLocationAddressState().observe(this, address -> {
            userAddress = address;
        });
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // check if permissions are given
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            currentLocation = location;
                            medicalProfileViewModel.getUserAddress(MedicalProfileActivity.this, currentLocation.getLatitude(), currentLocation.getLongitude());
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            currentLocation = locationResult.getLastLocation();
            medicalProfileViewModel.getUserAddress(MedicalProfileActivity.this, currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS
        }, REQUEST_LOCATION_CODE_PERMISSIONS);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void observeError() {
        medicalProfileViewModel.observeErrorState().observe(this,
                error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
    }

    private void observeUserQrCode() {
        medicalProfileViewModel.observeQrCodeState().observe(this, qrCodeUrl -> {
            this.qrCodeUrl = qrCodeUrl;
            Intent intent = new Intent(this, QrCodeActivity.class);
            intent.putExtra(Constants.QR_CODE, qrCodeUrl);
            startActivity(intent);
            sendLocationForRelatives();
        });
    }

    private void observeUserData() {
        medicalProfileViewModel.observeUserState().observe(this, user -> {
            userName.setText(user.getUserName());
            civilId.setText(user.getCivilId());
            phone.setText(user.getPhone());
            address.setText(user.getAddress());
            bloodType.setText(user.getBloodType());
            age.setText(user.getAge());
        });
    }

    private void observeRetrieveUserDiseases() {
        medicalProfileViewModel.observeUserDiseaseListLiveData().observe(this, userDiseasesMap -> {
            if (userDiseasesMap == null || userDiseasesMap.isEmpty()) {
                diseasesEmptyView.setVisibility(View.VISIBLE);
                userDiseasesRecyclerView.setVisibility(View.GONE);
            } else {
                diseasesEmptyView.setVisibility(View.GONE);
                userDiseasesRecyclerView.setVisibility(View.VISIBLE);

                UserDiseasesAdapter userDiseasesAdapter = new UserDiseasesAdapter(userDiseasesMap);
                userDiseasesRecyclerView.setAdapter(userDiseasesAdapter);
            }
        });
    }

    private void observeRetrieveUserMedicines() {
        medicalProfileViewModel.observeUserMedicineListLiveData().observe(this, userMedicinesMap -> {
            if (userMedicinesMap == null || userMedicinesMap.isEmpty()) {
                medicineEmptyView.setVisibility(View.VISIBLE);
                userMedicinesRecyclerView.setVisibility(View.GONE);
            } else {
                medicineEmptyView.setVisibility(View.GONE);
                userMedicinesRecyclerView.setVisibility(View.VISIBLE);

                UserMedicinesAdapter userMedicinesAdapter = new UserMedicinesAdapter(userMedicinesMap);
                userMedicinesRecyclerView.setAdapter(userMedicinesAdapter);
            }
        });
    }

    private void observeRetrieveUserRelatives() {
        medicalProfileViewModel.observeUserRelativesListLiveData().observe(this, userRelativesList -> {
            if (userRelativesList == null || userRelativesList.isEmpty()) {
                relativesEmptyView.setVisibility(View.VISIBLE);
                userRelativesRecyclerView.setVisibility(View.GONE);
            } else {
                this.userRelativesList = userRelativesList;
                relativesEmptyView.setVisibility(View.GONE);
                userRelativesRecyclerView.setVisibility(View.VISIBLE);

                UserRelativesAdapter userRelativesAdapter = new UserRelativesAdapter(userRelativesList);
                userRelativesRecyclerView.setAdapter(userRelativesAdapter);
            }
        });
    }

    private void observeRetrieveAllDiseases() {
        medicalProfileViewModel.observeAllDiseaseListLiveData().observe(this, allDiseasesList -> {
            this.allDiseasesList = (ArrayList<Disease>) allDiseasesList;
        });
    }

    private void observeRetrieveAllMedicines() {
        medicalProfileViewModel.observeAllMedicinesListLiveData().observe(this, allMedicineList -> {
            this.allMedicineList = (ArrayList<Medicine>) allMedicineList;
        });
    }

    @OnClick(R.id.add_disease)
    public void onAddNewDiseaseClicked() {
        List<String> statues = Arrays.asList(getResources().getStringArray(R.array.statues));
        AddUserDiseaseFragment addUserDiseaseFragment = AddUserDiseaseFragment.newInstance(allDiseasesList, new ArrayList<>(statues));
        addUserDiseaseFragment.show(getSupportFragmentManager(), AddUserDiseaseFragment.TAG);
    }

    @OnClick(R.id.add_medicine)
    public void onAddNewMedicineClicked() {
        AddUserMedicineFragment addUserMedicineFragment = AddUserMedicineFragment.newInstance(allMedicineList);
        addUserMedicineFragment.show(getSupportFragmentManager(), AddUserMedicineFragment.TAG);
    }

    @OnClick(R.id.add_relative)
    public void onAddNewRelativeClicked() {
        if (!hasContactsPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
        } else {
            Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(pickContact, REQUEST_CONTACT);
        }
    }

    @Override
    public void onAddNewUserDisease(Disease selectedDisease, String status) {
        medicalProfileViewModel.addUserDisease(selectedDisease, status, userId);
        Toast.makeText(this, "New user is added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddNewUserMedicine(Medicine selectedMedicine, String notes) {
        medicalProfileViewModel.addUserMedicine(selectedMedicine, notes, userId);
    }

    @OnClick(R.id.sign_out)
    public void onSignOutClicked() {
        FirebaseAuth.getInstance().signOut();
        sharedPreferencesDataSource.removeAllValues();
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.send_code)
    public void onSendQrCodeClicked() {
        medicalProfileViewModel.retrieveUserQrCode(userId);
    }

    private void detectShakeEvent() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private boolean hasContactsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void requestContactsPermission() {
        if (!hasContactsPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION && grantResults.length > 0) {
            Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(pickContact, REQUEST_CONTACT);
        } else if (requestCode == REQUEST_LOCATION_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER};

            try (Cursor c = this.getContentResolver().query(contactUri, queryFields, null, null, null)) {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String hasPhone = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);
                    phones.moveToFirst();
                    String number = phones.getString(phones.getColumnIndexOrThrow("data1"));
                    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    Relative relative = new Relative();
                    relative.setName(name);
                    relative.setPhone(number);
                    medicalProfileViewModel.addUserRelative(relative, userId);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        if (checkPermissions()) {
            getLastLocation();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> msgArray = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(phoneNo, null, msgArray, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 10) {
                medicalProfileViewModel.retrieveUserQrCode(userId);
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void sendLocationForRelatives() {
        for (Relative relative : userRelativesList) {
            sendSMS(relative.getPhone(), "Please help, I'm very tired and this is my location: "
                    + userAddress);
        }
        Toast.makeText(this, "Location has been sent to your relatives", Toast.LENGTH_SHORT).show();
    }
}