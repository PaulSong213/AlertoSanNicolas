package com.example.alertosannicolas;

public class UserModel {
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String address;
    private String proofIdLink;
    private Boolean isVerified;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public UserModel(String uid, String email, String firstName, String lastName, String contactNumber, String address, String proofId, Boolean isVerified) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.proofIdLink = proofId;
        this.isVerified = isVerified;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProofIdLink() {
        return proofIdLink;
    }

    public void setProofIdLink(String proofId) {
        this.proofIdLink = proofId;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public Boolean getIsVerified(){
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified){
        this.isVerified = isVerified;
    }
}
