package com.example.hi_breed.loginAndRegistration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hi_breed.R;
import com.example.hi_breed.userFile.dashboard.user_dashboard;
import com.example.hi_breed.screenLoading.screen_popupdialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int i = -1;
    private FirebaseUser userAuth;
    TextInputLayout emailView , passView ;
    TextInputEditText emailEdit, passEdit;
        Button signIn;

    final Pattern p = Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");
    final String passError = "Password must contains at least 1 special character[ex.@#$%!^&+],at least 5 characters and no white spaces ";

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

    private void checker() {

        String email = Objects.requireNonNull(emailEdit.getText()).toString();
        String pass = Objects.requireNonNull(passEdit.getText()).toString();

        CharSequence emailcs = emailEdit.getText();
        if (email.isEmpty()) {
            Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
            emailEdit.requestFocus();
            return;

        } else if (!(Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailcs))) {
            Toast.makeText(this, "Please input a valid email ex. example@gmail.com", Toast.LENGTH_SHORT).show();
            emailEdit.requestFocus();
            return;
        } else {
            emailView.setError("");
        }

        CharSequence passwordcs = passEdit.getText();

        if (pass.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            passEdit.requestFocus();
            return;
        } else if (pass.length() < 5) {
            Toast.makeText(this, "Password must have 5 characters", Toast.LENGTH_SHORT).show();
            passEdit.requestFocus();
            return;
        } else if (!(Pattern.matches(p.pattern(), passwordcs))) {
            Toast.makeText(this, passError, Toast.LENGTH_SHORT).show();
            passEdit.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                       SweetAlertDialog pDialog = new SweetAlertDialog(
                                Login.this, SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText("Loading");
                        pDialog.show();
                        pDialog.setCancelable(false);

                        new CountDownTimer(200 * 7, 200) {
                            public void onTick(long millisUntilFinished) {
                                // you can change the progress bar color by ProgressHelper every 800 millis                        i++;

                            }

                            @RequiresApi(api = Build.VERSION_CODES.S)
                            public void onFinish() {
                                i = -1;
                                pDialog.setTitleText("Login Success")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Intent intent = new Intent(getApplicationContext(), user_dashboard.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                                Intent.FLAG_ACTIVITY_NEW_TASK);

                                                        startActivity(intent);
                                                    }
                                                }, 0);

                                            }
                                        })
                                        .setContentText("Welcome To hiBreed!")
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            }
                        }.start();

                    }
                    else{
                            user.sendEmailVerification();
                       SweetAlertDialog pDialog = new SweetAlertDialog(
                                Login.this, SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText("Loading");
                        pDialog.show();
                        pDialog.setCancelable(false);

                        new CountDownTimer(200 * 7, 200) {
                            public void onTick(long millisUntilFinished) {
                                // you can change the progress bar color by ProgressHelper every 800 millis                        i++;

                            }


                            @RequiresApi(api = Build.VERSION_CODES.S)
                            public void onFinish() {
                                i = -1;

                                pDialog
                                        .setTitleText("Check your email to verify your account!")
                                        .setConfirmButton("Okay", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(getIntent());
                                                        finish();
                                                    }
                                                },0);
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                pDialog.findViewById(cn.pedant.SweetAlert.R.id.confirm_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCC33")));
                                pDialog.findViewById(cn.pedant.SweetAlert.R.id.confirm_button).setBackgroundResource(R.drawable.shapetwo);
                            }
                        }.start();
                    }
                    } else {

                    SweetAlertDialog pDialog = new SweetAlertDialog(
                            Login.this, SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Loading");
                    pDialog.show();
                    pDialog.setCancelable(false);
                        new CountDownTimer(200 * 7, 200) {
                            public void onTick(long millisUntilFinished) {
                                // you can change the progress bar color by ProgressHelper every 800 millis                        i++;

                            }

                            public void onFinish() {
                                i = -1;
                                pDialog.setTitleText("Account doesn't Exist")
                                        .setContentText("Choose cancel if you want to login again or go to Registration")
                                        .setConfirmText("Register")
                                        .setCancelText("Cancel")
                                        .setConfirmButton("Register", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        passEdit.getText().clear();
                                                        emailEdit.getText().clear();
                                                        startActivity(new Intent(Login.this, screen_popupdialog.class));
                                                        finish();
                                                    }
                                                } ,0);
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);


                            }
                        }.start();
                    emailView.setError("");
                    passView.setError("");

                    }

                }

        });

    }

    public void ClickMeLogin(View view) {
        // Variables
        checker();
    }


}
