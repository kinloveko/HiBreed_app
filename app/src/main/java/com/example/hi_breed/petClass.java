package com.example.hi_breed;

public class petClass {
    String pet_birthday,pet_breed,pet_breeder,pet_colorMarkings,pet_description,pet_gender,pet_images,pet_name,pet_price,pet_vaccine;


    public petClass(String pet_birthday, String pet_breed, String pet_breeder, String pet_colorMarkings, String pet_description,
                    String pet_gender, String pet_images, String pet_name, String pet_price, String pet_vaccine) {
        this.pet_birthday = pet_birthday;
        this.pet_breed = pet_breed;
        this.pet_breeder = pet_breeder;
        this.pet_colorMarkings = pet_colorMarkings;
        this.pet_description = pet_description;
        this.pet_gender = pet_gender;
        this.pet_images = pet_images;
        this.pet_name = pet_name;
        this.pet_price = pet_price;
        this.pet_vaccine = pet_vaccine;
    }

    public String getPet_birthday() {
        return pet_birthday;
    }

    public void setPet_birthday(String pet_birthday) {
        this.pet_birthday = pet_birthday;
    }

    public String getPet_breed() {
        return pet_breed;
    }

    public void setPet_breed(String pet_breed) {
        this.pet_breed = pet_breed;
    }

    public String getPet_breeder() {
        return pet_breeder;
    }

    public void setPet_breeder(String pet_breeder) {
        this.pet_breeder = pet_breeder;
    }

    public String getPet_colorMarkings() {
        return pet_colorMarkings;
    }

    public void setPet_colorMarkings(String pet_colorMarkings) {
        this.pet_colorMarkings = pet_colorMarkings;
    }

    public String getPet_description() {
        return pet_description;
    }

    public void setPet_description(String pet_description) {
        this.pet_description = pet_description;
    }

    public String getPet_gender() {
        return pet_gender;
    }

    public void setPet_gender(String pet_gender) {
        this.pet_gender = pet_gender;
    }

    public String getPet_images() {
        return pet_images;
    }

    public void setPet_images(String pet_images) {
        this.pet_images = pet_images;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_price() {
        return pet_price;
    }

    public void setPet_price(String pet_price) {
        this.pet_price = pet_price;
    }

    public String getPet_vaccine() {
        return pet_vaccine;
    }

    public void setPet_vaccine(String pet_vaccine) {
        this.pet_vaccine = pet_vaccine;
    }
}
