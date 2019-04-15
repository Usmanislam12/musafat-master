package com.example.musafat;

public class driver {
    String driverid;
    String drivername;
    String carname;
    String carcolour;
    String carnumber;
    String drivercontact;
    String driveremail;

    public String getDriveremail() {
        return driveremail;
    }

    public void setDriveremail(String driveremail) {
        this.driveremail = driveremail;
    }

    public String getDriverpass() {
        return driverpass;
    }

    public void setDriverpass(String driverpass) {
        this.driverpass = driverpass;
    }

    String driverpass;
    String image;
    float avgrating;


    public driver() {
    }

    public driver(String driverid, String drivername, String carname, String carcolour, String carnumber, String drivercontact,String driveremail,String driverpass, String image) {
        this.driverid = driverid;
        this.drivername = drivername;
        this.carname = carname;
        this.carcolour = carcolour;
        this.carnumber = carnumber;
        this.drivercontact = drivercontact;
        this.driveremail=driveremail;
        this.driverpass=driverpass;
        this.image = image;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getCarcolour() {
        return carcolour;
    }

    public void setCarcolour(String carcolour) {
        this.carcolour = carcolour;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getDrivercontact() {
        return drivercontact;
    }

    public void setDrivercontact(String drivercontact) {
        this.drivercontact = drivercontact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getAvgrating() {
        return avgrating;
    }

    public void setAvgrating(float avgrating) {
        this.avgrating = avgrating;
    }
}
