package com.example.halalfoodauthorityoss.model;

import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("name")
    public String name;
    @SerializedName("cnic")
    public String cnic;
    @SerializedName("c_mobile")
    public String c_mobile;
    @SerializedName("cpass")
    public String cpass;
    @SerializedName("district_id")
    public int district_id;
    @SerializedName("userID")
    public String userID;
    @SerializedName("success")
    public String success;
    @SerializedName("Name")
    public String cName;
    @SerializedName("Id")
    public String ID;
    @SerializedName("message")
    public String message;

    public String getName() {
        return name;
    }

    public String getCnic() {
        return cnic;
    }

    public String getUser_id() {
        return userID;
    }

    public String getSuccess()
    {
        return success;
    }

    public String getC_mobile() {
        return c_mobile;
    }

    public String getCpass() {
        return cpass;
    }

    public String getID() {
        return ID;
    }

    public String getcName() {
        return cName;
    }

    public String getMessage() {
        return message;
    }
}
