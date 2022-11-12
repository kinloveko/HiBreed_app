package com.example.hi_breed.screenLoading;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.hi_breed.R;
import com.example.hi_breed.loginAndRegistration.RegistrationForOwner;

public class screen_LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(screen_LoadingActivity.this, RegistrationForOwner.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}



