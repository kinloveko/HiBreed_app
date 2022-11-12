package com.example.hi_breed.classesFile;

public class Class_BreederClass {
  String firstName,lastName,middleName,contactNumber,email,address,zipCode,pass,image,breeder,certificate;


   public Class_BreederClass(){

    }
    public Class_BreederClass(String firstName, String middleName , String lastName, String contactNumber,
                              String address, String zipCode, String email, String pass, String image, String breeder, String certificate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
        this.pass = pass;
        this.image = image;
        this.breeder = breeder;
        this.certificate = certificate;
    }



    public void updateBreeder(String firstName, String middleName , String lastName, String contactNumber,
                              String address, String zipCode, String image, String owner) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.zipCode = zipCode;
        this.image = image;
        this.breeder= owner;
    }


    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getBreeder() {
        return breeder;
    }

    public void setBreeder(String owner) {
            this.breeder = owner;
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
