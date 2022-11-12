package com.example.hi_breed.userFile.home;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hi_breed.classesFile.Class_BreederClass;
import com.example.hi_breed.classesFile.Class_OwnerClass;
import com.example.hi_breed.R;
import com.example.hi_breed.petClass;
import com.example.hi_breed.userFile.profile.user_profile_edit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.internal.cache.DiskLruCache;

public class user_home_fragment extends Fragment {
    ImageSlider imageSlider;
    private DatabaseReference databaseReferenceOwner,databaseReferenceBreeder;
    private FirebaseUser firebaseUser;
    ImageView imageView;
    CardView profilePictureCard;



    public user_home_fragment() {
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

        return inflater.inflate(R.layout.owner_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePictureCard = view.findViewById(R.id.cardView);

        imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide_third, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_first, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_second, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        


        databaseReferenceBreeder = FirebaseDatabase.getInstance().getReference().child("Breeder");
        databaseReferenceOwner = FirebaseDatabase.getInstance().getReference().child("Owner");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        imageView = view.findViewById(R.id.profileImage);
        String userID ="";
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
            // calling the user info to display the details about the user.
            getUserInfo(userID);
        }

        profilePictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), user_profile_edit.class));
            }
        });

    }

    private void getUserInfo(String userID) {

                    databaseReferenceBreeder.child(userID).child("Breeder Info").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists() && snapshot.getChildrenCount() >0){
                                Class_BreederClass breederClass = snapshot.getValue(Class_BreederClass.class);
                                if(breederClass  != null) {
                                    Picasso.get().load(breederClass .getImage()).into(imageView);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //Reference for Owner
                    databaseReferenceOwner.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists() && snapshot.getChildrenCount() >0){
                                Class_OwnerClass ownerProfile = snapshot.getValue(Class_OwnerClass.class);
                                if(ownerProfile != null) {
                                    Picasso.get().load(ownerProfile.getImage()).into(imageView);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
     }
}
