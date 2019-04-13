package com.example.musafat;

import android.widget.EditText;

public class user {
    String image;
    String pass;
    String uname;
    String email;
    String contact;

    public user() {
    }

    public String getImage() {
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


    public  String getUname(){
        return uname;
    }
    public  void  setUname(){
        this.uname=uname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public user(String image, String pass, String name, String email, String contact) {
        this.image = image;
        this.pass = pass;
        this.uname = name;
        this.email = email;
        this.contact = contact;
        this.uid = uid;
    }

    String uid;



}
