package com.example.hi_breed.loginAndRegistration;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ScrollView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.Class_OwnerClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrationForOwner extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int i = -1;
    DatabaseReference reference;
    final Pattern p = Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");
    final Pattern phonePattern = Pattern.compile("^(09)\\d{9}");
   final Pattern noWhiteSpace = Pattern.compile("[^\\\\S]+[a-z,A-Z,0-9]+");
   final String passError = "Password must contains at least 1 special character[ex.@#$%!^&+],at least 5 characters and no white spaces ";
    Button submit;
    TextInputLayout reg_first,reg_last,reg_middle,reg_contact,reg_email,reg_address,reg_zip,reg_password;

    TextInputEditText reg_firstEdit,reg_lastEdit,reg_middleEdit,reg_contactEdit,reg_emailEdit,reg_addressEdit,reg_zipEdit,reg_passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_for_owner);
        mAuth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.submitButton);
        reg_first = findViewById(R.id.firstName_Input);
        reg_middle = findViewById(R.id.reg_middleName);
        reg_last = findViewById(R.id.reg_lastName);
        reg_contact = findViewById(R.id.reg_contact);
        reg_address = findViewById(R.id.reg_address);
        reg_zip = findViewById(R.id.reg_zip);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);

        //Edit Layout
        reg_firstEdit = findViewById(R.id.firstName_Edit);
        reg_middleEdit = findViewById(R.id.reg_middleNameEdit);
        reg_lastEdit = findViewById(R.id.reg_lastNameEdit);
        reg_contactEdit = findViewById(R.id.reg_contactEdit);
        reg_addressEdit = findViewById(R.id.reg_addressEdit);
        reg_zipEdit = findViewById(R.id.reg_zipEdit);
        reg_emailEdit = findViewById(R.id.reg_emailEdit);
        reg_passwordEdit = findViewById(R.id.reg_passwordEdit);

