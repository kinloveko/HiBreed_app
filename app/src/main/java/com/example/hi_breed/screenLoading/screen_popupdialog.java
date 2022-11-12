package com.example.hi_breed.screenLoading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hi_breed.R;

public class screen_popupdialog extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_popupdialog);

    }

    public void Owner_onClick(View view) {
        Intent intent = new Intent(screen_popupdialog.this, screen_LoadingActivity.class);
        startActivity(intent);
    }

    public void Breeder_onClick(View view) {
        Intent intent = new Intent(screen_popupdialog.this, screen_LoadingBreeders.class);
        startActivity(intent);
    }
}