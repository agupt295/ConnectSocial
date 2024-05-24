package com.example.phase3.Classes;

import java.util.Date;

public class Users {

    private String email, fName, lName, date, gender, hometown, password;

    public Users(){
        email = "No-User";
        fName = "No-fName";
        lName = "No-lName";
    }

    public Users(String newEmail){
        this.email = newEmail;
    }

    public void setUserEmail(String newEmail){
        this.email = newEmail;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setHometown(String home){
        this.hometown = home;
    }

    public void setPassword(String newPassword){
        this.password = newPassword;
    }

    public String getUserEmail(){
        return this.email;
    }

    public void setUserfName(String newfName){
        this.fName = newfName;
    }

    public void setUserlName(String newlName){
        this.lName = newlName;
    }

    public void setDob(int year, int month, int date){
        this.date = year+"-"+month+"-"+date;
    }

    public String getUserfName(){
        return this.fName;
    }

    public String getUserlName(){
        return this.lName;
    }

    public String getDob(){
        return date;
    }

    public String getGender(){
        return gender;
    }

    public String getHometown(){
        return hometown;
    }

    public String getPassword(){
        return password;
    }

    public String toString(){
        return fName + " " + lName;
    }
}
