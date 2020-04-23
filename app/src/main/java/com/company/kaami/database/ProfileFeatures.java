package com.company.kaami.database;

public class ProfileFeatures {
    private String aadharno;
    private String name;
    private String phoneno;
    private String education;
    private String skills;
    private String experience;
    private String address;
    private String email;
    private String password;

    public ProfileFeatures() {

    }

    public ProfileFeatures(String aadharno, String name, String phoneno, String education, String skills, String experience, String address, String email, String password) {
        this.aadharno = aadharno;
        this.name = name;
        this.phoneno = phoneno;
        this.education = education;
        this.skills = skills;
        this.experience = experience;
        this.address = address;
        this.email = email;
        this.password=password;
    }

    public String getAadharno() {
        return aadharno;
    }

    public void setAadharno(String aadharno) {
        this.aadharno = aadharno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
