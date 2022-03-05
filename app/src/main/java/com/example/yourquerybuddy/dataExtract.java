package com.example.yourquerybuddy;

public class dataExtract {

    dataExtract(){}

    private String FirstName;
    private String LastName;
    private String Email;
    private String Username;

    private String userType;


    dataExtract(String FirstName, String LastName, String Username, String Email, String Position){
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.Email=Email;
        this.Username=Username;
        this.userType =Position;
    }

    public String getUserType() {
        return userType;
    }
    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return Email;
    }

    public String getUsername() {
        return Username;
    }
}
