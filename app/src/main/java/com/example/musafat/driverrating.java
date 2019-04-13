package com.example.musafat;

public class driverrating {
    String uid;
    String Uname;
    float Rating;

    public driverrating(String uid, String uname, float rating) {
        this.uid = uid;
        Uname = uname;
        Rating = rating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }
}
