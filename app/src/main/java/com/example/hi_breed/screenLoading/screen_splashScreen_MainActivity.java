package com.example.hi_breed.screenLoading;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.hi_breed.R;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class screen_splashScreen_MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    Animation TopAnim,BottomAnim;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.screen_activity_main);
      //animation
        TopAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        BottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        image= findViewById(R.id.imageView2);
        //  logo= findViewById(R.id.textView);

        image.setAnimation(TopAnim);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(user !=null){
            startActivity(new Intent(this, user_dashboard.class));
            this.finish();
        }
        else{
            transition();
        }
    }



    private void transition(){
        final screen_splashScreen_MainActivity sPlashScreen = this;

        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(3000);
                    }
                } catch (InterruptedException ignored) {
                }
                finish();

                Intent intent = new Intent();
                intent.setClass(sPlashScreen, screen_WelcomeToHiBreed.class);
                startActivity(intent);
                overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out);
            }
        };

        mSplashThread.start();
    }
}