package com.example.hi_breed.userFile.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi_breed.classesFile.Class_BreederClass;
import com.example.hi_breed.classesFile.Class_OwnerClass;
import com.example.hi_breed.R;
import com.example.hi_breed.phoneAccess.phone_UncropperActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import java.util.Objects;
import java.util.regex.Pattern;

public class user_profile_edit extends AppCompatActivity {

    LinearLayout backLayout, saveLayoutButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceOwner,databaseReferenceBreeder;
    StorageReference fileRefOwner,fileRefBreeder;
    private FirebaseUser firebaseUser;
    Uri imgsUri;
    ActivityResultLauncher<String> cropImage;
    ImageView imageView;
    String userID;

    TextView editProfile;
    TextInputLayout reg_first,reg_last,reg_middle,reg_contact,reg_address,reg_zip;
    Button cancelbutton,updateButton;

    final Pattern phonePattern = Pattern.compile("^(09)\\d{9}");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_user_identity);


        editProfile = findViewById(R.id.editProfiletxt);
        reg_first = findViewById(R.id.firstname_profInput);
        reg_middle = findViewById(R.id.middle_profInput);
        reg_last = findViewById(R.id.lastname_profInput);
        reg_contact= findViewById(R.id.contact_profInput);
        reg_address = findViewById(R.id.address_profInput);
        reg_zip = findViewById(R.id.zip_profInput);


        imageView = findViewById(R.id.profileImage);
        saveLayoutButton = findViewById(R.id.saveLayout);

        updateButton = findViewById(R.id.updateButton);
        backLayout = findViewById(R.id.backLayout);


        mAuth = FirebaseAuth.getInstance();
        databaseReferenceBreeder = FirebaseDatabase.getInstance().getReference().child("Breeder");
        databaseReferenceOwner = FirebaseDatabase.getInstance().getReference().child("Owner");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID ="";
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
            getUserInfo(userID);
        }
        else{
            Toast.makeText(this.getApplicationContext(),"No info",Toast.LENGTH_SHORT).show();
        }

        fileRefBreeder = FirebaseStorage.getInstance().getReference("Breeder pictures/"+userID+"/"+"Profile Picture/").child("images_"+userID);
        fileRefOwner = FirebaseStorage.getInstance().getReference("Owner pictures/"+userID+"/"+"Profile Picture/").child("images_"+userID);

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_profile_edit.this.onBackPressed();
            }
        });
         cancelbutton = findViewById(R.id.cancelButton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_profile_edit.this.onBackPressed();
            }
        });




        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(user_profile_edit.this.getApplicationContext(), phone_UncropperActivity.class);

            if(result!=null){
                saveLayoutButton.setVisibility(View.VISIBLE);
                intent.putExtra("SendingData", result.toString());
               startActivityForResult(intent,101);

            }
            else{
                saveLayoutButton.setVisibility(View.GONE);
                Toast.makeText(this.getApplicationContext(),"No changes",Toast.LENGTH_SHORT).show();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermission();
                saveLayoutButton.setVisibility(View.VISIBLE);
            }
        });



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });

        saveLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              uploadProfileImage();
            }
        });

    }


    private void getUserInfo(String userID) {

        databaseReferenceBreeder.child(userID).child("Breeder Info").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                            Class_BreederClass breederProfile = snapshot.getValue(Class_BreederClass.class);

                            if (breederProfile != null) {
                                reg_first.getEditText().setText(breederProfile.getFirstName());
                                reg_middle.getEditText().setText(breederProfile.getMiddleName());
                                reg_last.getEditText().setText(breederProfile.getLastName());
                                reg_contact.getEditText().setText(breederProfile.getContactNumber());
                                reg_address.getEditText().setText(breederProfile.getAddress());
                                reg_zip.getEditText().setText(breederProfile.getZipCode());
                                Picasso.get().load(breederProfile.getImage()).into(imageView);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReferenceOwner.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.getChildrenCount() >0){

                            Class_OwnerClass ownerProfile = snapshot.getValue(Class_OwnerClass.class);

                            if(ownerProfile != null) {
                                reg_first.getEditText().setText(ownerProfile.getFirstName());
                                reg_middle.getEditText().setText(ownerProfile.getMiddleName());
                                reg_last.getEditText().setText(ownerProfile.getLastName());
                                reg_contact.getEditText().setText(ownerProfile.getContactNumber());
                                reg_address.getEditText().setText(ownerProfile.getAddress());
                                reg_zip.getEditText().setText(ownerProfile.getZipCode());
                                Picasso.get().load(ownerProfile.getImage()).into(imageView);
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
    String results="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==101 && resultCode == 102){
                results = data.getStringExtra("CROPS");
                imgsUri= data.getData();
                if(results!=null){
                    imgsUri = Uri.parse(results);
                }
                imageView.setImageURI(imgsUri);
            }
    }
    private void uploadProfileImage(){
        final ProgressDialog progressDialog = new ProgressDialog(user_profile_edit.this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();
        imgsUri = Uri.parse(results);

        if (firebaseUser != null) {
           userID = firebaseUser.getUid();
        }

            databaseReferenceBreeder.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        fileRefBreeder.putFile(imgsUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               //Breeder info
                                FirebaseDatabase.getInstance().getReference("Breeder")
                                    .child(userID).child("Breeder Info").child("image")
                                        .setValue(imgsUri.toString());
                                //for shop name
                                FirebaseDatabase.getInstance().getReference("Breeder")
                                        .child(userID).child("Breeder Shop").child("profImage")
                                        .setValue(imgsUri.toString());
                                //for shop
                                FirebaseDatabase.getInstance().getReference("Shop").child(firebaseUser.getUid())
                                        .child("profImage").setValue(imgsUri.toString());


                                saveLayoutButton.setVisibility(View.GONE);
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Image Uploaded Successfully!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        });

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
            databaseReferenceOwner.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        fileRefOwner.putFile(imgsUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                FirebaseDatabase.getInstance().getReference("Owner").child(userID).child("image")
                                        .setValue(imgsUri.toString());
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                                saveLayoutButton.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Image Uploaded Successfully!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
    }
    private void checkInput(){


        String first = Objects.requireNonNull(reg_first.getEditText().getText()).toString().trim();
        String middle = Objects.requireNonNull(reg_middle.getEditText().getText()).toString().trim();
        String last = Objects.requireNonNull(reg_last.getEditText().getText()).toString().trim();
        String contact = Objects.requireNonNull(reg_contact.getEditText().getText()).toString().trim();
        String address = Objects.requireNonNull(reg_address.getEditText().getText()).toString().trim();
        String zip = Objects.requireNonNull(reg_zip.getEditText().getText()).toString().trim();
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
        CharSequence phonecs = reg_contact.getEditText().getText();
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

        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
        }
        if(first!=null  && last!=null && contact!=null && address!=null && zip!=null) {
            uploadToFirebase();
        }
    }
    private void uploadToFirebase(){

        String first = Objects.requireNonNull(reg_first.getEditText().getText()).toString().trim();
        String middle = Objects.requireNonNull(reg_middle.getEditText().getText()).toString().trim();
        String last = Objects.requireNonNull(reg_last.getEditText().getText()).toString().trim();
        String contact = Objects.requireNonNull(reg_contact.getEditText().getText()).toString().trim();
        String address = Objects.requireNonNull(reg_address.getEditText().getText()).toString().trim();
        String zip = Objects.requireNonNull(reg_zip.getEditText().getText()).toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating your info");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();

            FirebaseDatabase.getInstance().getReference("Breeder").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() && snapshot.getChildrenCount() >0){
                        Class_BreederClass breederClass = new Class_BreederClass();
                        breederClass.updateBreeder(first, middle, last, contact, address, zip, imgsUri.toString(), "Pet Breeder");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                databaseReferenceBreeder.child(userID).child("Breeder Info").child("firstName").setValue(breederClass.getFirstName());
                                databaseReferenceBreeder.child(userID).child("Breeder Info").child("middleName").setValue(breederClass.getMiddleName());
                                databaseReferenceBreeder.child(userID).child("Breeder Info").child("lastName").setValue(breederClass.getLastName());
                                databaseReferenceBreeder.child(userID).child("Breeder Info").child("contactNumber").setValue(breederClass.getContactNumber());
                                databaseReferenceBreeder.child(userID).child("Breeder Info").child("address").setValue(breederClass.getAddress());
                                databaseReferenceBreeder.child(userID).child("Breeder Info").child("zipCode").setValue(breederClass.getZipCode());
                                databaseReferenceBreeder.child(userID).child("Breeder Info").child("breeder").setValue(breederClass.getBreeder());
                                fileRefBreeder.putFile(imgsUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        databaseReferenceBreeder.child(userID).child("Breeder Info").child("image").setValue(imgsUri.toString());
                                        //for shop name
                                        databaseReferenceBreeder
                                                .child(userID).child("Breeder Shop").child("profImage")
                                                .setValue(imgsUri.toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                                saveLayoutButton.setVisibility(View.GONE);
                                Toast.makeText(user_profile_edit.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();

                            }
                        }, 2000);

                    }

              }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });

            FirebaseDatabase.getInstance().getReference("Owner").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists() && snapshot.getChildrenCount() >0){
                        Class_OwnerClass ownerClass = new Class_OwnerClass();

                        ownerClass.updateOwner(first, middle, last, contact, address, zip, imgsUri.toString(), "Pet Owner");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                databaseReferenceOwner.child(userID).child("firstName").setValue(ownerClass.getFirstName());
                                databaseReferenceOwner.child(userID).child("middleName").setValue(ownerClass.getMiddleName());
                                databaseReferenceOwner.child(userID).child("lastName").setValue(ownerClass.getLastName());
                                databaseReferenceOwner.child(userID).child("contactNumber").setValue(ownerClass.getContactNumber());
                                databaseReferenceOwner.child(userID).child("address").setValue(ownerClass.getAddress());
                                databaseReferenceOwner.child(userID).child("zipCode").setValue(ownerClass.getZipCode());
                                databaseReferenceOwner.child(userID).child("owner").setValue(ownerClass.getOwner());
                                fileRefOwner.putFile(imgsUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        databaseReferenceOwner.child(userID).child("image").setValue(imgsUri.toString());
                                  }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if (progressDialog.isShowing())
                                            progressDialog.dismiss();
                                        Toast.makeText(user_profile_edit.this, "Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                  saveLayoutButton.setVisibility(View.GONE);
                                    Toast.makeText(user_profile_edit.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }, 2000);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
