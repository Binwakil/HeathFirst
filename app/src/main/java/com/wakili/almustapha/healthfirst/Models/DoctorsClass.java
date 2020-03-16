package com.wakili.almustapha.healthfirst.Models;


public class DoctorsClass {

    public String id;
    public String Name, Email, Phone, Office, Qualifications, Specialization, About, Imageurl;


    public DoctorsClass(String Name, String Email, String Phone, String Office, String Qualifications, String Specialization, String About, String Imageurl) {
        this.Name = Name;
        this.Email = Email;
        this.Phone= Phone;
        this.Office = Office;
        this.Qualifications = Qualifications;
        this.Specialization = Specialization;
        this.About = About;
        this.Imageurl = Imageurl;
    }
    // create an empty constructor
    public DoctorsClass() {
    }

    public void setName(String name) {
        Name = name;
    }
    public String getName() {
        return Name;
    }
    public void setQualifications(String qualifications) {

        Qualifications = qualifications;
    }
    public String getQualifications() {
        return Qualifications;
    }
    public void setEmail(String email){
        Email = email;
    }
    public String getEmail(){
        return Email;
    }
    public void setPhone(String phone){
        Phone = phone;
    }
    public String getPhone(){
        return Phone;
    }
    public void setOffice(String office){
        Office = office;
    }
    public String getOffice(){
        return Office;
    }
    public void setSpecialization(String specialization){
        Specialization = specialization;
    }
    public String getSpecialization(){
        return Specialization;
    }
    public void setAbout(String about){
        About = about;
    }
    public String getAbout(){
        return About;
    }
    public void setImageurl(String imageurl){
        Imageurl = imageurl;
    }
    public String getImageurl(){
        return Imageurl;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
