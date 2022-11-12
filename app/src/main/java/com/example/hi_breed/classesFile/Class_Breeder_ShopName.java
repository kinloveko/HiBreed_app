package com.example.hi_breed.classesFile;

import com.google.firebase.auth.FirebaseUser;

public class Class_Breeder_ShopName {

    String bio,gender,birthday,shopName,profImage,backgroundImage;
    String Breeder;
    public Class_Breeder_ShopName(){

    }
    public Class_Breeder_ShopName(String shopName, String bio, String gender, String birthday, String profImage, String backgroundImage, String Breeder){
        this.shopName = shopName;
        this.birthday = birthday;
        this.bio = bio;
        this.gender = gender;
        this.backgroundImage = backgroundImage;
        this.profImage = profImage;
        this.Breeder=Breeder;
    }

    public void setBreeder(String Breeder) {
        this.Breeder = Breeder;
    }

    public String getBreeder() {
        return Breeder;
    }

    public String getProfImage() {
        return profImage;
    }

    public void setProfImage(String profImage) {
        this.profImage = profImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
