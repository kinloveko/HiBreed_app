package com.example.hi_breed.userFile.shop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.Class_Breeder_ShopName;
import com.example.hi_breed.classesFile.datePickerClass;
import com.example.hi_breed.phoneAccess.phone_UncropperActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class shop_edit_profile extends AppCompatActivity  {

    String imageProfUpdate, imageBackgroundUpdate;
    Uri imgsUriProf,imgUriCover;
    ActivityResultLauncher<String> cropImage,cropImageCover;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference fileRefBreederProfile,fileRefBreederBackground;
    private String myText;
    private String selectGender = "";
    ImageView imageShopProfile,imageShopBackground,editSaveShop,imageCoverEdit;
    CardView imageEdit;
    LinearLayout backLayout;
    RelativeLayout shopNameLayout,bioLayout,genderLayout,birthdayLayout;
    TextView sName, bio, gender, birthday;
    // for date
    DatePickerDialog datePickerDialog;
    PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_shop_edit_profile);
        //imageView
        imageShopBackground = findViewById(R.id.view_shop_coverImageID);
        imageShopProfile = findViewById(R.id.view_shop_Profile);
        editSaveShop = findViewById(R.id.editShop);

        //CardView
        imageEdit = findViewById(R.id.cardView_shop_Profile);


        //TextView
        sName = findViewById(R.id.shop_edit_shopName);
        bio = findViewById(R.id.shop_edit_Bio);
        gender = findViewById(R.id.shop_edit_Gender);
        birthday = findViewById(R.id.shop_edit_birthday);

        //Layout
        //Relative Layout
        shopNameLayout = findViewById(R.id.shopnameLayout);
        bioLayout = findViewById(R.id.bioLayout);
        birthdayLayout = findViewById(R.id.birthdayLayout);
        genderLayout = findViewById(R.id.genderLayout);


          //If the image is not yet set for profile
        Uri uri = Uri.parse("android.resource://"+ getPackageName()+"/"+R.drawable.noimage);
        //Directing to specific Path in Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null)
        databaseReference = firebaseDatabase.getReference().child("Breeder").child(firebaseUser.getUid()).child("Breeder Shop");
        // to access the storage in firebase
        fileRefBreederProfile = FirebaseStorage.getInstance().getReference("Breeder pictures/"+firebaseUser.getUid()+"/"+"Profile Picture/").child("images_"+firebaseUser.getUid());
        fileRefBreederBackground = FirebaseStorage.getInstance().getReference("Breeder pictures/"+firebaseUser.getUid()+"/"+"Profile Background/").child("images_"+firebaseUser.getUid());
        //getting the data from FireBase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    Class_Breeder_ShopName shopName = snapshot.getValue(Class_Breeder_ShopName.class);
                    if(shopName!=null){

                        if(shopName.getShopName().equals("") || shopName.getShopName() == null)
                            sName.setText("Set shop name");
                        else
                            sName.setText(shopName.getShopName());


                        if(shopName.getBio().equals("") || shopName.getBio() == null)
                            bio.setText("Set bio");
                        else
                            bio.setText(shopName.getBio());


                        if(shopName.getGender().equals("") || shopName.getGender() == null)
                            gender.setText("Set gender");
                        else
                            gender.setText(shopName.getGender());


                        if(shopName.getBirthday().equals("") || shopName.getBirthday() == null )
                            birthday.setText("Set Birthday");
                        else
                            birthday.setText(shopName.getBirthday());


                        if(shopName.getBackgroundImage().equals("") || shopName.getBackgroundImage() == null) {
                            Picasso.get().load(shopName.getProfImage()).into(imageShopBackground);
                            imageBackgroundUpdate = shopName.getProfImage();
                        }
                        else {
                            Picasso.get().load(shopName.getBackgroundImage()).into(imageShopBackground);
                            imageBackgroundUpdate = shopName.getBackgroundImage();
                        }

                        if(shopName.getProfImage().equals("") || shopName.getProfImage() == null){
                            Picasso.get().load(uri).into(imageShopProfile);
                            imageProfUpdate = uri.toString();
                        }
                        else {
                            Picasso.get().load(shopName.getProfImage()).into(imageShopProfile);
                            imageProfUpdate = shopName.getProfImage();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(this.getApplicationContext(), phone_UncropperActivity.class);

            if(result!=null){
                intent.putExtra("SendingData", result.toString());
                startActivityForResult(intent,99);
                editSaveShop.setVisibility(View.VISIBLE);
            }
            else{
                editSaveShop.setVisibility(View.GONE);
                Toast.makeText(this.getApplicationContext(),"No changes",Toast.LENGTH_SHORT).show();
            }
        });

        cropImageCover = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(this.getApplicationContext(), phone_UncropperActivity.class);

            if(result!=null){
                intent.putExtra("SendingData", result.toString());
                startActivityForResult(intent,100);
                editSaveShop.setVisibility(View.VISIBLE);
            }
            else{
                editSaveShop.setVisibility(View.GONE);
                Toast.makeText(this.getApplicationContext(),"No changes",Toast.LENGTH_SHORT).show();
            }
        });

        //OnClickListener
        //Linear Layout
        backLayout = findViewById(R.id.backLayoutShopEdit);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(editSaveShop.isShown())
                   backButtonDialog();
               else
                   shop_edit_profile.this.onBackPressed();
            }
        });
        shopNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopName();
            }
        });
        bioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBioDialog();
            }
        });

        birthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerClass pickerClass = new datePickerClass(datePickerDialog, shop_edit_profile.this,R.id.shop_edit_birthday);
                if(pickerClass!=null) {
                    pickerClass.initDatePicker();
                }
            }
        });
        genderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });
        imageShopBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermissionBackground();
            }
        });
        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePermissionProfile();
            }
        });
        editSaveShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });

    }

    private void imagePermissionBackground() {
        Dexter.withContext(shop_edit_profile.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        cropImageCover.launch("image/*");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(shop_edit_profile.this, "Permission Denied",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void imagePermissionProfile() {
        Dexter.withContext(shop_edit_profile.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        cropImage.launch("image/*");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(shop_edit_profile.this, "Permission Denied",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    String resultsForProfile="";
    String resultsForCover="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == 102) {
            resultsForProfile = data.getStringExtra("CROPS");
            imgsUriProf = data.getData();
            if (resultsForProfile != null) {
                imgsUriProf = Uri.parse(resultsForProfile);
            }
            imageShopProfile.setImageURI(imgsUriProf);
        }
        if (requestCode == 100 && resultCode == 102){
            resultsForCover = data.getStringExtra("CROPS");
            imgUriCover = data.getData();
            if (resultsForCover != null) {
                imgUriCover = Uri.parse(resultsForCover);
            }
            imageShopBackground.setImageURI(imgUriCover);
        }

    }



    private void updateInfo() {
        String shopNameUpdate = sName.getText().toString().trim();
        String bioUpdate = bio.getText().toString().trim();
        String genderUpdate = gender.getText().toString().trim();
        String birthdayUpdate = birthday.getText().toString().trim();
        imgsUriProf = Uri.parse(resultsForProfile);
        imgUriCover = Uri.parse(resultsForCover);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating your shop");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() || snapshot.getChildrenCount() >0){
                    Class_Breeder_ShopName shopName = new Class_Breeder_ShopName
                            (shopNameUpdate,bioUpdate,genderUpdate,birthdayUpdate,imgsUriProf.toString(),imgUriCover.toString(),firebaseUser.getUid());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            databaseReference.child("shopName").setValue(shopName.getShopName());
                            databaseReference.child("bio").setValue(shopName.getBio());
                            databaseReference.child("gender").setValue(shopName.getGender());
                            databaseReference.child("birthday").setValue(shopName.getBirthday());
                            databaseReference.child("Breeder").setValue(firebaseUser.getUid());
                            fileRefBreederProfile.putFile(imgsUriProf).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    databaseReference.child("profImage").setValue(shopName.getProfImage());
                                    FirebaseDatabase.getInstance().getReference().child("Breeder").child(firebaseUser.getUid())
                                            .child("Breeder Info").child("image").setValue(shopName.getProfImage());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                            fileRefBreederBackground.putFile(imgUriCover).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    databaseReference.child("backgroundImage").setValue(shopName.getBackgroundImage());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                           FirebaseDatabase.getInstance().getReference("Shop").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                            firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("shopName").setValue(shopName.getShopName());
                                            firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("bio").setValue(shopName.getBio());
                                            firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("gender").setValue(shopName.getGender());
                                            firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("birthday").setValue(shopName.getBirthday());
                                            fileRefBreederProfile.putFile(imgsUriProf).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("profImage").setValue(imgsUriProf.toString());

                                                    FirebaseDatabase.getInstance().getReference().child("Breeder").child(firebaseUser.getUid())
                                                            .child("Breeder Info").child("image").setValue(shopName.getProfImage());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                            fileRefBreederBackground.putFile(imgUriCover).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    firebaseDatabase.getReference("Shop").child(firebaseUser.getUid()).child("backgroundImage").setValue(imgUriCover.toString());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }



                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                            editSaveShop.setVisibility(View.GONE);
                            Toast.makeText(shop_edit_profile.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    },2000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showShopName() {
        int style = AlertDialog.THEME_HOLO_LIGHT;
        AlertDialog builder =new AlertDialog.Builder(shop_edit_profile.this,style).create();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 30);
        builder.getWindow().setBackgroundDrawable(inset);

        builder.setTitle("Shop name");
        EditText shopInput = new EditText(shop_edit_profile.this);
        shopInput.setSingleLine();
        shopInput.setHint("Input here...");
        shopInput.setBackgroundColor(Color.TRANSPARENT);
        shopInput.setInputType(InputType.TYPE_CLASS_TEXT);
        FrameLayout container = new FrameLayout(shop_edit_profile.this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        shopInput.setLayoutParams(params);
        container.addView(shopInput);

        builder.setView(container);

        builder.setButton(Dialog.BUTTON_POSITIVE,"Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myText=shopInput.getText().toString();
                if(myText.equals("")||myText.equals(null)){
                    Toast.makeText(shop_edit_profile.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                }else{
                    sName.setText(myText);
                    editSaveShop.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }
        });
        builder.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    private void showBioDialog() {
        int style = AlertDialog.THEME_HOLO_LIGHT;
        AlertDialog builder =new AlertDialog.Builder(shop_edit_profile.this,style).create();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 30);
        builder.getWindow().setBackgroundDrawable(inset);
        builder.setTitle("Add bio");
        EditText bioInput = new EditText(shop_edit_profile.this);
        bioInput.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        bioInput.setMinLines(4);
        bioInput.setHint("Input here....");
        bioInput.setBackgroundResource(Color.TRANSPARENT);
        bioInput.setSingleLine(false);
        bioInput.setMaxLines(8);
        FrameLayout container = new FrameLayout(shop_edit_profile.this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        bioInput.setLayoutParams(params);
        container.addView(bioInput);

        builder.setView(container);

            builder.setButton(Dialog.BUTTON_POSITIVE,"Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        myText=bioInput.getText().toString();
                        if(myText.equals("")||myText.equals(null)){
                            Toast.makeText(shop_edit_profile.this, "Cannot make any changes", Toast.LENGTH_SHORT).show();
                        }else{
                            bio.setText(myText);
                            editSaveShop.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }

                }
            });
            builder.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                }
            });

        builder.show();

    }

    private void showGenderDialog() {
        int style = AlertDialog.THEME_HOLO_LIGHT;
        String [] choose = {"Male","Female","Other"};
        AlertDialog.Builder builder =new AlertDialog.Builder(shop_edit_profile.this,style);
        builder.setTitle("Choose Gender");
        builder.setSingleChoiceItems(choose, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectGender = choose[which];
                gender.setText(selectGender);
                editSaveShop.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    //TODO: TO GET THE CURRENT DATE
         /**
          *  IT IS WORKING BUT I DON'T NEED IT
          * private String getTodaysDate() {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                return makeDateString(day,month,year);
           }*/


    private void backButtonDialog() {

        AlertDialog builder =new AlertDialog.Builder(shop_edit_profile.this).create();
        ColorDrawable back = new ColorDrawable(Color.WHITE);
        InsetDrawable inset = new InsetDrawable(back, 30);
        builder.getWindow().setBackgroundDrawable(inset);
        builder.setTitle("Are you sure?");

        builder.setButton(Dialog.BUTTON_POSITIVE,"Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    editSaveShop.setVisibility(View.GONE);
                    shop_edit_profile.this.onBackPressed();
                    dialog.dismiss();
            }
        });
        builder.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

}