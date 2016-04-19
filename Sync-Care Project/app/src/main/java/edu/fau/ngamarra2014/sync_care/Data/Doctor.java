package edu.fau.ngamarra2014.sync_care.Data;


public class Doctor {
    private int id, patient;
    private String name, type;
    private String phone, email, fax;
    private String address, city, state, zip;

    public void setID(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setPatient(int id){
        this.patient = id;
    }
    public void setContactInfo(String phone, String fax, String email){
        this.phone = phone;
        this.fax = fax;
        this.email = email;
    }
    public void setAddress(String address, String city, String state, String zip){
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
    public int getID(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public int getPatient(){
        return this.patient;
    }
    public String[] getContactInfo(){
        return new String[] {this.phone, this.fax, this.email};
    }
    public String[] getAddress(){
        return new String[] {this.address, this.city, this.state, this.zip};
    }

    public void update(Doctor doc){
        this.name = doc.getName();
        this.type = doc.getType();
        this.phone = doc.getContactInfo()[0];
        this.fax = doc.getContactInfo()[1];
        this.email = doc.getContactInfo()[2];
        this.address = doc.getAddress()[0];
        this.city = doc.getAddress()[1];
        this.state = doc.getAddress()[2];
        this.zip = doc.getAddress()[3];
    }
}
