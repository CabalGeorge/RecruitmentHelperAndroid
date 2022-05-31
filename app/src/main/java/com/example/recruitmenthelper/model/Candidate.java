package com.example.recruitmenthelper.model;


public class Candidate {

    private int candidateId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String city;
    private String birthdate;
    private String gender;
    private String interestPosition;
    private byte[] cv;
    private byte[] gdprConsent;
    private int workExperience;

    public Candidate() {}

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInterestPosition() {
        return interestPosition;
    }

    public void setInterestPosition(String interestPosition) {
        this.interestPosition = interestPosition;
    }

    public byte[] getCv() {
        return cv;
    }

    public void setCv(byte[] cv) {
        this.cv = cv;
    }

    public byte[] getGdprConsent() {
        return gdprConsent;
    }

    public void setGdprConsent(byte[] gdprConsent) {
        this.gdprConsent = gdprConsent;
    }

    public int getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(int workExperience) {
        this.workExperience = workExperience;
    }
}
