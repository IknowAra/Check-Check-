package com.javaProject.checkcheck;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User{
    public String email;
    public String userName;

    public User(){

    }

    public User(String userName, String email   ){
        this.userName = userName;
        this.email = email;

    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

