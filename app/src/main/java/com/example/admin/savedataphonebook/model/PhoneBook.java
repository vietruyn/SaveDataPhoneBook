package com.example.admin.savedataphonebook.model;

/**
 * Created by jce on 8/10/15.
 */
public class PhoneBook {

    //-> Atributos (Comunes)

    private String name, phoneNumber, email;

    private int type;

    //-> constructor
    public PhoneBook() {

    }

    public PhoneBook(String name, String phoneNumber, String email, int type) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.type = type;
    }

    //-> Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
