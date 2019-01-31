package com.example.dghaghar.mailboxapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Users implements Serializable {
    public String userID, email, password, name;

    public Users() {
    }

    public Users(String userID, String email, String password, String name) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userID='" + userID + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
