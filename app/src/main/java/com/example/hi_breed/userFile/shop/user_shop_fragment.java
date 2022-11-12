package com.example.hi_breed.userFile.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.Class_Breeder_ShopName;
import com.example.hi_breed.userFile.shop.pet.pet_add;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
        If the user is an owner display none
 */
public class user_shop_fragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    TextView viewShop;

    TextView breederName, breederLabel;
    CircleImageView imageView;
    CardView myPet,myService;
    public user_shop_fragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.owner_shop_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Breeder");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //CardView
        myPet = view.findViewById(R.id.myPetsCardView8);
        myService = view.findViewById(R.id.myServicesCardView9);
        viewShop = view.findViewById(R.id.viewShopID);
        breederName = view.findViewById(R.id.shop_userNameTxtView);
        breederLabel = view.findViewById(R.id.shop_userLabelTextView);
        imageView = view.findViewById(R.id.shop_profileImage);

        databaseReference.child(firebaseUser.getUid()).child("Breeder Shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Class_Breeder_ShopName shopName = snapshot.getValue(Class_Breeder_ShopName.class);
                    if (shopName!= null) {
                        String ownerName = shopName.getShopName();
                        breederName.setText(ownerName);
                        breederLabel.setText("hiBreed.ph/"+shopName.getShopName().trim());
                        Picasso.get().load(shopName.getProfImage()).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(),shop_view_profile.class));
            }
        });
        myPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user_shop_fragment.this.getActivity(), pet_add.class));
            }
        });
    }
}