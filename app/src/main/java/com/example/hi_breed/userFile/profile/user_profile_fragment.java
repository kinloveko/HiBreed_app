package com.example.hi_breed.userFile.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi_breed.screenLoading.LoadingDialog;
import com.example.hi_breed.classesFile.Class_BreederClass;
import com.example.hi_breed.classesFile.Class_OwnerClass;
import com.example.hi_breed.R;
import com.example.hi_breed.screenLoading.screen_WelcomeToHiBreed;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class user_profile_fragment extends Fragment {

    int i = -1;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceOwner,databaseReferenceBreeder;
    private FirebaseUser firebaseUser;
    String  userID;
    CircleImageView imageView;
    ConstraintLayout editLayout;
    ConstraintLayout accountLayout,cardLayout,faqLayout,aboutLayout;
    TextView ownerDisplay;
    TextView label;

    public user_profile_fragment() {
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

        return inflater.inflate(R.layout.owner_profile_fragment, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Variables

        //images
        imageView = view.findViewById(R.id.profileImage);
        //layout
        editLayout = view.findViewById(R.id.profileIconLayout);
        accountLayout = view.findViewById(R.id.accountLayout);
        cardLayout = view.findViewById(R.id.cardLayout);
        aboutLayout = view.findViewById(R.id.aboutLayout);
        faqLayout = view.findViewById(R.id.faqLayout);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        databaseReferenceBreeder = FirebaseDatabase.getInstance().getReference().child("Breeder");
        databaseReferenceOwner = FirebaseDatabase.getInstance().getReference().child("Owner");

        ownerDisplay = view.findViewById(R.id.userNameTxtView);
         label = view.findViewById(R.id.userLabelTextView);

 /*
    TODO: Getting the info of the user that is currently logged in.
  */


        if (firebaseUser != null) {
         userID = firebaseUser.getUid();
            getUserBreeder(userID);

        }else{
            Toast.makeText(this.getActivity().getApplicationContext(),"No current User",Toast.LENGTH_SHORT).show();
        }


//to edit
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(user_profile_fragment.this.getActivity(), user_profile_edit.class));
            }
        });

// to save the image



//account settings
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      transistion();
            }
        });

//signout

     ConstraintLayout signout = view.findViewById(R.id.signOutLayout);
     signout.setOnClickListener(new View.OnClickListener() {
         final LoadingDialog loadingDialog = new LoadingDialog(user_profile_fragment.this.getActivity());
         @Override
         public void onClick(View v) {
             loadingDialog.startDialog();

             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {

                     FirebaseAuth.getInstance().signOut();
                     Intent intent = new Intent(getActivity(), screen_WelcomeToHiBreed.class);
                     startActivity(intent);
                     getActivity().finish();
                     loadingDialog.dismissDialog();
                 }
             },1500);

         }
     });

    /*
    BACK LAYOUT CODE
    TODO: if the user click the back button. it will be directed to the home page

  */

        LinearLayout backArrowLayout = view.findViewById(R.id.backLayout);
    backArrowLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                getActivity().onBackPressed();
        }
    });
    }


//retrieve the data in the firebase and display it on profile
    public void getUserBreeder(String userID) {


       databaseReferenceBreeder.child(userID).child("Breeder Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Class_BreederClass breederClass = snapshot.getValue(Class_BreederClass.class);

                        if (breederClass != null) {
                            String ownerName = breederClass.getFirstName() + " " + breederClass.getMiddleName() + " "
                                    + breederClass.getLastName();
                            ownerDisplay.setText(ownerName);
                            label.setText(breederClass.getBreeder());
                            Picasso.get().load(breederClass.getImage()).into(imageView);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       databaseReferenceOwner.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Class_OwnerClass ownerProfile = snapshot.getValue(Class_OwnerClass.class);
                    if (ownerProfile != null) {
                        String ownerName = ownerProfile.getFirstName() + " " + ownerProfile.getMiddleName() + " "
                                + ownerProfile.getLastName();
                        ownerDisplay.setText(ownerName);
                        label.setText(ownerProfile.getOwner());
                        Picasso.get().load(ownerProfile.getImage()).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
  }


    final FragmentActivity sPlashScreen = getActivity();


    private void transistion(){
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(getActivity(), user_profile_edit.class);

                getActivity().overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out);
                startActivity(intent);
            }
        };
        mSplashThread.start();
    }
}