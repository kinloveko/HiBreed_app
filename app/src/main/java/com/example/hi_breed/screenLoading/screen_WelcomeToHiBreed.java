package com.example.hi_breed.screenLoading;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hi_breed.loginAndRegistration.Login;
import com.example.hi_breed.R;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class screen_WelcomeToHiBreed extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_welcome_to_hi_breed);


    }


    public void SignIn_Onclick(View view) {
                Intent intent = new Intent(screen_WelcomeToHiBreed.this, Login.class);
                startActivity(intent);
    }


    public void createAccount_Onclick(View view) {
        {
            startActivity(new Intent(this, screen_popupdialog.class));
        }

    }

    }
