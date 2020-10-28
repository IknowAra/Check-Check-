package com.javaProject.checkcheck;

public class User {
    private String name;
    private boolean gender;

    public User(){

    }
    public User(String name, boolean gender){
        this.name = name;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public boolean getGender(){
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }
}
