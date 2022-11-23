package com.example.hi_breed.loginAndRegistration;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;

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
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.Class_OwnerClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.droidsonroids.gif.GifImageView;

public class RegistrationForOwner extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

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
        firebaseUser =mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

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

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() >= 2) {
                    reg_first.setError("");
                    reg_first.setHelperText("First name is valid");
                    reg_first.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                } else {
                    reg_first.setHelperText("Name must have 2 characters");
                    reg_first.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));

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
                    reg_last.setHelperText("Last name must have 2 letters");
                    reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
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

                    }else{
                        reg_contact.setHelperText("Enter a valid Phone number ex. 09106851425");
                        reg_contact.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                }
                else {
                    reg_contact.setHelperText("Contact must 11 numbers");
                    reg_contact.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));

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

                    } else {
                        reg_email.setHelperText("Enter a valid email ex. example@gmail.com");
                        reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                }
                else{
                    reg_email.setHelperText("Please enter a valid Address");
                    reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
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
                        reg_address.setHelperText("Please input your correct address");
                        reg_address.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }


                } else {
                        reg_address.setHelperText("Please input your address");
                    reg_address.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
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

                    reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                } else {
                    reg_zip.setHelperText("Zipcode must 4 numbers");
                    reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
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
                        reg_password.setHelperText("Valid password!");
                        reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                    }
                    else{
                        reg_password.setHelperText(passError);
                        reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    }
                }
                else{
                    reg_password.setHelperText("Please enter a password");
                    reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
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
            reg_first.setHelperText("First name must have 2 characters");
            reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_firstEdit.requestFocus();

            return;
        }
        else if (first.length() <2) {
            reg_first.setHelperText("Name must have 2 characters");
            reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_firstEdit.requestFocus();

            return;

        } //end of firstname
//last condition
        if (last.isEmpty()) {
            reg_last.setHelperText("Please input your lastname");
            reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_lastEdit.requestFocus();

            return;
        }
        else if (last.length() <2) {
            reg_last.setHelperText("Last name must have 2 letters");
            reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_lastEdit.requestFocus();

            return;
        }//end of lastname
//contact condition
        CharSequence phonecs = reg_contactEdit.getText();
        if (contact.isEmpty()) {

            reg_contact.setHelperText("Please input contact!");
            reg_contact.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            return;
        }
        else if (contact.length() != 11) {
            reg_contact.setHelperText("Number must have 11 numbers");
            reg_contact.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_contactEdit.requestFocus();

            return;
        }
        else if (!Pattern.matches(phonePattern.pattern(), phonecs)) {
            reg_contact.setHelperText("Enter a valid Phone number ex. 09106851425");
            reg_contact.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_contactEdit.requestFocus();

            return;
        }//end of contact

//address
        if(address.isEmpty()){
            reg_address.setHelperText("Please input your correct address");
            reg_address.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_addressEdit.requestFocus();

            return;
        }
       //end of address
//zip
        if(zip.isEmpty())
        {
            reg_zip.setHelperText("Please input your zip code");
            reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_zipEdit.requestFocus();

            return;
        }
        else if (zip.length()!=4)
        {
            reg_zip.setHelperText("Zipcode must 4 numbers");
            reg_zip.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_zipEdit.requestFocus();

            return;
        }
        //end of zip




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
            setMargins(findViewById(R.id.submitButton), 0,-1100,0,0);
            reg_password.setVisibility(View.VISIBLE);
            reg_email.setVisibility(View.VISIBLE);
            reg_passwordEdit.setVisibility(View.VISIBLE);
            reg_emailEdit.setVisibility(View.VISIBLE);
            reg_email.setHintEnabled(false);
            reg_password.setHintEnabled(false);
//email
            CharSequence emailcs = reg_emailEdit.getText();
            if (email.isEmpty()) {
                reg_email.setHelperText("Please input your email address");
                reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_emailEdit.requestFocus();

                return;
            } else if (!(Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailcs))) {
                reg_email.setHelperText("Enter a valid email ex. example@gmail.com");
                reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_emailEdit.requestFocus();
                return;
            }

//end of email

//Password
            CharSequence passwordcs = reg_passwordEdit.getText();

            if(password.isEmpty()){
                reg_password.setHelperText("Please input your password");
                reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_passwordEdit.requestFocus();

                return;
            }
            else if(password.length()<5){
                reg_password.setHelperText("Password must have 5 characters");
                reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_passwordEdit.requestFocus();

                return;
            }
            else if(!(Pattern.matches(p.pattern(),passwordcs))){
                reg_password.setHelperText(passError);
                reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_passwordEdit.requestFocus();

                return;
            }

