package com.example.hi_breed.loginAndRegistration;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi_breed.R;
import com.example.hi_breed.adapter.registrationRecyclerAdapter;
import com.example.hi_breed.classesFile.Class_BreederClass;
import com.example.hi_breed.classesFile.Class_Breeder_ShopName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrationForBreeder extends AppCompatActivity implements registrationRecyclerAdapter.CountOfImagesWhenRemoved, registrationRecyclerAdapter.itemClickListener {
//pattern
    final Pattern p = Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");
    final Pattern phonePattern = Pattern.compile("^(09)\\d{9}");
    final String passError = "Password must contains at least 1 special character[ex.@#$%!^&+],at least 5 characters and no white spaces ";
    private static final int Read_Permission = 101;
    private static final int PICK_IMAGE = 1;
    private int i = -1;
    private int countOfImages;


    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    //variable declaration
    RecyclerView recyclerView;
    registrationRecyclerAdapter adapter;

    ArrayList <Uri> uri = new ArrayList<>();
    Uri imageUri;
    ActivityResultLauncher<Intent> activityResultLauncher;

    CardView dropImageCardView;
    ImageView dropImageView;
    Button submit;
    TextInputLayout reg_first,reg_last,reg_middle,reg_contact,reg_email,reg_address,reg_zip,reg_password;
    TextInputEditText reg_firstEdit,reg_lastEdit,reg_middleEdit,reg_contactEdit,reg_emailEdit,reg_addressEdit,reg_zipEdit,reg_passwordEdit;
    TextView dropImageTextVIew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_for_breeder);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.breederTheme));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.semigreen));

        //for recycler view
            recyclerView = findViewById(R.id.recyclerView_gallery_images);
        //for adapter
            adapter = new registrationRecyclerAdapter(uri,getApplicationContext(),this, this);
            recyclerView.setLayoutManager(new GridLayoutManager(RegistrationForBreeder.this,4));
            recyclerView.setAdapter(adapter);

// variable initialized
    //TextView
        dropImageTextVIew = findViewById(R.id.dropImageTextView);
    //cardView
        dropImageCardView = findViewById(R.id.dropImageCardView);
    //ImageView
        dropImageView = findViewById(R.id.dropImageView);
    //Firebase authentication
        mAuth = FirebaseAuth.getInstance();
    //InputText
        submit = findViewById(R.id.submitButton);
        reg_first = findViewById(R.id.firstName_Input);
        reg_middle = findViewById(R.id.reg_middleName);
        reg_last = findViewById(R.id.reg_lastName);
        reg_contact = findViewById(R.id.reg_contact);
        reg_address = findViewById(R.id.reg_address);
        reg_zip = findViewById(R.id.reg_zip);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);

    //EditText
        reg_firstEdit = findViewById(R.id.firstName_Edit);
        reg_middleEdit = findViewById(R.id.reg_middleNameEdit);
        reg_lastEdit = findViewById(R.id.reg_lastNameEdit);
        reg_contactEdit = findViewById(R.id.reg_contactEdit);
        reg_addressEdit = findViewById(R.id.reg_addressEdit);
        reg_zipEdit = findViewById(R.id.reg_zipEdit);
        reg_emailEdit = findViewById(R.id.reg_emailEdit);
        reg_passwordEdit = findViewById(R.id.reg_passwordEdit);
//end of variable initialized