//first name
        reg_firstEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() >= 2) {
                    reg_first.setError("");
                    reg_first.setHelperText("First name is valid");
                    reg_first.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                } else {
                    reg_first.setError("Name must have 2 characters");
                    reg_first.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//no Condition for Middle name
//last name
        reg_lastEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() >= 2) {
                    reg_last.setError("");
                    reg_last.setHelperText("Last name is valid");
                    reg_last.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                } else {
                    reg_last.setError("Last name must have 2 letters");
                    reg_last.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//contact number
        reg_contactEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() == 11) {

                    Matcher m = phonePattern.matcher(s);
                    if(m.matches()) {
                        reg_contact.setHelperText("Contact is valid");
                        reg_contact.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                        reg_contact.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                        reg_contact.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));

                    }else{
                        reg_contact.setError("Enter a valid Phone number ex. 09106851425");
                        reg_contact.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));

                    }
                }
                else {
                    reg_contact.setError("Contact must 11 numbers");
                    reg_contact.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));

                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//Email
        reg_emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s != null) {

                    if (Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), s)) {
                        reg_email.setHelperText("Email is valid");
                        reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                        reg_email.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));

                    } else {
                        reg_email.setError("Enter a valid email ex. example@gmail.com");
                        reg_email.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));
                    }
                }
                else{
                    reg_email.setError("Please enter a valid Address");
                    reg_email.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//Address
        reg_addressEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null) {
                    if(s.length()>2){
                        reg_address.setError("");
                        reg_address.setHelperText("Address is valid");
                        reg_address.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                        reg_address.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    }
                    else{
                        reg_address.setError("Please input your correct address");
                        reg_address.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));
                    }


                } else {
                        reg_address.setHelperText("");
                        reg_address.setError("Please input your address");
                    reg_address.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//Zip
        reg_zipEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() == 4) {
                    reg_zip.setHelperText("Zipcode is valid");
                    reg_zip.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                } else {
                    reg_zip.setError("Zipcode must 4 numbers");
                    reg_zip.setBoxStrokeErrorColor(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//Password
        reg_passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null){

                    Matcher m = p.matcher(s);
                    if(m.matches()){
                        reg_password.setError("");
                        reg_password.setHelperText("Valid password!");
                        reg_password.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    }
                    else{
                        reg_password.setError(passError);
                        reg_password.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));

                    }
                }
                else{
                    reg_password.setError("Please enter a password");
                    reg_password.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF0026")));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }//end of onCreate()



//button Submit Event listener


    private void registerUser() {

        String first = Objects.requireNonNull(reg_firstEdit.getText()).toString().trim();
        String middle = Objects.requireNonNull(reg_middleEdit.getText()).toString().trim();
        String last = Objects.requireNonNull(reg_lastEdit.getText()).toString().trim();
        String contact = Objects.requireNonNull(reg_contactEdit.getText()).toString().trim();
        String email = Objects.requireNonNull(reg_emailEdit.getText()).toString().trim();
        String address = Objects.requireNonNull(reg_addressEdit.getText()).toString().trim();
        String zip = Objects.requireNonNull(reg_zipEdit.getText()).toString().trim();
        String password = Objects.requireNonNull(reg_passwordEdit.getText()).toString().trim();

//first condition
        if (first.isEmpty()) {
            reg_first.setError("Please input your firstname");
            reg_firstEdit.requestFocus();

            return;
        } else if (first.length() <2) {
            reg_first.setError("Firstname must contains 2 characters");
            reg_firstEdit.requestFocus();

            return;

        } else {

            reg_first.setError("");
        }//end of firstname
//last condition
        if (last.isEmpty()) {
            reg_last.setError("Please input your lastname");
            reg_lastEdit.requestFocus();

            return;
        } else if (last.length() <2) {
            reg_last.setError("Lastname must contains 2 characters");
            reg_lastEdit.requestFocus();

            return;
        } else {

            reg_last.setError("");
        }//end of lastname
//contact condition
        CharSequence phonecs = reg_contactEdit.getText();
        if (contact.isEmpty()) {
            reg_contact.setError("Please input contact!");

            return;
        } else if (contact.length() != 11) {
            reg_contact.setError("Contact must 11 numbers");
            reg_contactEdit.requestFocus();

            return;
        } else if (!Pattern.matches(phonePattern.pattern(), phonecs)) {
            reg_contact.setError("Input valid number ex.09106851425");
            reg_contactEdit.requestFocus();

            return;
        } else {
            reg_contact.setError("");

        }//end of contact

//address
        if(address.isEmpty()){
            reg_address.setError("Please input your current address");
            reg_addressEdit.requestFocus();

            return;
        }
        else{
            reg_address.setError("");

        }//end of address
//zip
        if(zip.isEmpty())
        {
            reg_zip.setError("Please input your zip code");
            reg_zipEdit.requestFocus();

            return;
        }
        else if (zip.length()!=4)
        {
            reg_zip.setError("Zip code must 4 characters");
            reg_zipEdit.requestFocus();

            return;
        }
        else{
            reg_zip.setError("");

        }//end of zip




//password
        if(!(first.isEmpty()) && !(last.isEmpty()) && !(contact.isEmpty()) && !(address.isEmpty()) && !(zip.isEmpty())){
            //set the input layout to invisible
            reg_first.setVisibility(View.INVISIBLE);
            reg_middle.setVisibility(View.INVISIBLE);
            reg_last.setVisibility(View.INVISIBLE);
            reg_contact.setVisibility(View.INVISIBLE);
            reg_address.setVisibility(View.INVISIBLE);
            reg_zip.setVisibility(View.INVISIBLE);

            //Edit layout to invisible
            reg_firstEdit.setVisibility(View.INVISIBLE);
            reg_middleEdit.setVisibility(View.INVISIBLE);
            reg_lastEdit.setVisibility(View.INVISIBLE);
            reg_contactEdit.setVisibility(View.INVISIBLE);
            reg_addressEdit.setVisibility(View.INVISIBLE);
            reg_zipEdit.setVisibility(View.INVISIBLE);

            //set visible

            ScrollView sv = (ScrollView)findViewById(R.id.scrl);
            sv.scrollTo(-1500, sv.getTop());
            setMargins(reg_first, 0,0,0,0);
            setMargins(findViewById(R.id.submitButton), 0,-1150,0,0);
            reg_password.setVisibility(View.VISIBLE);
            reg_email.setVisibility(View.VISIBLE);
            reg_passwordEdit.setVisibility(View.VISIBLE);
            reg_emailEdit.setVisibility(View.VISIBLE);
            reg_email.setHintEnabled(false);
            reg_password.setHintEnabled(false);
//email
            CharSequence emailcs = reg_emailEdit.getText();
            if (email.isEmpty()) {
                reg_email.setError("Please input your email");
                reg_emailEdit.requestFocus();

                return;
            } else if (!(Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailcs))) {
                reg_email.setError("Please input a valid email ex. example@gmail.com");
                reg_emailEdit.requestFocus();
                return;
            }
            else{
                reg_email.setError("");

            }
//end of email

//Password
            CharSequence passwordcs = reg_passwordEdit.getText();

            if(password.isEmpty()){
                reg_password.setError("Please enter your password");
                reg_passwordEdit.requestFocus();

                return;
            }
            else if(password.length()<5){
                reg_password.setError("Password must have 5 characters");
                reg_passwordEdit.requestFocus();

                return;
            }
            else if(!(Pattern.matches(p.pattern(),passwordcs))){
                reg_password.setError(passError);
                reg_passwordEdit.requestFocus();

                return;
            }
            else{
                reg_password.setError("");
            }
//end of password
        }
        else{
            return;
        }


        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();


                        if (isNewUser) {
                            mAuth.createUserWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Uri uri = Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.noimage);

                                                Class_OwnerClass ownerClass = new Class_OwnerClass(first,middle,last,contact,address,zip,email,password,uri.toString(),"Pet Owner.");

                                                    FirebaseDatabase.getInstance().getReference("Owner")
                                                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                                        .setValue(ownerClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(first+" "+
                                                                            middle+" "+last).build();
                                                                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(userProfileChangeRequest);

                                                                    final SweetAlertDialog pDialog = new SweetAlertDialog(
                                                                            RegistrationForOwner.this, SweetAlertDialog.PROGRESS_TYPE)
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
                                                                            reg_email.setError("");
                                                                            reg_password.setError("");
                                                                            pDialog.setTitleText("Account Registered!")
                                                                                    .setConfirmText("OK")
                                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                        @Override
                                                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                                            new Handler().postDelayed(new Runnable() {
                                                                                                @Override
                                                                                                public void run() {
                                                                                                    startActivity(new Intent(RegistrationForOwner.this, Login.class));
                                                                                                    finish();


                                                                                                }
                                                                                            },2000);

                                                                                        }
                                                                                    })

                                                                                    .setContentText("Welcome new Owner!")
                                                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                                                        }
                                                                    }.start();

                                                                    reg_emailEdit.getText().clear();

                                                                }
                                                                else{
                                                                    final SweetAlertDialog pDialog = new SweetAlertDialog(
                                                                            RegistrationForOwner.this, SweetAlertDialog.PROGRESS_TYPE)
                                                                            .setTitleText("Loading");
                                                                    pDialog.show();
                                                                    pDialog.setCancelable(false);
                                                                    new CountDownTimer(200 * 7, 200) {
                                                                        public void onTick(long millisUntilFinished) {
                                                                            // you can change the progress bar color by ProgressHelper every 800 millis                        i++;

                                                                        }

                                                                        public void onFinish() {
                                                                            i = -1;
                                                                            pDialog.setTitleText("Not Valid!")
                                                                                    .setConfirmText("OK")
                                                                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                                        }
                                                                    }.start();
                                                                }

                                                            }

                                                        });
                                            }
                                        }
                                    });

                        } else {
                            final SweetAlertDialog pDialog = new SweetAlertDialog(
                                    RegistrationForOwner.this, SweetAlertDialog.PROGRESS_TYPE)
                                    .setTitleText("Loading");
                            pDialog.show();
                            pDialog.setCancelable(false);
                            new CountDownTimer(200 * 7, 200) {
                                public void onTick(long millisUntilFinished) {
                                    // you can change the progress bar color by ProgressHelper every 800 millis                        i++;

                                }

                                public void onFinish() {
                                    i = -1;
                                    pDialog.setTitleText("Email is already registered!")
                                            .hideConfirmButton()
                                            .setCancelText("Okay")
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            }.start();

                            reg_email.setError("Email is already registered. Try another one!");
                            reg_emailEdit.getText().clear();
                            reg_passwordEdit.getText().clear();
                        }

                    }
                });





    }  //end of method registration()





    public void gotoLogin(View view) {
        startActivity(new Intent(RegistrationForOwner.this,Login.class));
        finish();
    }

    public void SubmitButton(View view) {

        registerUser();


    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
