package com.example.hi_breed.userFile.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.hi_breed.adapter.petClass;
import com.example.hi_breed.adapter.productPetRecyclerAdapter;
import com.example.hi_breed.R;
import com.example.hi_breed.classesFile.Class_BreederClass;
import com.example.hi_breed.classesFile.Class_OwnerClass;
import com.example.hi_breed.userFile.profile.user_profile_edit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class user_home_fragment extends Fragment {
    ImageSlider imageSlider;

    private FirebaseUser firebaseUser;
    FirebaseFirestore fireStore;

    ImageView imageView;
    CardView profilePictureCard;
    RecyclerView recyclerView;
    productPetRecyclerAdapter adapter;
    ArrayList<petClass> list;
    DocumentReference databaseReference;
    public user_home_fragment() {
        // Required empty public constructor
    }

    String hold="";
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
        //fireStore
        fireStore = FirebaseFirestore.getInstance();

        //user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //image Slider
        imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide_third, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_first, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide_second, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        //RecyclerView
        recyclerView = view.findViewById(R.id.petDisplay);

        //imageView
        imageView = view.findViewById(R.id.profileImage);
        String userID ="";
        if (firebaseUser != null) {
            userID = firebaseUser.getUid();
            // calling the user info to display the details about the user.

            databaseReference = fireStore.collection("User").document(userID);
            getUserInfo(databaseReference);
        }

        profilePictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), user_profile_edit.class));
            }
        });

    }

    private void getUserInfo(DocumentReference userID) {
        userID.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String role = documentSnapshot.getString("role");
                        if(role.equals("Pet Breeder")){
                           String breederClass = documentSnapshot.getString("image");
                            if(breederClass== "" || breederClass==null ){
                                imageView.setImageResource(R.drawable.noimage);
                            }
                            else{
                                Picasso.get().load(breederClass).into(imageView);
                            }

                        }
                        else{
                            String ownerClass= documentSnapshot.getString("image");
                            if(ownerClass == "" || ownerClass == null){
                                imageView.setImageResource(R.drawable.noimage);
                            }
                            else{
                                Picasso.get().load(ownerClass).into(imageView);
                            }
                        }
                    }
            }
        });

     }
}
