package com.example.hi_breed.loginAndRegistration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi_breed.R;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.example.hi_breed.screenLoading.screen_popupdialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.droidsonroids.gif.GifImageView;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int i = -1;
    private FirebaseUser userAuth;
    TextInputLayout emailView , passView ;
    TextInputEditText emailEdit, passEdit;
        Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailView = findViewById(R.id.email_layout);
        passView = findViewById(R.id.password_layout);
        emailEdit = findViewById(R.id.email_Edit);
        passEdit = findViewById(R.id.password_Edit);
        signIn = findViewById(R.id.submitButton);
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        signIn = findViewById(R.id.submitButton);




        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null)
                {
                    emailView.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null)
                {
                    passView.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    public void ClickMeRegister(View view) {
        startActivity(new Intent(this, screen_popupdialog.class));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checker() {

        String email = Objects.requireNonNull(emailEdit.getText()).toString();
        String pass = Objects.requireNonNull(passEdit.getText()).toString();


        if (email.isEmpty()) {
            Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
            emailEdit.requestFocus();
            return;

        }  else {
            emailView.setError("");
        }



        if (pass.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            passEdit.requestFocus();
            return;
        } else if (pass.length() < 5) {
            Toast.makeText(this, "Password must have 5 characters", Toast.LENGTH_SHORT).show();
            passEdit.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        View view = View.inflate(Login.this,R.layout.screen_custom_alert,null);
        TextView title = view.findViewById(R.id.screen_custom_alert_title);
        builder.setCancelable(false);
        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
        imageViewCompat.setVisibility(View.GONE);
        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
        gif.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        TextView message = view.findViewById(R.id.screen_custom_alert_message);
        message.setVisibility(View.GONE);
        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                                     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user.isEmailVerified()){
                                     alert.dismiss();

                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Login.this);
                                        builder2.setCancelable(false);
                                        View view = View.inflate(Login.this,R.layout.screen_custom_alert,null);
                                        //title
                                        TextView title = view.findViewById(R.id.screen_custom_alert_title);
                                        //loading text
                                        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                                        loadingText.setVisibility(View.GONE);
                                        //gif
                                        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                                        Uri uri = Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.kawaii_gif);
                                        gif.setImageURI(uri);
                                        //header image
                                        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                                        imageViewCompat.setVisibility(View.VISIBLE);
                                        imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
                                        //message
                                        TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                        title.setText("Login Success");
                                        message.setVisibility(View.GONE);
                                        //button


                                        builder2.setView(view);
                                        AlertDialog alert2 = builder2.create();
                                        alert2.show();
                                        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        emailView.setError("");
                                        passView.setError("");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent i = new Intent(Login.this,user_dashboard.class);
                                                // set the new task and clear flags
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                            }
                                        },2000);

                                    }
                                    else{
                                        //if user is registered but not verified
                                        user.sendEmailVerification();
                                        alert.dismiss();
                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(Login.this);
                                        View view = View.inflate(Login.this,R.layout.screen_custom_alert,null);
                                        builder3.setCancelable(false);
                                        TextView title = view.findViewById(R.id.screen_custom_alert_title);
                                        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                                        loadingText.setVisibility(View.GONE);
                                        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                                        imageViewCompat.setVisibility(View.VISIBLE);
                                        imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_error_border));
                                        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                                        gif.setVisibility(View.GONE);
                                        TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                        title.setText("Please check your email inbox");
                                        message.setText("We sent you an email verification. Click the link and login again.");
                                        LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                        buttonLayout.setVisibility(View.VISIBLE);
                                        MaterialButton cancel,okay;
                                        cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                        cancel.setVisibility(View.GONE);
                                        okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                        okay.setText("Got it");
                                        okay.setBackgroundColor(Color.parseColor("#adb6c4"));
                                        okay.setTextColor(Color.WHITE);
                                        builder3.setView(view);
                                        AlertDialog alert3 = builder3.create();
                                        alert3.show();
                                        alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        okay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alert3.dismiss();
                                                emailEdit.getText().clear();
                                                passEdit.getText().clear();
                                                emailView.setError("");
                                                passView.setError("");
                                            }
                                        });
                                    }
                    } else {
                        alert.dismiss();
                        //Account doesn't Exist
                        emailView.setError("");
                        passView.setError("");

                        AlertDialog.Builder builder3 = new AlertDialog.Builder(Login.this);
                        View view = View.inflate(Login.this,R.layout.screen_custom_alert,null);
                        builder3.setCancelable(false);
                        TextView title = view.findViewById(R.id.screen_custom_alert_title);
                        TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                        loadingText.setVisibility(View.GONE);
                        AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                        imageViewCompat.setVisibility(View.VISIBLE);
                        imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_error_border));
                        GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                        gif.setVisibility(View.GONE);
                        TextView message = view.findViewById(R.id.screen_custom_alert_message);
                        title.setText("Account doesn't exist");
                        message.setText("Don't have an account? Click Register button.");
                        LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                        buttonLayout.setVisibility(View.VISIBLE);
                        MaterialButton cancel,okay;
                        cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                        okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                        okay.setText("Register");
                        okay.setBackgroundColor(Color.parseColor("#F6B75A"));
                        okay.setTextColor(Color.WHITE);
                        builder3.setView(view);
                        AlertDialog alert4 = builder3.create();
                        alert4.show();
                        alert4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert4.dismiss();
                            }
                        });
                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert4.dismiss();
                                startActivity(new Intent(Login.this,screen_popupdialog.class));
                                finish();
                            }
                        });
                    }
                }
        });

    }

    public void ClickMeLogin(View view) {
        // Variables
        checker();
    }


}
