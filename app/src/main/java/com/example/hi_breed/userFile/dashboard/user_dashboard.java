package com.example.hi_breed.userFile.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.hi_breed.R;
import com.example.hi_breed.screenLoading.LoadingDialog;
import com.example.hi_breed.loginAndRegistration.Login;
import com.example.hi_breed.userFile.profile.user_profile_fragment;
import com.example.hi_breed.userFile.shop.user_shop_fragment;
import com.example.hi_breed.userFile.cart.user_cart_fragment;
import com.example.hi_breed.userFile.home.user_home_fragment;
import com.example.hi_breed.userFile.likes.user_likes_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class user_dashboard extends AppCompatActivity {
    /*
    <! Todo: Initialized Variable !>
    */
    Window window;
    FirebaseUser firebaseUser;
    BottomNavigationView OwnerBottomNavigation,BreederBottomNavigation;
    Deque<Integer> IntegerDeque = new ArrayDeque<>(3);
    DatabaseReference find;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_dashboard);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        window.setStatusBarColor(Color.parseColor("#e28743"));
        window.setNavigationBarColor(Color.WHITE);


//Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        BreederBottomNavigation = findViewById(R.id.BreederBottom_navigation);
        OwnerBottomNavigation = findViewById(R.id.OwnerBottom_navigation);
        //load home Fragment
        loadFragment(new user_home_fragment());
        //Set home as a default fragment


         find = FirebaseDatabase.getInstance().getReference();

        readData(find, new OnGetDataListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onStart() {
                //when starting

            }

            @Override
            public void onFailure() {

            }
        });




     }//end of onCreate



    @SuppressLint("NonConstantResourceId")
    private Fragment BreederGetFragment(int itemId) {
        switch (itemId){
            case R.id.breeder_homeItem:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(0).setChecked(true);
                //return dashboard fragment
                return new user_home_fragment();
            case R.id.breeder_likesItem:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(1).setChecked(true);
                //return dashboard fragment
                return new user_likes_fragment();
            case R.id.breeder_shop:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(2).setChecked(true);
                //return notifications_fragment
                return new user_shop_fragment();
            case R.id.breeder_Order:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(3).setChecked(true);
                //return profile_fragment
                return new user_cart_fragment();

            case R.id.breeder_profileItem:
                //set checked dashboard fragment
                BreederBottomNavigation.getMenu().getItem(4).setChecked(true);
                //return profile_fragment
                return new user_profile_fragment();

        }
        BreederBottomNavigation.getMenu().getItem(0).setChecked(true);
        return new user_home_fragment();
    }

    @SuppressLint("NonConstantResourceId")
    private Fragment OwnerGetFragment(int itemId) {

        switch (itemId){
            case R.id.owner_homeItem:
                //set checked dashboard fragment
                OwnerBottomNavigation.getMenu().getItem(0).setChecked(true);
                //return dashboard fragment
                return new user_home_fragment();
            case R.id.owner_likesItem:
                //set checked dashboard fragment
                OwnerBottomNavigation.getMenu().getItem(1).setChecked(true);
                //return dashboard fragment
                return new user_likes_fragment();
            case R.id.owner_orderItem:
                //set checked dashboard fragment
                OwnerBottomNavigation.getMenu().getItem(2).setChecked(true);
                //return notifications_fragment
                return new user_cart_fragment();

            case R.id.owner_profileItem:
                //set checked dashboard fragment
                OwnerBottomNavigation.getMenu().getItem(3).setChecked(true);
                //return profile_fragment
                return new user_profile_fragment();

        }
        OwnerBottomNavigation.getMenu().getItem(0).setChecked(true);
        return new user_home_fragment();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,fragment,fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        IntegerDeque.pop();
        //check condition
        if(!IntegerDeque.isEmpty()) {
            //Pop to previous fragment
            //When deque list is not empty
            //load Fragment
            loadFragment(OwnerGetFragment(IntegerDeque.peek()));
        }
            else
            {
                //when deque list is empty
                //finish activity
                finish();
            }
        if(!IntegerDeque.isEmpty()) {
            //Pop to previous fragment
            //When deque list is not empty
            //load Fragment
            loadFragment(BreederGetFragment(IntegerDeque.peek()));
        }
        else
        {
            //when deque list is empty
            //finish activity
            finish();
        }

        }


        public interface OnGetDataListener {
            //this is for callbacks
            void onSuccess(DataSnapshot dataSnapshot);
            void onStart();
            void onFailure();
        }

    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        listener.onStart();
       final LoadingDialog loadingDialog = new LoadingDialog(user_dashboard.this);
        loadingDialog.startDialog();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ref.addListenerForSingleValueEvent(new ValueEventListener() {

                    @SuppressLint("ResourceType")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Breeder").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("Breeder Info").exists()){

                            IntegerDeque.push(R.id.breeder_homeItem);
                            OwnerBottomNavigation.setVisibility(View.GONE);
                            BreederBottomNavigation.setVisibility(View.VISIBLE);
/*                            BreederBottomNavigation.setBackgroundResource(R.color.white);
                            BreederBottomNavigation.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#FF000000")));*/
                            BreederBottomNavigation.setSelectedItemId(R.id.breeder_homeItem);
                            BreederBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                                @Override
                                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                    //get selected item id
                                    int id = item.getItemId();
                                    //check condition
                                    if(IntegerDeque.contains(id)){
                                        //when deque list contains selected id
                                        //check condition
                                        if(id == R.id.breeder_homeItem){
                                            //if id is equal to home fragment or tab 1
                                            //check condition
                                            if(IntegerDeque.size() !=1){
                                                //if deque list size is not equal to 1
                                                //check condition
                                                if(flag){
                                                    IntegerDeque.addFirst(R.id.breeder_homeItem);
                                                    //set flag to true
                                                    flag = false;
                                                }
                                            }
                                        }
                                        //Remove selected id from deque list
                                        IntegerDeque.remove(id);
                                    }
                                    //push selected id in deque list
                                    IntegerDeque.push(id);
                                    //load fragment
                                    loadFragment(BreederGetFragment(item.getItemId()));

                                    return true;
                                }
                            });
                        }
                        else if (snapshot.child("Owner").child(firebaseUser.getUid()).exists()){

                            IntegerDeque.push(R.id.owner_homeItem);
                            OwnerBottomNavigation.setVisibility(View.VISIBLE);
                            BreederBottomNavigation.setVisibility(View.GONE);
                            // clear FLAG_TRANSLUCENT_STATUS flag:

               /*            *//* OwnerBottomNavigation.setBackgroundResource(R.color.white);*//*
                            OwnerBottomNavigation.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#FF000000")));*/
                            OwnerBottomNavigation.setSelectedItemId(R.id.owner_homeItem);
                            OwnerBottomNavigation.setOnItemSelectedListener(
                                    new NavigationBarView.OnItemSelectedListener() {
                                        @Override
                                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                            //get selected item id
                                            int id = item.getItemId();
                                            //check condition
                                            if(IntegerDeque.contains(id)){
                                                //when deque list contains selected id
                                                //check condition
                                                if(id == R.id.owner_homeItem){
                                                    //if id is equal to home fragment or tab 1
                                                    //check condition
                                                    if(IntegerDeque.size() !=1){
                                                        //if deque list size is not equal to 1
                                                        //check condition
                                                        if(flag){
                                                            IntegerDeque.addFirst(R.id.owner_homeItem);
                                                            //set flag to true
                                                            flag = false;
                                                        }
                                                    }
                                                }
                                                //Remove selected id from deque list
                                                IntegerDeque.remove(id);
                                            }
                                            //push selected id in deque list
                                            IntegerDeque.push(id);
                                            //load fragment
                                            loadFragment(OwnerGetFragment(item.getItemId()));
                                            return true;
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(user_dashboard.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(user_dashboard.this, Login.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                        loadingDialog.dismissDialog();
                        listener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }
        });

    }

    }