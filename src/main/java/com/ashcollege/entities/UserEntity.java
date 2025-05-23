package com.ashcollege.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserEntity extends BaseEntity {

    private String username;

    @JsonIgnore
    private RoleEntity role;

    private String phoneNumber;

    @JsonIgnore
    private String otp;

    @JsonIgnore
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    private String passwordRecovery; // סיסמא שתיווצר במידה ומשתמש שכח סיסמא וצריך לאפס לו

    public UserEntity() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPasswordRecovery() {
        return passwordRecovery;
    }

    public void setPasswordRecovery(String passwordRecovery) {
        this.passwordRecovery = passwordRecovery;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserEntity(String username, String password, String firstName, String lastName, String email, String role, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = new RoleEntity();
        setTheRole(role);
        this.phoneNumber = phoneNumber;
    }

    private void setTheRole(String role) {
        switch (role) {
            case "Student" -> this.role.setId(1);
            case "Lecturer" -> this.role.setId(2);
            case "Admin" -> this.role.setId(3);
        }
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", role=" + role +
                ", passwordRecovery='" + passwordRecovery + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
