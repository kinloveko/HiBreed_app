package com.example.hi_breed.userFile.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi_breed.classesFile.Class_BreederClass;
import com.example.hi_breed.classesFile.Class_Breeder_ShopName;
import com.example.hi_breed.classesFile.Class_OwnerClass;
import com.example.hi_breed.R;
import com.example.hi_breed.loginAndRegistration.Login;
import com.example.hi_breed.loginAndRegistration.RegistrationForBreeder;
import com.example.hi_breed.phoneAccess.phone_UncropperActivity;
import com.example.hi_breed.userFile.shop.shop_edit_profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;

public class user_profile_edit extends AppCompatActivity {

    /*
         updateButton.setText("Save");
                updateButton.setText("Edit");*/
    LinearLayout backLayout;
    private FirebaseAuth mAuth;
    private DocumentReference documentReference;
    private FirebaseFirestore firebaseFirestore;
    StorageReference fileRefOwner,fileRefBreeder;
    StorageReference fileRefBreederBackground,fileRefOwnerBackground;
    private FirebaseUser firebaseUser;
    Uri imgsUri,imgUriCover;
    ActivityResultLauncher<String> cropImage,cropImageCover;
    ImageView imageView,imageBackground;
    String userID;
    String role;
    String alertCaller;
    RelativeLayout reg_firstLayout,reg_lastLayout,reg_middleLayout,reg_contactLayout,reg_addressLayout,reg_zipLayout;
    TextView reg_first,reg_last,reg_middle,reg_contact,reg_address,reg_zip;
    TextView updateButton;
    String before_first,before_last,before_middle,before_contact,before_address,before_zip;
    final Pattern phonePattern = Pattern.compile("^(09)\\d{9}");



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_user_identity);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
            documentReference = firebaseFirestore.collection("User").document(userID);
            getUserInfo(documentReference);
        }

        //background cover
        fileRefOwnerBackground = FirebaseStorage.getInstance().getReference("Owner pictures/"+firebaseUser.getUid()+"/"+"Profile Background/");
        fileRefBreederBackground = FirebaseStorage.getInstance().getReference("Breeder pictures/"+firebaseUser.getUid()+"/"+"Profile Background/");
        //profile
        fileRefBreeder = FirebaseStorage.getInstance().getReference("Breeder pictures/"+userID+"/"+"Profile Picture/");
        fileRefOwner = FirebaseStorage.getInstance().getReference("Owner pictures/"+userID+"/"+"Profile Picture/");




        //Relative layout
        reg_firstLayout = findViewById(R.id.first_layout);
        reg_middleLayout = findViewById(R.id.middle_layout);
        reg_lastLayout = findViewById(R.id.last_layout);
        reg_contactLayout= findViewById(R.id.contact_layout);
        reg_addressLayout = findViewById(R.id.address_layout);
        reg_zipLayout = findViewById(R.id.zip_layout);
        //textview
        reg_first = findViewById(R.id.first_input);
        reg_middle = findViewById(R.id.middle_input);
        reg_last = findViewById(R.id.last_input);
        reg_contact= findViewById(R.id.contact_input);
        reg_address = findViewById(R.id.address_input);
        reg_zip = findViewById(R.id.zip_input);


        imageBackground = findViewById(R.id.imageBackground);
        imageView = findViewById(R.id.profileImage);
        updateButton = findViewById(R.id.updateButton);
        backLayout = findViewById(R.id.backLayout);

        //set to read only

        // on click for edit text
        reg_firstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCaller = "firstName";
                dialogForAll(alertCaller);
            }
        });
        reg_middleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCaller = "middleName";
                dialogForAll(alertCaller);
            }
        });
        reg_lastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCaller = "lastName";
                dialogForAll(alertCaller);
            }
        });
        reg_contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCaller = "contact";
                dialogForAll(alertCaller);
            }
        });
        reg_addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCaller = "address";
                dialogForAll(alertCaller);
            }
        });
        reg_zipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCaller = "zip";
                dialogForAll(alertCaller);
            }
        });

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_profile_edit.this.onBackPressed();
            }
        });


        //background image request
        cropImageCover = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(this.getApplicationContext(), phone_UncropperActivity.class);

            if(result!=null){
                intent.putExtra("SendingData", result.toString());
                startActivityForResult(intent,100);
            }
            else{
                Toast.makeText(this.getApplicationContext(),"No changes",Toast.LENGTH_SHORT).show();
            }
        });
        //profile image request
        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(user_profile_edit.this.getApplicationContext(), phone_UncropperActivity.class);

            if(result!=null){
                intent.putExtra("SendingData", result.toString());
               startActivityForResult(intent,99);

            }
            else{
                Toast.makeText(this.getApplicationContext(),"No changes",Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });
        imageBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermissionBackground();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInput();
            }
        });
    }



    //to get the value of the imageView
    String imageProf="";
    String imageCover="";
    private void getUserInfo(DocumentReference userID) {
        userID.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                     role = documentSnapshot.getString("role");
                    if(role.equals("Pet Breeder")){
                        Class_BreederClass breederProfile = documentSnapshot.toObject(Class_BreederClass.class);
                        if (breederProfile != null) {

                            before_first =breederProfile.getFirstName();
                            before_middle = breederProfile.getMiddleName();
                            before_last = breederProfile.getLastName();
                            before_contact = breederProfile.getContactNumber();
                            before_address = breederProfile.getAddress();
                            before_zip = breederProfile.getZipCode();

                            reg_first.setText(breederProfile.getFirstName());
                            reg_middle.setText(breederProfile.getMiddleName());
                            reg_last.setText(breederProfile.getLastName());
                            reg_contact.setText(breederProfile.getContactNumber());
                            reg_address.setText(breederProfile.getAddress());
                            reg_zip.setText(breederProfile.getZipCode());
                            if(breederProfile.getImage() == ""|| breederProfile.getImage() == null){
                                imageView.setImageResource(R.drawable.noimage);
                                imageProf = "";
                            }
                            else{
                                Picasso.get().load(breederProfile.getImage()).into(imageView);
                                //setting the variable to what imageView picture has.
                                imageProf = breederProfile.getImage();
                            }
                            if(breederProfile.getBackgroundImage() == ""|| breederProfile.getBackgroundImage() == null){
                                imageView.setImageResource(R.drawable.noimage);
                                imageCover = "";
                            }
                            else{
                                Picasso.get().load(breederProfile.getBackgroundImage()).into(imageView);
                                //setting the variable to what imageView picture has.
                                imageCover = breederProfile.getBackgroundImage();
                            }
                        }
                    }
                    else if(role.equals("Pet Owner")){
                        Class_OwnerClass ownerProfile = documentSnapshot.toObject(Class_OwnerClass.class);
                        if(ownerProfile != null) {
                            reg_first.setText(ownerProfile.getFirstName());
                            reg_middle.setText(ownerProfile.getMiddleName());
                            reg_last.setText(ownerProfile.getLastName());
                            reg_contact.setText(ownerProfile.getContactNumber());
                            reg_address.setText(ownerProfile.getAddress());
                            reg_zip.setText(ownerProfile.getZipCode());
                            if(ownerProfile.getImage() == ""|| ownerProfile.getImage() == null){
                                imageView.setImageResource(R.drawable.noimage);
                                imageProf = "";
                            }
                            else{
                                Picasso.get().load(ownerProfile.getImage()).into(imageView);
                                imageProf = ownerProfile.getImage();
                            }
                            if(ownerProfile.getBackgroundImage() == ""|| ownerProfile.getBackgroundImage() == null){
                                imageView.setImageResource(R.drawable.noimage);
                                imageCover = "";
                            }
                            else{
                                Picasso.get().load(ownerProfile.getBackgroundImage()).into(imageView);
                                //setting the variable to what imageView picture has.
                                imageCover = ownerProfile.getBackgroundImage();
                            }
                        }
                    }

                }
            }
        });
 }


    int count = 0;
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void dialogForAll(String alertCaller) {


        if(alertCaller.equals("firstName")){

            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("First name");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your Firstname");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_details_borders));
            //gif
            GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
            gif.setVisibility(View.GONE);
            //button layout
            LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
            buttonLayout.setVisibility(View.VISIBLE);
            //button
            MaterialButton cancel,okay;
            //cancel button
            cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
            cancel.setText("Cancel");
            //Okay button
            okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
            okay.setText("Save");
            okay.setBackgroundColor(Color.parseColor("#F6B75A"));
            okay.setTextColor(Color.WHITE);
            //EditText
            EditText editText = view2.findViewById(R.id.screen_custom_editText);
            editText.setHint("First name..");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            //valid
            ImageView valid = view2.findViewById(R.id.screen_custom_valid_icon);
            //clear text
            TextView clear = view2.findViewById(R.id.screen_custom_clearText);
            clear.setVisibility(View.GONE);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()!=0){
                        if(s.length() >= 2){
                            valid.setVisibility(View.VISIBLE);
                        }
                        else {
                            valid.setVisibility(View.GONE);
                        }
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getText().clear();
                            }
                        });
                    }
                    else{
                        valid.setVisibility(View.GONE);
                        clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            builder3.setView(view2);
            AlertDialog alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert3.dismiss();
                }
            });
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().equals("") || editText.getText().toString() == null){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. You made no entry! a First name was expected", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText.getText().toString().equals(before_first)){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. We expecting a different name, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    }
                    else if (editText.length()<2){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. Input must have 2 characters", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        reg_first.setText(editText.getText().toString());
                        Toast.makeText(user_profile_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        updateButton.setVisibility(View.VISIBLE);
                    }
                    if(count != 0){
                        alert3.dismiss();
                    }
                    else{

                    }
                }
            });

        }
        else if(alertCaller.equals("middleName")){
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("Middle name");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your middle name. If you don't have one leave it blank, and Click CANCEL.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_details_borders));
            //gif
            GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
            gif.setVisibility(View.GONE);
            //button layout
            LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
            buttonLayout.setVisibility(View.VISIBLE);
            //button
            MaterialButton cancel,okay;
            //cancel button
            cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
            cancel.setText("Cancel");
            //Okay button
            okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
            okay.setText("Save");
            okay.setBackgroundColor(Color.parseColor("#F6B75A"));
            okay.setTextColor(Color.WHITE);
            //EditText
            EditText editText = view2.findViewById(R.id.screen_custom_editText);
            editText.setHint("Middle name..");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);

            //clear text
            TextView clear = view2.findViewById(R.id.screen_custom_clearText);
            clear.setVisibility(View.GONE);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()!=0){
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getText().clear();
                            }
                        });
                    }
                    else{
                        clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            builder3.setView(view2);
            AlertDialog alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert3.dismiss();
                }
            });
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().equals("") || editText.getText().toString() == null){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. You made no entry! your Middle name was expected", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText.getText().toString().equals(before_middle)){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. We expecting a different name, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        reg_middle.setText(editText.getText().toString());
                        Toast.makeText(user_profile_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        updateButton.setVisibility(View.VISIBLE);
                    }
                    if(count != 0){
                        alert3.dismiss();
                    }
                    else{

                    }
                }
            });
        }
        else if(alertCaller.equals("lastName")){
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("Last name");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your last name, and click SAVE if you're done. Click CANCEL if you don't want to edit something.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_details_borders));
            //gif
            GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
            gif.setVisibility(View.GONE);
            //button layout
            LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
            buttonLayout.setVisibility(View.VISIBLE);
            //button
            MaterialButton cancel,okay;
            //cancel button
            cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
            cancel.setText("Cancel");
            //Okay button
            okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
            okay.setText("Save");
            okay.setBackgroundColor(Color.parseColor("#F6B75A"));
            okay.setTextColor(Color.WHITE);
            //EditText
            EditText editText = view2.findViewById(R.id.screen_custom_editText);
            editText.setHint("Last name..");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            //valid
            ImageView valid = view2.findViewById(R.id.screen_custom_valid_icon);
            //clear text
            TextView clear = view2.findViewById(R.id.screen_custom_clearText);
            clear.setVisibility(View.GONE);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()!=0){
                        if(s.length() >= 2){
                            valid.setVisibility(View.VISIBLE);
                        }
                        else{
                            valid.setVisibility(View.GONE);
                        }
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getText().clear();
                            }
                        });
                    }
                    else{
                        valid.setVisibility(View.GONE);
                        clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            builder3.setView(view2);
            AlertDialog alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert3.dismiss();
                }
            });
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().equals("") || editText.getText().toString() == null){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. You made no entry! your Last name was expected", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText.getText().toString().equals(before_last)){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. We expecting a different name, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    }
                    else if (editText.length()<2){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. Input must have 2 characters", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        reg_last.setText(editText.getText().toString());
                        Toast.makeText(user_profile_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        updateButton.setVisibility(View.VISIBLE);
                    }
                    if(count != 0){
                        alert3.dismiss();
                    }
                    else{

                    }
                }
            });
        }
        else if(alertCaller.equals("contact")){
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("Contact number");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your contact number, and click SAVE if you're done. Click CANCEL if you don't want to edit something.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_phone_borders));
            //gif
            GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
            gif.setVisibility(View.GONE);
            //button layout
            LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
            buttonLayout.setVisibility(View.VISIBLE);
            //button
            MaterialButton cancel,okay;
            //cancel button
            cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
            cancel.setText("Cancel");
            //Okay button
            okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
            okay.setText("Save");
            okay.setBackgroundColor(Color.parseColor("#F6B75A"));
            okay.setTextColor(Color.WHITE);
            //EditText
            EditText editText = view2.findViewById(R.id.screen_custom_editText);
            editText.setHint("Contact number..");
            int maxLength = 11;
            editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            //valid image
            ImageView valid = view2.findViewById(R.id.screen_custom_valid_icon);
           //error textview
            TextView errorText = view2.findViewById(R.id.screen_custom_error_TextView);
            //clear text
            TextView clear = view2.findViewById(R.id.screen_custom_clearText);
            clear.setVisibility(View.GONE);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int c) {
                    if(s.length()!=0) {

                            Matcher m = phonePattern.matcher(s);
                            if (m.matches()) {
                                errorText.setVisibility(View.GONE);
                                valid.setVisibility(View.VISIBLE);
                                count = 1;
                            } else {
                                count = 0;
                                errorText.setVisibility(View.VISIBLE);
                                valid.setVisibility(View.GONE);
                                errorText.setText("Enter a valid Phone number ex. 09106851425");
                            }
                            clear.setVisibility(View.VISIBLE);
                            clear.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    editText.getText().clear();
                                }
                            });
                        }
                    else{
                        valid.setVisibility(View.GONE);
                        clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            builder3.setView(view2);
            AlertDialog alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert3.dismiss();
                }
            });
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().equals("") || editText.getText().toString() == null){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. You made no entry! your contact number was expected", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText.getText().toString().equals(before_contact)){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. We expecting a different name, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText.length() != 11){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. We expecting 11 numbers.", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        reg_contact.setText(editText.getText().toString());
                        Toast.makeText(user_profile_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        updateButton.setVisibility(View.VISIBLE);

                    }
                    if(count != 0){
                        alert3.dismiss();
                    }
                    else{

                    }
                }
            });
        }
        else if(alertCaller.equals("address")){
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("User Address");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your Address, and click SAVE if you're done. Click CANCEL if you don't want to edit something.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_address_borders));
            //gif
            GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
            gif.setVisibility(View.GONE);
            //button layout
            LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
            buttonLayout.setVisibility(View.VISIBLE);
            //button
            MaterialButton cancel,okay;
            //cancel button
            cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
            cancel.setText("Cancel");
            //Okay button
            okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
            okay.setText("Save");
            okay.setBackgroundColor(Color.parseColor("#F6B75A"));
            okay.setTextColor(Color.WHITE);
            //EditText
            EditText editText = view2.findViewById(R.id.screen_custom_editText);
            editText.setHint("Address..");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);

            //clear text
            TextView clear = view2.findViewById(R.id.screen_custom_clearText);
            clear.setVisibility(View.GONE);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()!=0){
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getText().clear();
                            }
                        });
                    }
                    else{
                        clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            builder3.setView(view2);
            AlertDialog alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert3.dismiss();
                }
            });
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().equals("") || editText.getText().toString() == null){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. You made no entry! your current Address was expected", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText.getText().toString().equals(before_address)){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. We expecting a different info, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        reg_address.setText(editText.getText().toString());
                        Toast.makeText(user_profile_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        updateButton.setVisibility(View.VISIBLE);
                    }
                    if(count != 0){
                        alert3.dismiss();
                    }
                    else{

                    }
                }
            });
        }
        else if(alertCaller.equals("zip")){
            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
            View view2 = View.inflate(this,R.layout.screen_custom_alert,null);
            builder3.setCancelable(false);
            //title
            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
            title2.setText("Zip Code / Postal Code");
            //message
            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
            message2.setText("Please input your Zip Code/ Postal Code, and click SAVE if you're done. Click CANCEL if you don't want to edit something.");
            //loading
            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
            loadingText.setVisibility(View.GONE);
            //top image
            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
            imageViewCompat2.setVisibility(View.VISIBLE);
            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.dialog_zip_borders));
            //gif
            GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
            gif.setVisibility(View.GONE);
            //button layout
            LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
            buttonLayout.setVisibility(View.VISIBLE);
            //button
            MaterialButton cancel,okay;
            //cancel button
            cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
            cancel.setText("Cancel");
            //Okay button
            okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
            okay.setText("Save");
            okay.setBackgroundColor(Color.parseColor("#F6B75A"));
            okay.setTextColor(Color.WHITE);
            //EditText
            EditText editText = view2.findViewById(R.id.screen_custom_editText);
            editText.setHint("Zip code/Postal code..");
            int maxLength = 4;
            editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            //valid
            ImageView valid = view2.findViewById(R.id.screen_custom_valid_icon);
            //clear text
            TextView clear = view2.findViewById(R.id.screen_custom_clearText);
            clear.setVisibility(View.GONE);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int c) {
                    if(s.length()!=0){
                        if(s.length()!=4){
                            count = 0;
                        }
                        else{
                            count = 1;
                            valid.setVisibility(View.VISIBLE);
                        }
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.getText().clear();
                            }
                        });
                    }
                    else{
                        valid.setVisibility(View.VISIBLE);
                        clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            builder3.setView(view2);
            AlertDialog alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert3.dismiss();
                }
            });
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().equals("") || editText.getText().toString() == null){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. You made no entry! a Zip code/Postal Code was expected", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText.getText().toString().equals(before_zip)){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. We expecting a different info, but you entered the same value.", Toast.LENGTH_SHORT).show();
                    }
                    else if(editText.length() != 4){
                        count = 0;
                        Toast.makeText(user_profile_edit.this, "Oops. Input must have 4 numbers.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        reg_zip.setText(editText.getText().toString());
                        Toast.makeText(user_profile_edit.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        count = 1;
                        updateButton.setVisibility(View.VISIBLE);
                    }
                    if(count != 0){
                        alert3.dismiss();
                    }
                    else{

                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Cannot open a dialog", Toast.LENGTH_SHORT).show();
        }
        this.alertCaller = "";

    }

    private void imagePermissionBackground() {
        Dexter.withContext(user_profile_edit.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        cropImageCover.launch("image/*");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(user_profile_edit.this, "Permission Denied",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    private void imagePermission(){

        Dexter.withContext(user_profile_edit.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        cropImage.launch("image/*");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(user_profile_edit.this, "Permission Denied",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    String resultsForCover="";
    String results="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==99 && resultCode == 102){
                results = data.getStringExtra("CROPS");
                imgsUri= data.getData();
                if(results!=null){
                    imgsUri = Uri.parse(results);
                }
                imageView.setImageURI(imgsUri);
            }
        if (requestCode == 100 && resultCode == 102){
            resultsForCover = data.getStringExtra("CROPS");
            imgUriCover = data.getData();
            if (resultsForCover != null) {
                imgUriCover = Uri.parse(resultsForCover);
            }
            imageBackground.setImageURI(imgUriCover);
        }
    }

    private void uploadProfileImage(){
        imgsUri = Uri.parse(results);
        imgUriCover  = Uri.parse(resultsForCover);
        //for breeder
        if(role.equals("Pet Breeder")){
            StorageReference imageName = fileRefBreeder.child(imgsUri.getLastPathSegment());
            StorageReference imageCoverName = fileRefBreederBackground.child(imgUriCover.getLastPathSegment());

            imageCoverName.putFile(imgUriCover).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             imageCoverName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                        imageCover = uri.toString();
                                     Map<String, Object> data = new HashMap<>();
                                     data.put("backgroundImage", uri.toString());
                                     documentReference.set(data,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    DocumentReference ref=   FirebaseFirestore.getInstance()
                                                            .collection("Shop")
                                                            .document(firebaseUser.getUid());
                                                    ref.set(data,SetOptions.merge());
                                                    uploadToFirebase();
                                                }
                                         }
                                     });
                                 }
                             });
                }
            });

            imageName.putFile(imgsUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageProf = uri.toString();
                            //for user profile
                            Map<String, Object> data = new HashMap<>();
                            data.put("image", uri.toString());
                            //for shop profile
                            Map<String, Object> shopImage = new HashMap<>();
                            data.put("profImage", uri.toString());
                            documentReference.set(data,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                     DocumentReference ref=   FirebaseFirestore.getInstance()
                                                        .collection("Shop")
                                                                .document(firebaseUser.getUid());
                                        ref.set(shopImage,SetOptions.merge());
                                        uploadToFirebase();
                                    }
                                    else{
                                        Toast.makeText(user_profile_edit.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            //Owner
            StorageReference imageNameOwner = fileRefOwner.child(imgsUri.getLastPathSegment());
            StorageReference imageCoverOwner = fileRefOwnerBackground.child(imgUriCover.getLastPathSegment());
            imageCoverOwner.putFile(imgUriCover).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageCoverOwner.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageCover = uri.toString();
                            Map<String, Object> data = new HashMap<>();
                            data.put("backgroundImage", uri.toString());
                            documentReference.set(data,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        uploadToFirebase();
                                    }
                                    else{
                                        Toast.makeText(user_profile_edit.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
            imageNameOwner.putFile(imgsUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageNameOwner.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageProf = uri.toString();
                            Map<String, Object> data = new HashMap<>();
                            data.put("image", uri.toString());
                            documentReference.set(data,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                       uploadToFirebase();
                                    }
                                    else{
                                        Toast.makeText(user_profile_edit.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }

    }

    private void checkInput(){

        String first = Objects.requireNonNull(reg_first.getText()).toString().trim();
        String last = Objects.requireNonNull(reg_last.getText()).toString().trim();
        String contact = Objects.requireNonNull(reg_contact.getText()).toString().trim();
        String address = Objects.requireNonNull(reg_address.getText()).toString().trim();
        String zip = Objects.requireNonNull(reg_zip.getText()).toString().trim();
//first condition
        if (first.isEmpty()) {
            reg_first.setError("Please input your firstname");


            return;
        } else if (first.length() <2) {
            reg_first.setError("Firstname must contains 2 characters");


            return;

        } else {

            reg_first.setError("");
        }//end of firstname
//last condition
        if (last.isEmpty()) {
            reg_last.setError("Please input your lastname");


            return;
        } else if (last.length() <2) {
            reg_last.setError("Lastname must contains 2 characters");


            return;
        } else {

            reg_last.setError("");
        }//end of lastname

//contact condition
        CharSequence phonecs = reg_contact.getText();
        if (contact.isEmpty()) {
            reg_contact.setError("Please input contact!");

            return;
        } else if (contact.length() != 11) {
            reg_contact.setError("Contact must 11 numbers");


            return;
        } else if (!Pattern.matches(phonePattern.pattern(), phonecs)) {
            reg_contact.setError("Input valid number ex.09106851425");


            return;
        } else {
            reg_contact.setError("");

        }//end of contact

//address
        if(address.isEmpty()){
            reg_address.setError("Please input your current address");


            return;
        }
        else{
            reg_address.setError("");

        }//end of address
//zip
        if(zip.isEmpty())
        {
            reg_zip.setError("Please input your zip code");


            return;
        }
        else if (zip.length()!=4)
        {
            reg_zip.setError("Zip code must 4 characters");

            return;
        }
        else{
            reg_zip.setError("");

        }//end of zip

        if(results == "" || resultsForCover == ""){

        }
        else{
            uploadProfileImage();
        }

        if(first!=null  && last!=null && contact!=null && address!=null && zip!=null) {
            uploadToFirebase();
        }
    }

    private void uploadToFirebase(){

        String first = Objects.requireNonNull(reg_first.getText()).toString().trim();
        String middle = Objects.requireNonNull(reg_middle.getText()).toString().trim();
        String last = Objects.requireNonNull(reg_last.getText()).toString().trim();
        String contact = Objects.requireNonNull(reg_contact.getText()).toString().trim();
        String address = Objects.requireNonNull(reg_address.getText()).toString().trim();
        String zip = Objects.requireNonNull(reg_zip.getText()).toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating your info");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();

             if(role.equals("Pet Owner")){

                 Map<String, Object> ownerClass = new HashMap<>();
                        ownerClass.put("firstName",first);
                        ownerClass.put("middleName",middle);
                        ownerClass.put("lastName",last);
                        ownerClass.put("contactNumber",contact);
                        ownerClass.put("address",address);
                        ownerClass.put("zipCode",zip);

                   FirebaseFirestore.getInstance().collection("User")
                        .document(firebaseUser.getUid()).set(ownerClass,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                                 public void onComplete(@NonNull Task<Void> task)
                                    {
                                         if(task.isSuccessful()){
                                                     Toast.makeText(user_profile_edit.this, "Update was successfully saved", Toast.LENGTH_SHORT).show();
                                         }
                                         else{
                                                     Toast.makeText(user_profile_edit.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                                         }
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                            }
                                        },2000);
                                    }
                });
             }
             else
             {
                 Map<String, Object> breederClass = new HashMap<>();
                 breederClass.put("firstName",first);
                 breederClass.put("middleName",middle);
                 breederClass.put("lastName",last);
                 breederClass.put("contactNumber",contact);
                 breederClass.put("address",address);
                 breederClass.put("zipCode",zip);

                 FirebaseFirestore.getInstance().collection("User")
                         .document(firebaseUser.getUid()).set(breederClass,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task)
                             {
                                 if(task.isSuccessful()){
                                     Toast.makeText(user_profile_edit.this, "Update was successfully saved", Toast.LENGTH_SHORT).show();
                                 }
                                 else{
                                     Toast.makeText(user_profile_edit.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                                 }
                                 new Handler().postDelayed(new Runnable() {
                                     @Override
                                     public void run() {
                                         progressDialog.dismiss();
                                     }
                                 },2000);
                             }
                         });
             }

        }
   }