//TextChangedListeners

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
                    if(m.matches()) {
                        if (s.length() >= 4) {
                            reg_password.setError("");
                            reg_password.setHelperText("Valid password!");
                            reg_password.setBoxStrokeColorStateList(android.content.res.ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                        }
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
//End of TextChangedListeners


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //Refresh this code
                        if (result.getResultCode() == RESULT_OK && null != result.getData()) {
                            if (result.getData().getClipData() != null) {

                                //this part is for getting the multiple images
                                countOfImages = result.getData().getClipData().getItemCount();
                                for (int i = 0 ; i < countOfImages; i++) {
                                    //limiting the no of images picked up from gallery
                                    if (uri.size() < 5) {
                                        imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                        uri.add(imageUri);
                                    } else {
                                        Toast.makeText(RegistrationForBreeder.this, "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //then notify the adapter
                                adapter.notifyDataSetChanged();
                                Toast.makeText(RegistrationForBreeder.this, uri.size() + ":selected", Toast.LENGTH_SHORT).show();

                            }else{
                                if(uri.size() <5){
                                    //this is for to get the single images
                                   imageUri = result.getData().getData();
                                    //add the code into
                                    uri.add(imageUri);
                                }else{
                                    Toast.makeText(RegistrationForBreeder.this, "Not Allowed to pick more than 5 images", Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Toast.makeText(RegistrationForBreeder.this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
                          }else{
                            Toast.makeText(RegistrationForBreeder.this, "You Haven't Pick any Image", Toast.LENGTH_SHORT).show();
                        }
                      }

                });

        dropImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
            }
        });

    }//end of onCreate()

    @SuppressLint("ObsoleteSdkInt")
    private void imagePermission(){
            if(ContextCompat.checkSelfPermission(RegistrationForBreeder.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RegistrationForBreeder.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data){

            //this part is for to get multiple images
            if(data.getClipData() != null){

                countOfImages = data.getClipData().getItemCount();
                for(int j = 0; j<countOfImages; j++){

                    if(uri.size() < 5){
                      imageUri = data.getClipData().getItemAt(j).getUri();
                        uri.add(imageUri);
                    }else{
                        Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                    }
                }
                //then notify the adapter
                adapter.notifyDataSetChanged();
                Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
            }else{
                //this is for to get the single images
                if(uri.size() <5){
                 imageUri =data.getData();
                    uri.add(imageUri);
                }else{
                    Toast.makeText(this,"Not allowed to pick more than 5 images",Toast.LENGTH_SHORT).show();
                }
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
        }
        else{
            //this code is for if user not picked any image
            Toast.makeText(this,"You Haven't Pick any image",Toast.LENGTH_SHORT).show();
        }
    }

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
        if(uri.size()==0){
            Toast.makeText(this, "Certificate is needed", Toast.LENGTH_SHORT).show();

            return;
        }



        if(!(first.isEmpty()) && !(last.isEmpty()) && !(contact.isEmpty()) && !(address.isEmpty()) && !(zip.isEmpty()) && uri.size()!=0){
            //set the input layout to invisible
            reg_first.setVisibility(View.INVISIBLE);
            reg_middle.setVisibility(View.INVISIBLE);
            reg_last.setVisibility(View.INVISIBLE);
            reg_contact.setVisibility(View.INVISIBLE);
            reg_address.setVisibility(View.INVISIBLE);
            reg_zip.setVisibility(View.INVISIBLE);
            //recycler view
            recyclerView.setVisibility(View.INVISIBLE);
            //TextView
            CardView dropCardView;

            dropImageTextVIew.setVisibility(View.GONE);
            dropImageCardView.setVisibility(View.GONE);
            //Edit layout to invisible
            reg_firstEdit.setVisibility(View.INVISIBLE);
            reg_middleEdit.setVisibility(View.INVISIBLE);
            reg_lastEdit.setVisibility(View.INVISIBLE);
            reg_contactEdit.setVisibility(View.INVISIBLE);
            reg_addressEdit.setVisibility(View.INVISIBLE);
            reg_zipEdit.setVisibility(View.INVISIBLE);

            //set visible

            ScrollView sv = (ScrollView)findViewById(R.id.scrollID);
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
                                                Uri image = Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.noimage);
                                                Class_BreederClass breederClass= new Class_BreederClass(first,middle,last,contact,address,zip,email,password,image.toString(),"Pet Breeder", uri.toString());
                                                Class_Breeder_ShopName shopClass = new Class_Breeder_ShopName(last+" Shop","","","",image.toString(),"","");
                                                FirebaseDatabase.getInstance().getReference("Breeder").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Breeder Shop").setValue(shopClass);
                                                FirebaseDatabase.getInstance().getReference("Breeder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Breeder Info")
                                                        .setValue(breederClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(first+" "+
                                                                            middle+" "+last).build();
                                                                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(userProfileChangeRequest);
                                                                    compressImages();

                                                                    final SweetAlertDialog pDialog = new SweetAlertDialog(
                                                                            RegistrationForBreeder.this, SweetAlertDialog.PROGRESS_TYPE)
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
                                                                                                    startActivity(new Intent(RegistrationForBreeder.this, Login.class));
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
                                                                            RegistrationForBreeder.this, SweetAlertDialog.PROGRESS_TYPE)
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
                                    RegistrationForBreeder.this, SweetAlertDialog.PROGRESS_TYPE)
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
                                            .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                    pDialog.findViewById(cn.pedant.SweetAlert.R.id.confirm_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCC33")));
                                    pDialog.findViewById(cn.pedant.SweetAlert.R.id.confirm_button).setBackgroundResource(R.drawable.shapetwo);
                                }
                            }.start();

                            reg_email.setError("Email is already registered. Try another one!");
                            reg_emailEdit.getText().clear();
                            reg_passwordEdit.getText().clear();
                        }

                    }
                });
    }  //end of method registration()

    private void compressImages(){

        for (int i = 0; i< uri.size(); i++){
            try{
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri.get(i));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG,60,stream);
                byte[] imageByte = stream.toByteArray();
                uploadToFirebase(imageByte);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }



    private void uploadToFirebase(byte[] imageByte) {
        String userID="";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
        StorageReference fileRef = FirebaseStorage.getInstance().getReference("Breeder pictures/"+userID+"/"+"certificate/").child("images"+System.currentTimeMillis()+"_"+userID);

        fileRef.putBytes(imageByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gotoLogin(View view) {
        startActivity(new Intent(RegistrationForBreeder.this,Login.class));
        finish();
    }

    public void SignInBreeder_Onclick(View view) {
        registerUser();
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    @Override
    public void clicked(int size) {
       Toast.makeText(this,uri.size()+": selected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClick(int position) {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_dialog_zoom);

        TextView textView = dialog.findViewById(R.id.text_dialog);
        ImageView imageView = dialog.findViewById(R.id.image_view_dialog);
        Button buttonClose = dialog.findViewById(R.id.button_close_dialog);


        textView.setText("Image"+position);
        imageView.setImageURI(uri.get(position));
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



}