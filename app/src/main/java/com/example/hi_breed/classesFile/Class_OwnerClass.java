package com.example.hi_breed.classesFile;

public class Class_OwnerClass {
  String firstName;
    String lastName;
    String middleName;
    String contactNumber;
    String email;
    String address;
    String zipCode;
    String pass;
    String image;


    String backgroundImage;
    String role;

   public Class_OwnerClass(){

    }
    public Class_OwnerClass(String firstName, String middleName , String lastName, String contactNumber,
                            String address, String zipCode, String email, String pass, String image,String backgroundImage, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
        this.pass = pass;
        this.image = image;
        this.role = role;
    }

    public void updateOwner(String firstName, String middleName ,String lastName, String contactNumber,
                      String address, String zipCode,String image,String backgroundImage,String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.zipCode = zipCode;
        this.image = image;
        this.role = role;
        this.backgroundImage = backgroundImage;
    }



    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage(){
       return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