//end of password
        }
        else{
            return;
        }


        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();


                        if (isNewUser) {
                            mAuth.createUserWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationForOwner.this);
                                                View view = View.inflate(RegistrationForOwner.this,R.layout.screen_custom_alert,null);
                                                TextView title = view.findViewById(R.id.screen_custom_alert_title);
                                                builder.setCancelable(false);
                                                AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                                                imageViewCompat.setImageURI(Uri.parse(String.valueOf(R.drawable.icon_check_grey)));
                                                title.setVisibility(View.GONE);
                                                TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                                message.setVisibility(View.GONE);
                                                builder.setView(view);
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                //set a default image to save in firebase
                                                Uri uri = Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.noimage);
                                                Uri uriBackground = Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.nobackground);
                                                //object
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                Class_OwnerClass ownerClass = new Class_OwnerClass(first,middle,last,contact,address,zip,email,password,uri.toString(),uriBackground.toString(),"Pet Owner");
                                                //Reference in fire store
                                                DocumentReference documentReference= firestore.collection("User").document(Objects.requireNonNull(user).getUid());

                                                    documentReference.set(ownerClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @SuppressLint("SetTextI18n")
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    alert.dismiss();
                                                                }
                                                            },2000);

                                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(RegistrationForOwner.this);
                                                            builder2.setCancelable(false);
                                                            View view = View.inflate(RegistrationForOwner.this,R.layout.screen_custom_alert,null);
                                                            //title
                                                            TextView title = view.findViewById(R.id.screen_custom_alert_title);
                                                            //loading text
                                                            TextView loadingText = view.findViewById(R.id.screen_custom_alert_loadingText);
                                                            loadingText.setVisibility(View.GONE);
                                                            //gif
                                                            GifImageView gif = view.findViewById(R.id.screen_custom_alert_gif);
                                                            gif.setVisibility(View.GONE);
                                                            //header image
                                                            AppCompatImageView imageViewCompat = view.findViewById(R.id.appCompatImageView);
                                                            imageViewCompat.setVisibility(View.VISIBLE);
                                                            imageViewCompat.setImageDrawable(getDrawable(R.drawable.screen_alert_image_valid_borders));
                                                            //message
                                                            TextView message = view.findViewById(R.id.screen_custom_alert_message);
                                                            title.setText("Account Registered");
                                                            message.setText("Welcome to hiBreed application. You can now search for your desired pet to adopt.");
                                                            //button
                                                            LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                                                            buttonLayout.setVisibility(View.VISIBLE);
                                                            //button cancel,okay
                                                            MaterialButton cancel,okay;
                                                            cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                                                            okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                                                            cancel.setVisibility(View.GONE);

                                                            builder2.setView(view);
                                                            AlertDialog alert2 = builder2.create();
                                                            alert2.show();
                                                            alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            okay.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                       alert2.dismiss();
                                                                    reg_email.setError("");
                                                                    reg_password.setError("");
                                                                    reg_emailEdit.getText().clear();
                                                                    startActivity(new Intent(RegistrationForOwner.this, Login.class));
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            alert.dismiss();
                                                            Toast.makeText(RegistrationForOwner.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            }
                                        }
                                    });

                        } else {

                            AlertDialog.Builder builder3 = new AlertDialog.Builder(RegistrationForOwner.this);
                            View view = View.inflate(RegistrationForOwner.this,R.layout.screen_custom_alert,null);
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
                            title.setText("Email address already exist!");
                            message.setText("Please use another email address.");
                            LinearLayout buttonLayout = view.findViewById(R.id.screen_custom_alert_buttonLayout);
                            buttonLayout.setVisibility(View.VISIBLE);
                            MaterialButton cancel,okay;
                            cancel = view.findViewById(R.id.screen_custom_dialog_btn_cancel);
                            cancel.setVisibility(View.GONE);
                            okay = view.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                            okay.setText("Okay");
                            okay.setBackgroundColor(Color.parseColor("#999999"));
                            okay.setTextColor(Color.WHITE);
                            builder3.setView(view);
                            AlertDialog alert3 = builder3.create();
                            alert3.show();
                            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            okay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert3.dismiss();

                                }
                            });

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
