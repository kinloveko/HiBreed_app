package com.example.hi_breed.loginAndRegistration;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
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
import android.graphics.drawable.ColorDrawable;
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
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.droidsonroids.gif.GifImageView;

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
    FirebaseFirestore firestore;
    StorageReference storageReference;
    //variable declaration
    RecyclerView recyclerView;
    registrationRecyclerAdapter adapter;

    ArrayList <Uri> uri = new ArrayList<>();
    Uri imageUri;
    ActivityResultLauncher<Intent> activityResultLauncher;
    StorageReference fileRef;
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
        //Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
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

                    reg_first.setHelperText("First name is valid");
                    reg_first.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                } else {
                    reg_first.setHelperText("Name must have 2 characters");
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

                    reg_last.setHelperText("Last name is valid");
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
                        reg_address.setHelperText("Address is valid");
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
                    if(m.matches()) {
                        if (s.length() >= 4) {
                            reg_password.setHelperText("Valid password!");
                            reg_password.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
                        }
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
            reg_first.setHelperText("*Please enter your first name");
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
            reg_last.setHelperText("Please enter your last name");
            reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5CCD08")));
            reg_lastEdit.requestFocus();
            return;
        }
        else if (last.length() <2) {
            reg_last.setHelperText("Last name must have 2 letters");
            reg_last.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            reg_lastEdit.requestFocus();
            return;
        } //end of lastname
//contact condition
        CharSequence phonecs = reg_contactEdit.getText();
        if (contact.isEmpty()) {
            reg_contact.setHelperText("Please input your contact number");
            reg_contact.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            return;
        }
        else if (contact.length() != 11) {
            reg_contact.setHelperText("Contact must 11 numbers");
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
                reg_email.setHelperText("Please input your email");
                reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                reg_emailEdit.requestFocus();

                return;
            } else if (!(Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailcs))) {
                reg_email.setHelperText("Enter a valid email ex. example@gmail.com");
                reg_email.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
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
                    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {



                        boolean isNewUser = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();

                        if (isNewUser) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationForBreeder.this);
                            View view = View.inflate(RegistrationForBreeder.this,R.layout.screen_custom_alert,null);
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

                            mAuth.createUserWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){

                                                alert.dismiss();
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                Uri image = Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.noimage);
                                                Uri imageCover= Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.nobackground);
                                                Class_BreederClass breederClass= new Class_BreederClass(first,middle,last,contact,address,zip,email,password,image.toString(),imageCover.toString(),"Pet Breeder");
                                                Class_Breeder_ShopName shopClass = new Class_Breeder_ShopName(last+" Shop","","","",image.toString(),"", user.getUid());

                                                DocumentReference documentBreeder = firestore.collection("User").document(user.getUid());
                                                                  documentBreeder.set(breederClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                      @Override
                                                                      public void onComplete(@NonNull Task<Void> task) {
                                                                          uploadToStorage(user);

                                                                          //for creation of shop data
                                                                          DocumentReference documentShop = firestore.collection("Shop").document(user.getUid());
                                                                          documentShop.set(shopClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                              @Override
                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                  new Handler().postDelayed(new Runnable() {
                                                                                      @Override
                                                                                      public void run() {
                                                                                          AlertDialog.Builder builder2 = new AlertDialog.Builder(RegistrationForBreeder.this);
                                                                                          builder2.setCancelable(false);
                                                                                          View view = View.inflate(RegistrationForBreeder.this,R.layout.screen_custom_alert,null);
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
                                                                                          message.setText("Welcome to hiBreed application. You can now showcase all your pets and services online.");
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
                                                                                                  startActivity(new Intent(RegistrationForBreeder.this, Login.class));
                                                                                                  FirebaseAuth.getInstance().signOut();
                                                                                                  finish();
                                                                                              }
                                                                                          });
                                                                                      }
                                                                                  },2000);
                                                                              }
                                                                          });
                                                                      }
                                                                  }).addOnFailureListener(new OnFailureListener() {
                                                                      @Override
                                                                      public void onFailure(@NonNull Exception e) {
                                                                          Toast.makeText(RegistrationForBreeder.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                                      }
                                                                  });

                                            }
                                        }
                                    });

                        } else {
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(RegistrationForBreeder.this);
                            View view2 = View.inflate(RegistrationForBreeder.this,R.layout.screen_custom_alert,null);
                            builder3.setCancelable(false);
                            TextView title2 = view2.findViewById(R.id.screen_custom_alert_title);
                            TextView loadingText = view2.findViewById(R.id.screen_custom_alert_loadingText);
                            loadingText.setVisibility(View.GONE);
                            AppCompatImageView imageViewCompat2 = view2.findViewById(R.id.appCompatImageView);
                            imageViewCompat2.setVisibility(View.VISIBLE);
                            imageViewCompat2.setImageDrawable(getDrawable(R.drawable.screen_alert_image_error_border));
                            GifImageView gif = view2.findViewById(R.id.screen_custom_alert_gif);
                            gif.setVisibility(View.GONE);
                            TextView message2 = view2.findViewById(R.id.screen_custom_alert_message);
                            title2.setText("Email address already exist!");
                            message2.setText("Please use another email address.");
                            LinearLayout buttonLayout = view2.findViewById(R.id.screen_custom_alert_buttonLayout);
                            buttonLayout.setVisibility(View.VISIBLE);
                            MaterialButton cancel,okay;
                            cancel = view2.findViewById(R.id.screen_custom_dialog_btn_cancel);
                            cancel.setVisibility(View.GONE);
                            okay = view2.findViewById(R.id.screen_custom_alert_dialog_btn_done);
                            okay.setText("Okay");
                            okay.setBackgroundColor(Color.parseColor("#999999"));
                            okay.setTextColor(Color.WHITE);
                            builder3.setView(view2);
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



    int counter;
    List<String> savedImage = new ArrayList<>();
    private void uploadToStorage( FirebaseUser user) {

       fileRef = FirebaseStorage.getInstance().getReference("Breeder pictures/"+user.getUid()+"/Certificate");

        for ( int z=0; z < uri.size(); z++){
            final int last = z;
            Uri uriLast = uri.get(z);
            fileRef.child(uriLast.getLastPathSegment()).putFile(uri.get(z)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            fileRef.child(uriLast.getLastPathSegment()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    counter++;
                                    if(task.isSuccessful()){
                                        savedImage.add(task.getResult().toString());
                                    }else{
                                        Toast.makeText(RegistrationForBreeder.this, "Image cannot uploaded_"+uri.get(last)+"_"+last, Toast.LENGTH_SHORT).show();
                                    }
                                    if(counter == uri.size()){
                                        saveToFireStore(user);
                                    }
                                }
                            });
                        }
                }
            });
        }


    }

    private void saveToFireStore(FirebaseUser user) {
        DocumentReference def = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid());
        Map<String,String> data = new HashMap<>();
        for(int i = 0; i < savedImage.size(); i++){
            data.put("certificate"+i,savedImage.get(i));
        }
        def.collection("certificate").document("certificate").set(data,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

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