package com.example.hi_breed.userFile.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.Class_Breeder_ShopName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class shop_view_profile extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView viewShopName,viewShopVerified,viewShopReviews,viewShopLink,editShop;
    ImageView imageShopProfile,imageShopBackground;
    LinearLayout backLayoutShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_shop_view_profile);
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        imageShopBackground = findViewById(R.id.view_shop_coverImageID);
        imageShopProfile = findViewById(R.id.view_shop_Profile);

        backLayoutShop = findViewById(R.id.backLayoutShop);

        viewShopLink = findViewById(R.id.view_shop_link);
        viewShopName = findViewById(R.id.view_shop_name);
        viewShopVerified = findViewById(R.id.view_shop_verified);
        viewShopReviews = findViewById(R.id.view_shop_reviews);
        editShop = findViewById(R.id.editShop);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Breeder");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(firebaseUser.getUid()).child("Breeder Shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Class_Breeder_ShopName shopName = snapshot.getValue(Class_Breeder_ShopName.class);

                    if(shopName!=null){
                        viewShopName.setText(shopName.getShopName());
                        viewShopLink.setText("hiBreed.ph/"+shopName.getShopName());
                        viewShopLink.setTextSize(11);
                        viewShopReviews.setTextSize(11);
                        if(firebaseUser.isEmailVerified()){
                            viewShopVerified.setText("Verified");
                        }

                        Picasso.get().load(shopName.getProfImage()).into(imageShopProfile);

                        if(shopName.getBackgroundImage().equals("") || shopName.getBackgroundImage() == null) {
                            Picasso.get().load(shopName.getProfImage()).into(imageShopBackground);
                        }
                        else {
                            Picasso.get().load(shopName.getBackgroundImage()).into(imageShopBackground);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        editShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(shop_view_profile.this,shop_edit_profile.class));
            }
        });

    backLayoutShop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_view_profile.this.onBackPressed();
        }
    });

    }

}