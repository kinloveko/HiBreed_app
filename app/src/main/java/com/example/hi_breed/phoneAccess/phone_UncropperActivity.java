package com.example.hi_breed.phoneAccess;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.hi_breed.R;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class phone_UncropperActivity extends AppCompatActivity {

    String sourceUri, destinationUri;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_uncropper);


        Intent intent = getIntent();
        if(intent.getExtras()!=null){
                sourceUri = intent.getStringExtra("SendingData");
                uri = Uri.parse(sourceUri);
        }

        destinationUri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop.Options options = new UCrop.Options();
        UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationUri)))
                .withOptions(options)
                .withMaxResultSize(2000,2000)
                .start(phone_UncropperActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== UCrop.REQUEST_CROP){
            final Uri resultUri = UCrop.getOutput(data);
            Intent intent = new Intent();
            intent.putExtra("CROPS",resultUri+"");
            setResult(102,intent);
            finish();
        }
        else if(resultCode==UCrop.RESULT_ERROR){
            onBackPressed();
            finish();
        }
        else {
            startActivity(new Intent(this, user_dashboard.class));
           finish();
        }
    }

}