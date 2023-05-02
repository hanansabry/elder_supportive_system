package com.android.elderlysupportivesystem.presentation.user;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.di.Constants;
import com.bumptech.glide.Glide;

public class QrCodeActivity extends AppCompatActivity {

    @BindView(R.id.qr_code_image_view)
    ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.bind(this);

        String qrCodeUrl = getIntent().getStringExtra(Constants.QR_CODE);
        Glide.with(this)
                .load(qrCodeUrl)
                .into(qrCodeImageView);
    }
}