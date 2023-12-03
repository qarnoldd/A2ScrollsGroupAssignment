package lab13.qifan.group2.a2.models;

import lab13.qifan.group2.a2.utilities.IdGenerator;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String salt;
    private String email;
    private int phone;
    private String idKey;
    private UserType userType;


    // Constructors
    public User(String username, String password, String email, int phone, String idKey, UserType userType, String salt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.salt = salt;
        this.idKey = (idKey == null || idKey.isEmpty()) ? IdGenerator.generateId() : idKey;
        this.userType = userType;
    }

//    public User() {
//        // Default constructor
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    public String getSalt() { return this.salt;}
    public void setSalt(String salt) {this.salt = salt;}
}
